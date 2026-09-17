package com.byteasy.permission.interceptor;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.byteasy.common.component.OpenApiLoadingCache;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.common.util.RequestUtil;
import com.byteasy.platform.api.IOpenapiLogService;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.platform.entity.OpenapiLog;
import com.byteasy.util.RequestMappingResolver;
import com.byteasy.util.SpringContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
    // 🔥 注入刚刚定义的专用日志线程池
    @Autowired
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
        // 1. 仅拦截用户发起的请求，避免内部系统转发或递归调用
        if (userInfo != null && userInfo.isForwardBy()) {
            return joinPoint.proceed();
        }

        // 2. 物理防重入：如果当前正在请求日志本身的 API，坚决不再记录日志！
        String actualUri = request.getRequestURI();
        if (actualUri != null && actualUri.contains("/openapiLogs")) {
            return joinPoint.proceed();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = request.getMethod().toUpperCase();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        String fullUri = SpringContextUtil.getContextPath() + RequestMappingResolver.getFullMethodUri(targetClass, method);

        // 3. 匹配 OpenAPI 模型定义
        Openapi openApi = findOpenApi(actualUri, fullUri, methodName);

        // 如果未定义或未开启 loggable，直接放行
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
            // 异步持久化接口调用日志，避免影响主业务性能
            asyncRecordLog(openApi, request, result, error, costTime, userInfo);
        }
    }

    /**
     * 异步组装并记录日志
     */
    private void asyncRecordLog(Openapi openApi, HttpServletRequest request, Object result,
                                Throwable error, long costTime, UserInfo userInfo) {
        try {
            // 必须在当前主线程先提取 Request 相关参数（防止异步线程脱离 Web 作用域后 request 被销毁或回收）
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

            if (error != null) {
                logEntity.setStatus(OpenapiLog.Status.FAILED);
                // 记录异常错误信息摘要（控制长度，防止大堆栈撑爆日志）
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

            // 异步调用 Feign 写入，避免阻塞业务返回
            CompletableFuture.runAsync(() -> {
                try {
                    openapiLogService.saveOrUpdate(logEntity);
                } catch (Exception e) {
                    log.error("保存 OpenAPI 接口调用日志失败: uri={}, error={}", logEntity.getUri(), e.getMessage());
                }
            }, openapiLogExecutor);

        } catch (Exception e) {
            log.error("构建 OpenAPI 调用日志异常: {}", e.getMessage(), e);
        }
    }

    /**
     * 提取请求头为 Map<String, String[]>
     */
    private Map<String, String[]> extractHeaders(HttpServletRequest request) {
        Map<String, String[]> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames == null) {
            return headerMap;
        }

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

    /**
     * 提取请求体（支持 ContentCachingRequestWrapper 包装流）
     */
    private String extractRequestBody(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = org.springframework.web.util.WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    String encoding = wrapper.getCharacterEncoding() != null ? wrapper.getCharacterEncoding() : StandardCharsets.UTF_8.name();
                    return new String(buf, encoding);
                } catch (Exception e) {
                    log.debug("读取 RequestBody 字符集异常", e);
                }
            }
        }
        return null;
    }

    /**
     * 智能定位 Openapi 模型定义
     */
    private Openapi findOpenApi(String actualUri, String path, String httpMethod) {
        try {
            return openApiLoadingCache.load(openApiLoadingCache.buildCompositeKey(actualUri, path, Openapi.HttpMethod.fromValue(httpMethod)));
        } catch (ExecutionException e) {
            log.error("未找到对应 openapi: actualUri={}, path={}, httpMethod={}", actualUri, path, httpMethod);
            return null;
        }
    }
}