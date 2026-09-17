package com.byteasy.permission.interceptor;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.Result;
import com.byteasy.Status;
import com.byteasy.common.component.OpenApiLoadingCache;
import com.byteasy.common.controller.AbstractController;
import com.byteasy.common.controller.OpenapiCommonController;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.common.util.RequestUtil;
import com.byteasy.exception.BizException;
import com.byteasy.permission.component.RoleOpenapiLoadingCache;
import com.byteasy.permission.component.UserRoleLoadingCache;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.util.EntityCodeUtil;
import com.byteasy.util.I18nUtil;
import com.byteasy.util.RequestMappingResolver;
import com.byteasy.util.SpringContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Aspect
@Component
@Order(9)
@Log4j2
public class OpenapiAuthAspect {

    @Autowired
    private OpenApiLoadingCache openApiLoadingCache;
    @Autowired
    private UserRoleLoadingCache userRoleLoadingCache;
    @Autowired
    private RoleOpenapiLoadingCache roleOpenapiLoadingCache;
    @Autowired(required = false)
    @Qualifier("openapiLogExecutor")
    private Executor openapiLogExecutor;

    @Pointcut("@annotation(com.byteasy.annotations.Openapi) || @within(com.byteasy.annotations.Openapi)")
    public void openapiPointcut() {
        // 切点签名，保持空方法体即可
    }

    @Around("openapiPointcut()")
    public Object aroundOpenapiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        UserInfo userInfo = TokenUtil.getUserinfo();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.byteasy.annotations.Openapi annotation = method.getAnnotation(com.byteasy.annotations.Openapi.class);
        HttpServletRequest request = RequestUtil.getHttpServletRequest();

        String methodName = request.getMethod().toUpperCase();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        String fullUri = SpringContextUtil.getContextPath() + RequestMappingResolver.getFullMethodUri(targetClass, method);
        String actualUri = request.getRequestURI();
        Object[] args = joinPoint.getArgs();

        if (userInfo == null) {
            // public
            Openapi openApi = findOpenApi(actualUri, fullUri, methodName);
            if (openApi == null) {
                return joinPoint.proceed();
            }
            if (Openapi.PermissionType.PUBLIC.equals(openApi.getPermissionType())) {
                return joinPoint.proceed();
            }
            return Result.error("缺少认证信息");
        } else {
            // 仅拦截用户发起的请求，避免递归调用
            if (userInfo.isForwardBy()) {
                return joinPoint.proceed();
            }

            /**
             * 系统未注册接口，调用时自动注册为LOGIN访问接口
             */
            Openapi openApi = findOpenApi(actualUri, fullUri, methodName);
            if (openApi == null) {
                Openapi openapi = new Openapi();
                openapi.setCode(EntityCodeUtil.generateCode());
                openapi.setServiceCode(SpringUtil.getApplicationName());
                String name = annotation.title();
                if (StringUtils.isEmpty(name)) {
                    name = method.getName();
                }
                openapi.setName(I18nUtil.convertI18nFieldZhValue(name));
                openapi.setHttpMethod(Openapi.HttpMethod.fromValue(methodName));
                openapi.setUri(fullUri);
                openapi.setType(Openapi.Type.DEVELOP);
                openapi.setPermissionType(Openapi.PermissionType.LOGIN);
                openapi.setLoggable(openapi.getHttpMethod() != null && !Openapi.HttpMethod.GET.equals(openApi.getHttpMethod()));
                Runnable saveTask = null;
                if (AbstractController.class.isAssignableFrom(targetClass)) {
                    Method getEntityClassMethod = ReflectUtil.getMethod(targetClass, "getEntityClass");
                    Class<?> entityClass = ReflectUtil.invoke(joinPoint.getTarget(), getEntityClassMethod);
                    openapi.setClassName(entityClass.getName());
                    saveTask = () -> {
                        try {
                            openApiLoadingCache.register(openapi);
                        } catch (Exception e) {
                            log.error("Openapi not found, auto register api failed: {}, error: {}", openapi, e.getMessage());
                        }
                    };
                } else if (OpenapiCommonController.class.isAssignableFrom(targetClass)) {
                    openapi.setClassName(extractClassName(signature, args));
                    saveTask = () -> {
                        try {
                            openApiLoadingCache.register(openapi);
                        } catch (Exception e) {
                            log.error("Openapi not found, auto register api failed: {}, error: {}", openapi, e.getMessage());
                        }
                    };
                } else {
                    log.error("Openapi not found, src: {}, uri: {}, method: {}, class: {} ", fullUri, actualUri, methodName, targetClass.getName());
                }

                if (saveTask != null) {
                    log.info("Openapi not found, auto register: {}", openapi);
                    CompletableFuture.runAsync(saveTask, openapiLogExecutor);
                }
                return joinPoint.proceed();
            } else {
                switch (openApi.getPermissionType()) {
                    case PUBLIC, LOGIN:
                        return joinPoint.proceed();
                    case AUTH: {
                        Set<Long> roleIds = userRoleLoadingCache.load(userInfo.getUsername());
                        if (roleIds == null || roleIds.isEmpty()) {
                            throw new BizException(Status.AUTHENTICATION_FAILED.getI18nKey());
                        }
                        for (Long roleId : roleIds) {
                            Set<Long> apiIds = roleOpenapiLoadingCache.load(roleId);
                            if (apiIds != null) {
                                if (apiIds.contains(openApi.getId())) {
                                    return joinPoint.proceed();
                                }
                            }
                        }
                        throw new BizException(Status.AUTHENTICATION_FAILED.getI18nKey());
                    }
                }
                return joinPoint.proceed();
            }
        }
    }

    /**
     * 智能定位 Openapi 模型定义
     */
    private Openapi findOpenApi(String actualUri, String path, String httpMethod) {
        try {
            return openApiLoadingCache.load(openApiLoadingCache.buildCompositeKey(actualUri, path, Openapi.HttpMethod.fromValue(httpMethod)));
        } catch (Exception e) {
            log.error("未找到对应 openapi: actualUri={}, path={}, httpMethod={}", actualUri, path, httpMethod);
            return null;
        }
    }

    /**
     * 自动从方法形参中提取 className
     */
    private String extractClassName(MethodSignature signature, Object[] args) {
        String[] paramNames = signature.getParameterNames();
        for (int i = 0; i < paramNames.length; i++) {
            if ("className".equals(paramNames[i]) && args[i] instanceof String) {
                return (String) args[i];
            }
        }
        return null;
    }
}
