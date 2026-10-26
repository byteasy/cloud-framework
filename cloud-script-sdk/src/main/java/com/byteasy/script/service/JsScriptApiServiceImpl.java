package com.byteasy.script.service;

import cn.hutool.core.map.MapUtil;
import com.byteasy.Result;
import com.byteasy.common.component.OpenApiLoadingCache;
import com.byteasy.platform.entity.JsScript;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.script.component.JsScriptFactory;
import com.byteasy.script.util.JsScriptExecutor;
import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j2;
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
    private OpenApiLoadingCache openApiLoadingCache;

    @Override
    @Transactional
    public Result runScript(String apiCode, Map<String, Object> params, Map<String, String> headers, String body) throws ExecutionException {
        log.info("runApiScript, api: {}, params: {}, headers: {}, body: {}", apiCode, params, headers, body);

        Openapi openApi = openApiLoadingCache.load(apiCode);
        if (openApi == null) {
            log.error("openApi is null, apiCode: {}", apiCode);
            return Result.error("openApi is null, apiCode: " + apiCode);
        }

        Long scriptId = openApi.getScriptId();
        if (scriptId == null) {
            log.error("scriptId is null, apiCode: {}, scriptId: {}", apiCode, scriptId);
            return Result.error("scriptId is null, apiCode: {}" + apiCode);
        }

        JsScript jsScriptObj = jsScriptFactory.load(scriptId);
        if (StringUtils.isEmpty(jsScriptObj.getScript())) {
            log.error("script is null, apiCode: {}, scriptId: {}", apiCode, scriptId);
            return Result.error("script is null, apiCode: " + apiCode);
        }

        Map<String, Object> input = Maps.newHashMap();
        input.put("header", headers);
        input.put("params", params);
        input.put("body", body);

        exePreScript(openApi, input);

        try {
            return Result.success(JsScriptExecutor.runScriptWithTimeout(input, jsScriptObj.getScript(), String.valueOf(jsScriptObj.getId()), 30000));
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

    private void exePreScript(Openapi openapi, Map<String, Object> input) throws ExecutionException {
        if (openapi.getPreScriptId() != null) {
            Long scriptId = openapi.getPreScriptId();
            JsScript jsScriptObj = jsScriptFactory.load(scriptId);
            if (StringUtils.isEmpty(jsScriptObj.getScript())) {
                return;
            }
            JsScriptExecutor.runScriptWithTimeout(input, jsScriptObj.getScript(), "PreScript-" + scriptId, 30000);
        }
    }

    private void exePostScript(Openapi openapi, Map<String, Object> input) throws ExecutionException {
        if (openapi.getPostScriptId() != null) {
            Long scriptId = openapi.getPostScriptId();
            JsScript jsScriptObj = jsScriptFactory.load(scriptId);
            if (StringUtils.isEmpty(jsScriptObj.getScript())) {
                return;
            }
            JsScriptExecutor.runScriptWithTimeout(input, jsScriptObj.getScript(), "PostScriptId-" + scriptId, 30000);
        }
    }
}
