package com.byteasy.script.service;

import cn.hutool.core.map.MapUtil;
import com.byteasy.Result;
import com.byteasy.component.OpenApiFactory;
import com.byteasy.script.component.JsScriptFactory;
import com.byteasy.script.util.JsScriptExecutor;
import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Log4j2
public class JsScriptApiServiceImpl implements IJsScriptApiService {
    @Autowired
    private JsScriptFactory jsScriptFactory;
    @Autowired
    private OpenApiFactory openApiFactory;

    @Override
    @Transactional
    public Result runScript(String apiCode, Map<String, Object> params, Map<String, String> headers, String body) throws ExecutionException {
        log.info("runApiScript, api: {}, params: {}, headers: {}, body: {}", apiCode, params, headers, body);

        Map<String, Object> openApi = openApiFactory.getOpenApi(apiCode);
        if (openApi == null) {
            log.error("openApi is null, apiCode: {}", apiCode);
            return Result.error("openApi is null, apiCode: " + apiCode);
        }

        Long scriptId = MapUtil.getLong(openApi, "scriptId");
        if (scriptId == null) {
            log.error("scriptId is null, apiCode: {}, scriptId: {}", apiCode, scriptId);
            return Result.error("scriptId is null, apiCode: {}" + apiCode);
        }

        Map<String, Object> jsScriptObj = jsScriptFactory.getScript(scriptId);
        String script = MapUtil.getStr(jsScriptObj, "script");
        if (StringUtils.isEmpty(script)) {
            log.error("script is null, apiCode: {}, scriptId: {}", apiCode, scriptId);
            return Result.error("script is null, apiCode: " + apiCode);
        }

        Map<String, Object> input = Maps.newHashMap();
        input.put("header", headers);
        input.put("params", params);
        input.put("body", body);

        exePreScript(openApi, input);

        try {
            return Result.success(JsScriptExecutor.runScriptWithTimeout(input, script, MapUtil.getStr(jsScriptObj, "id"), 30000));
        } finally {
            exePostScript(openApi, input);
        }
    }

    @Override
    @Transactional
    public Result debugScript(Map<String, Object> jsScriptObj, Map<String, Object> params, Map<String, String> headers, String body) throws ExecutionException {
        String script = MapUtil.getStr(jsScriptObj, "script");
        if (StringUtils.isEmpty(script)) {
            log.error("script is null, scriptId: {}", script);
            return Result.error("script is null");
        }

        Map<String, Object> input = Maps.newHashMap();
        input.put("header", headers);
        input.put("params", params);
        input.put("body", body);

        String scriptId = MapUtil.getStr(jsScriptObj, "id") + "-debug";
        JsScriptFactory.invalidate(scriptId);
        return Result.success(JsScriptExecutor.runScriptWithTimeout(input, script, scriptId, 30000));
    }

    private void exePreScript(Map<String, Object> openapi, Map<String, Object> input) throws ExecutionException {
        if (openapi.containsKey("preScriptId") && ObjectUtils.isNotEmpty(openapi.get("preScriptId"))) {
            Long scriptId = MapUtil.getLong(openapi, "preScriptId");
            Map<String, Object> jsScriptObj = jsScriptFactory.getScript(scriptId);
            String script = MapUtil.getStr(jsScriptObj, "script");
            if (StringUtils.isEmpty(script)) {
                return;
            }
            JsScriptExecutor.runScriptWithTimeout(input, script, "PreScript-" + scriptId, 30000);
        }
    }

    private void exePostScript(Map<String, Object> openapi, Map<String, Object> input) throws ExecutionException {
        if (openapi.containsKey("postScriptId") && ObjectUtils.isNotEmpty(openapi.get("postScriptId"))) {
            Long scriptId = MapUtil.getLong(openapi, "postScriptId");
            Map<String, Object> jsScriptObj = jsScriptFactory.getScript(scriptId);
            String script = MapUtil.getStr(jsScriptObj, "script");
            if (StringUtils.isEmpty(script)) {
                return;
            }
            JsScriptExecutor.runScriptWithTimeout(input, script, "PostScriptId-" + scriptId, 30000);
        }
    }
}
