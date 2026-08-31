package com.byteasy.common.interceptor;

import cn.hutool.core.map.MapUtil;
import com.byteasy.Result;
import com.byteasy.component.JsScriptFactory;
import com.byteasy.component.OpenApiFactory;
import com.byteasy.util.JsScriptExecutor;
import com.google.common.collect.Maps;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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

    /**
     * 环绕拦截 OpenapiCommonController 下的所有公开方法
     */
    @Around("execution(public * com.byteasy.common.controller.OpenapiCommonController.*(..))")
    public Object aroundOpenapiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        Object[] args = joinPoint.getArgs();

        // 1. 获取当前 HTTP 请求上下文
        HttpServletRequest request = getHttpServletRequest();

        // 2. 智能提取 className, params, body
        String className = extractClassName(signature, args);

        // 3. 查找当前操作对应的 Openapi 模型配置
        Map<String, Object> openApi = findOpenApi(className, methodName);

        // 4. 构建标准脚本入参 Context
        Map<String, Object> scriptContext = buildScriptContext(className, signature, args, request);

        // 5. 🔥【执行前置脚本钩子】
        if (openApi != null) {
            executePreScript(openApi, scriptContext);
        }

        // 6. 执行核心 Controller 业务逻辑
        Object result = joinPoint.proceed();

        // 7. 🔥【执行后置脚本钩子】
        if (openApi != null) {
            // 如果返回的是 Result 对象，解包或直接传入
            scriptContext.put("result", result instanceof Result ? ((Result<?>) result).getData() : result);
            executePostScript(openApi, scriptContext);
        }

        return result;
    }

    // =========================================================================
    // 脚本执行器与内部辅助方法
    // =========================================================================

    private void executePreScript(Map<String, Object> openapi, Map<String, Object> input) throws ExecutionException {
        Long scriptId = MapUtil.getLong(openapi, "preScriptId");
        if (scriptId == null || scriptId <= 0) return;

        Map<String, Object> jsScriptObj = jsScriptFactory.getScript(scriptId);
        String script = MapUtil.getStr(jsScriptObj, "script");
        if (StringUtils.isBlank(script)) return;

        log.debug("【AOP 触发前置脚本】apiCode={}, scriptId={}", openapi.get("code"), scriptId);
        JsScriptExecutor.runScriptWithTimeout(input, script, "PreScript-" + scriptId, 30000);
    }

    private void executePostScript(Map<String, Object> openapi, Map<String, Object> input) throws ExecutionException {
        Long scriptId = MapUtil.getLong(openapi, "postScriptId");
        if (scriptId == null || scriptId <= 0) return;

        Map<String, Object> jsScriptObj = jsScriptFactory.getScript(scriptId);
        String script = MapUtil.getStr(jsScriptObj, "script");
        if (StringUtils.isBlank(script)) return;

        log.debug("【AOP 触发后置脚本】apiCode={}, scriptId={}", openapi.get("code"), scriptId);
        JsScriptExecutor.runScriptWithTimeout(input, script, "PostScript-" + scriptId, 30000);
    }

    /**
     * 智能定位 Openapi 模型定义
     */
    private Map<String, Object> findOpenApi(String className, String type) {
        if (openApiFactory == null || StringUtils.isEmpty(className)) return null;
        try {
            return openApiFactory.getOpenApi(openApiFactory.buildCompositeKey(className, type));
        } catch (Exception e) {
            log.debug("未找到对应 openapi: className={}, type={}", className, type);
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

    /**
     * 组装标准的脚本上下文 input
     */
    private Map<String, Object> buildScriptContext(String className, MethodSignature signature, Object[] args, HttpServletRequest request) {
        Map<String, Object> input = Maps.newHashMap();
        input.put("className", className);
        input.put("headers", extractHeaders(request));

        Map<String, Object> params = Maps.newHashMap();
        Object body = null;

        String[] paramNames = signature.getParameterNames();
        for (int i = 0; i < paramNames.length; i++) {
            String pName = paramNames[i];
            Object pVal = args[i];

            if ("params".equals(pName) && pVal instanceof Map) {
                params.putAll((Map<String, Object>) pVal);
            } else if ("id".equals(pName)) {
                params.put("id", pVal);
            } else if ("entity".equals(pName) || "entities".equals(pName)) {
                body = pVal;
            }
        }

        input.put("params", params);
        input.put("body", body != null ? body : Maps.newHashMap());
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
