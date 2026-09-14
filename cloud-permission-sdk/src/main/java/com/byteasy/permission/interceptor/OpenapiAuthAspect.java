package com.byteasy.permission.interceptor;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.Result;
import com.byteasy.common.controller.OpenapiCommonController;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.common.util.RequestUtil;
import com.byteasy.component.I18nUtil;
import com.byteasy.common.component.OpenApiLoadingCache;
import com.byteasy.common.controller.AbstractController;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.util.EntityCodeUtil;
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
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(9)
@Log4j2
public class OpenapiAuthAspect {

    @Autowired
    private OpenApiLoadingCache openApiLoadingCache;

    @Pointcut("@annotation(com.byteasy.annotations.Openapi) || @within(com.byteasy.annotations.Openapi)")
    public void openapiPointcut() {
        // 切点签名，保持空方法体即可
    }

    @Around("openapiPointcut()")
    public Object aroundOpenapiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        UserInfo userInfo = TokenUtil.getUserinfo();

        if (userInfo == null) {
            return Result.error("缺少认证信息");
        }
        // 仅拦截用户发起的请求，避免递归调用
        if (userInfo.isForwardBy()) {
            return joinPoint.proceed();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.byteasy.annotations.Openapi annotation = method.getAnnotation(com.byteasy.annotations.Openapi.class);
        HttpServletRequest request = RequestUtil.getHttpServletRequest();

        String methodName = request.getMethod().toUpperCase();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        String fullUri = SpringContextUtil.getContextPath() + RequestMappingResolver.getFullMethodUri(targetClass, method);

        String actualUri = request.getRequestURI();

        Object[] args = joinPoint.getArgs();
        Openapi openApi = findOpenApi(actualUri, fullUri, methodName);
        if (openApi == null) {
            Openapi openapi = new Openapi();
            openapi.setCode(EntityCodeUtil.generateCode());
            openapi.setServiceCode(SpringUtil.getApplicationName());
            openapi.setName(I18nUtil.convertI18nFieldZhValue(annotation.title()));
            openapi.setHttpMethod(Openapi.HttpMethod.fromValue(methodName));
            openapi.setUri(fullUri);
            openapi.setType(Openapi.Type.DEVELOP);
            openapi.setPermissionType(Openapi.PermissionType.PUBLIC);
            openapi.setLoggable(false);
            if (AbstractController.class.isAssignableFrom(targetClass)) {
                Method getEntityClassMethod = ReflectUtil.getMethod(targetClass, "getEntityClass");
                Class<?> entityClass = ReflectUtil.invoke(joinPoint.getTarget(), getEntityClassMethod);
                openapi.setClassName(entityClass.getName());
                openApiLoadingCache.register(openapi);
                log.info("Openapi not found, auto registe: {}", openapi);
            } else if (OpenapiCommonController.class.isAssignableFrom(targetClass)) {
                openapi.setClassName(extractClassName(signature, args));
                openApiLoadingCache.register(openapi);
                log.info("Openapi not found, auto registe: {}", openapi);
            } else {
                log.error("Openapi not found, src: {}, uri: {}, method: {}, class: {} ", fullUri, actualUri, methodName, targetClass.getName());
            }
            return joinPoint.proceed();
        } else {
            // TODO 根据角色进行校验
            return joinPoint.proceed();
        }
    }

    /**
     * 智能定位 Openapi 模型定义
     */
    private Openapi findOpenApi(String actualUri, String path, String httpMethod) {
        if (openApiLoadingCache == null || StringUtils.isEmpty(path)) return null;
        try {
            return openApiLoadingCache.load(openApiLoadingCache.buildCompositeKey(actualUri, path, Openapi.HttpMethod.fromValue(httpMethod)));
        } catch (Exception e) {
            log.debug("未找到对应 openapi: path={}, httpMethod={}", path, httpMethod);
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
