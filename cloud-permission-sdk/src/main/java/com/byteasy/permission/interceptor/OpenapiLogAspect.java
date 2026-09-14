package com.byteasy.permission.interceptor;

import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.common.util.RequestUtil;
import com.byteasy.common.component.OpenApiLoadingCache;
import com.byteasy.platform.entity.Openapi;
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
@Order(20)
@Log4j2
public class OpenapiLogAspect {

    @Autowired
    private OpenApiLoadingCache openApiLoadingCache;

    @Pointcut("@annotation(com.byteasy.annotations.Openapi) || @within(com.byteasy.annotations.Openapi)")
    public void openapiPointcut() {
        // 切点签名，保持空方法体即可
    }

    @Around("openapiPointcut()")
    public Object aroundOpenapiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        UserInfo userInfo = TokenUtil.getUserinfo();
        // 仅拦截用户发起的请求，避免递归调用
        if (userInfo == null || userInfo.isForwardBy()) {
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
            // do nothing
            return joinPoint.proceed();
        } else {
            // TODO 记录日志
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
}
