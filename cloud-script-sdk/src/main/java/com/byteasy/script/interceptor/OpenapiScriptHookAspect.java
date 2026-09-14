package com.byteasy.script.interceptor;

import com.byteasy.Result;
import com.byteasy.component.OpenApiFactory;
import com.byteasy.platform.entity.JsScript;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.script.component.JsScriptFactory;
import com.byteasy.script.util.JsScriptExecutor;
import com.byteasy.util.RequestMappingResolver;
import com.google.common.collect.Maps;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Aspect
@Component
@Order(10) // 确保在统一日志/事务切面之后或合适的位置执行
@Log4j2
public class OpenapiScriptHookAspect {

    @Autowired
    private OpenApiFactory openApiFactory;

    @Autowired
    private JsScriptFactory jsScriptFactory;
    @Autowired
    private org.springframework.transaction.support.TransactionTemplate transactionTemplate;

    @Pointcut("@annotation(com.byteasy.annotations.Openapi) || @within(com.byteasy.annotations.Openapi)")
    public void openapiPointcut() {
        // 切点签名，保持空方法体即可
    }

    @Around("openapiPointcut()")
    public Object aroundOpenapiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        // 使用编程式事务包裹整个调用链
        return transactionTemplate.execute(status -> {
            try {
                HttpServletRequest request = getHttpServletRequest();

                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                Method method = signature.getMethod();
                String methodName = method.getName();
                Class<?> targetClass = joinPoint.getTarget().getClass();
                String fullUri = RequestMappingResolver.getFullMethodUri(targetClass, method);

                String actualUri = request.getRequestURI();

                Object[] args = joinPoint.getArgs();
                Openapi openApi = findOpenApi(fullUri, methodName);
                if (openApi == null) {
                    return Result.error("Openapi:" + methodName + " " + fullUri + " not found");
                } else {
                    Map<String, Object> scriptContext = buildScriptContext(openApi.getClassName(), signature, args, request);

                    // 1. 前置脚本
                    if (openApi != null) {
                        executePreScript(openApi, scriptContext);
                    }

                    // 2. Controller & Service
                    Object result = joinPoint.proceed();

                    // 3. 后置脚本
                    if (openApi != null) {
                        scriptContext.put("result", result instanceof Result ? ((Result<?>) result).getData() : result);
                        executePostScript(openApi, scriptContext);
                    }

                    return result;
                }
            } catch (Throwable ex) {
                // 标记事务回滚
                status.setRollbackOnly();
                if (ex instanceof RuntimeException) {
                    throw (RuntimeException) ex;
                }
                throw new RuntimeException("OpenAPI 切面执行异常，事务已回滚", ex);
            }
        });
    }

    // =========================================================================
    // 脚本执行器与内部辅助方法
    // =========================================================================

    private void executePreScript(Openapi openapi, Map<String, Object> input) throws ExecutionException {
        Long scriptId = openapi.getPreScriptId();
        if (scriptId == null || scriptId <= 0) return;

        JsScript jsScriptObj = jsScriptFactory.getScript(scriptId);
        String script = jsScriptObj.getScript();
        if (StringUtils.isBlank(script)) return;

        log.info("【AOP 触发前置脚本】apiCode={}, scriptId={}", openapi.getCode(), scriptId);
        JsScriptExecutor.runScriptWithTimeout(input, script, Long.toString(scriptId), 30000);
    }

    private void executePostScript(Openapi openapi, Map<String, Object> input) throws ExecutionException {
        Long scriptId = openapi.getPostScriptId();
        if (scriptId == null || scriptId <= 0) return;

        JsScript jsScriptObj = jsScriptFactory.getScript(scriptId);
        String script = jsScriptObj.getScript();
        if (StringUtils.isBlank(script)) return;

        log.info("【AOP 触发后置脚本】apiCode={}, scriptId={}", openapi.getCode(), scriptId);
        JsScriptExecutor.runScriptWithTimeout(input, script, Long.toString(scriptId), 30000);
    }

    /**
     * 智能定位 Openapi 模型定义
     */
    private Openapi findOpenApi(String path, String httpMethod) {
        if (openApiFactory == null || StringUtils.isEmpty(path)) return null;
        try {
            return openApiFactory.getOpenApi(openApiFactory.buildCompositeKey(path, Openapi.HttpMethod.fromValue(httpMethod)));
        } catch (Exception e) {
            log.debug("未找到对应 openapi: path={}, httpMethod={}", path, httpMethod);
            return null;
        }
    }

    /**
     * 组装标准的脚本上下文 input
     */
    private Map<String, Object> buildScriptContext(String className, MethodSignature signature, Object[] args, HttpServletRequest request) {
        Map<String, Object> input = Maps.newHashMap();
        input.put("className", className);
        input.put("headers", extractHeaders(request));

        Map<String, String[]> params = request.getParameterMap();

        String[] paramNames = signature.getParameterNames();
        for (int i = 0; i < paramNames.length; i++) {
            String pName = paramNames[i];
            Object pVal = args[i];
            input.put(pName, pVal);
        }
        input.put("params", com.byteasy.util.MapUtil.convertToMap(params));
        return input;
    }

    private Map<String, String> extractHeaders(HttpServletRequest request) {
        Map<String, String> headers = Maps.newHashMap();
        if (request == null) return headers;
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames != null && headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, request.getHeader(name));
        }
        return headers;
    }

    private HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}
