package com.byteasy.permission.interceptor;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.NumberUtil;
import com.byteasy.common.component.OpenApiLoadingCache;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.common.util.RequestUtil;
import com.byteasy.enums.YesNo;
import com.byteasy.platform.api.IOpenapiLogService;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.platform.entity.OpenapiLog;
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
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Aspect
@Component
@Order(20)
@Log4j2
public class OpenapiLogAspect {

    @Autowired
    private OpenApiLoadingCache openApiLoadingCache;

    @Autowired
    private IOpenapiLogService openapiLogService;

    @Autowired(required = false)
    @Qualifier("openapiLogExecutor")
    private Executor openapiLogExecutor;

    @Pointcut("@annotation(com.byteasy.annotations.Openapi) || @within(com.byteasy.annotations.Openapi)")
    public void openapiPointcut() {
        // 切点签名，保持空方法体即可
    }

    @Around("openapiPointcut()")
    public Object aroundOpenapiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = RequestUtil.getHttpServletRequest();
        if (request == null) {
            return joinPoint.proceed();
        }

        UserInfo userInfo = TokenUtil.getUserinfo();
        // 1. 避免微服务间自动传递链路产生的自反调用
        if (userInfo != null && userInfo.isForwardBy()) {
            return joinPoint.proceed();
        }

        // 2. 避免日志服务自身的记录接口产生递归
        String actualUri = request.getRequestURI();
        if (actualUri != null && actualUri.contains("/openapiLogs")) {
            return joinPoint.proceed();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = request.getMethod().toUpperCase();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        String fullUri = SpringContextUtil.getContextPath() + RequestMappingResolver.getFullMethodUri(targetClass, method);

        // 3. 匹配 OpenAPI 接口定义
        Openapi openApi = findOpenApi(actualUri, fullUri, methodName);
        if (openApi == null || !Boolean.TRUE.equals(openApi.getLoggable())) {
            return joinPoint.proceed();
        }

        long startTime = System.currentTimeMillis();
        Object result = null;
        Throwable error = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable ex) {
            error = ex;
            throw ex;
        } finally {
            long costTime = System.currentTimeMillis() - startTime;
            // 组装并异步持久化日志
            asyncRecordLog(openApi, request, result, error, costTime, userInfo);
        }
    }

    /**
     * 异步记录日志
     */
    private void asyncRecordLog(Openapi openApi, HttpServletRequest request, Object result,
                                Throwable error, long costTime, UserInfo userInfo) {
        try {
            OpenapiLog logEntity = new OpenapiLog();
            logEntity.setServiceCode(openApi.getServiceCode());
            logEntity.setUri(request.getRequestURI());
            logEntity.setHttpMethod(Openapi.HttpMethod.fromValue(request.getMethod().toUpperCase()));
            logEntity.setQueryString(request.getQueryString());
            logEntity.setContentType(request.getContentType());
            logEntity.setHttpHeader(extractHeaders(request));
            logEntity.setHttpBody(extractRequestBody(request));

            logEntity.setOpenapiId(openApi.getId());
            logEntity.setOpenapiName(openApi.getName());
            logEntity.setUserInfo(userInfo);
            logEntity.setCostTime(costTime);
            logEntity.setCreatedBy(userInfo.getName());
            logEntity.setCreatorId(userInfo.getUsername());
            logEntity.setCreatedTime(LocalDateTime.now());

            // 🔥 核心点：识别是否为重放调用，并提取来源日志 ID
            String isReplayHeader = request.getHeader("X-Openapi-Replay");
            if ("true".equalsIgnoreCase(isReplayHeader)) {
                logEntity.setIsReplay(YesNo.YES);
                String sourceIdStr = request.getHeader("X-Replay-Source-Id");
                if (StringUtils.isNotBlank(sourceIdStr) && NumberUtil.isNumber(sourceIdStr)) {
                    logEntity.setReplaySourceId(Long.valueOf(sourceIdStr));
                }
            } else {
                logEntity.setIsReplay(YesNo.NO);
                logEntity.setReplaySourceId(null);
            }

            // 结果与异常处理
            if (error != null) {
                logEntity.setStatus(OpenapiLog.Status.FAILED);
                logEntity.setErrorMessage(ExceptionUtil.stacktraceToString(error, 2048));
                logEntity.setHttpResponse(error.getMessage());
            } else {
                logEntity.setStatus(OpenapiLog.Status.SUCCESS);
                logEntity.setErrorMessage(null);
                if (result != null) {
                    try {
                        logEntity.setHttpResponse(JacksonUtil.toJson(result));
                    } catch (Exception e) {
                        logEntity.setHttpResponse(result.toString());
                    }
                }
            }

            Runnable saveTask = () -> {
                try {
                    openapiLogService.saveOrUpdate(logEntity);
                } catch (Exception e) {
                    log.error("保存 OpenAPI 执行日志异常: uri={}, error={}", logEntity.getUri(), e.getMessage());
                }
            };

            CompletableFuture.runAsync(saveTask, openapiLogExecutor);
        } catch (Exception e) {
            log.error("构建 OpenAPI 执行日志失败: {}", e.getMessage(), e);
        }
    }

    private Map<String, String[]> extractHeaders(HttpServletRequest request) {
        Map<String, String[]> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames == null) return headerMap;

        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            Enumeration<String> values = request.getHeaders(name);
            List<String> valList = new ArrayList<>();
            while (values.hasMoreElements()) {
                valList.add(values.nextElement());
            }
            headerMap.put(name, valList.toArray(new String[0]));
        }
        return headerMap;
    }

    private String extractRequestBody(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = org.springframework.web.util.WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    String encoding = wrapper.getCharacterEncoding() != null ? wrapper.getCharacterEncoding() : StandardCharsets.UTF_8.name();
                    return new String(buf, encoding);
                } catch (Exception e) {
                    log.error("提取 RequestBody 字符集异常", e);
                }
            }
        }
        return null;
    }

    private Openapi findOpenApi(String actualUri, String path, String httpMethod) {
        try {
            return openApiLoadingCache.load(openApiLoadingCache.buildCompositeKey(actualUri, path, Openapi.HttpMethod.fromValue(httpMethod)));
        } catch (Exception e) {
            log.error("未找到对应 openapi: actualUri={}, path={}, httpMethod={}", actualUri, path, httpMethod);
            return null;
        }
    }
}