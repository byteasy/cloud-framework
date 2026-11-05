package com.byteasy.script.service;

import cn.hutool.core.map.MapUtil;
import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.common.component.OpenApiLoadingCache;
import com.byteasy.enums.Enable;
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
    public Result runScript(String apiCode, Map<String, Object> params, Map<String, String> headers, String body, boolean publicOnly) throws ExecutionException {
        log.info("runApiScript, api: {}, params: {}, headers: {}, body: {}", apiCode, params, headers, body);

        Openapi openApi = openApiLoadingCache.load(apiCode);

        if (publicOnly && openApi == null) {
            // 安全期间，仅返回特殊错误码
            return Result.error("接口执行失败，错误码：" + Constant.ERR_00001);
        }

        if (openApi == null) {
            log.error("openApi is null, apiCode: {}", apiCode);
            return Result.error("openApi is null, apiCode: " + apiCode);
        }

        if (!Enable.ENABLE.equals(openApi.getStatus())) {
            log.error("openApi is disable, apiCode: {}", apiCode);
            return Result.error("openApi is disable, apiCode: {}" + apiCode);
        }

        if (publicOnly) {
            if (!Openapi.PermissionType.PUBLIC.equals(openApi.getPermissionType())) {
                return Result.error("接口执行失败，错误码：" + Constant.ERR_00002);
            }
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
