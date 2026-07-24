package com.byteasy.service.impl;

import cn.hutool.core.map.MapUtil;
import com.byteasy.Result;
import com.byteasy.component.JsScriptFactory;
import com.byteasy.component.OpenApiFactory;
import com.byteasy.service.IJsScriptApiService;
import com.byteasy.util.JsScriptExecutor;
import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Result runApiScript(String apiCode, Map<String, String[]> params, Map<String, String> headers, String body) throws ExecutionException {
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

        return Result.success(JsScriptExecutor.runScriptWithTimeout(input, script, 30000));
    }
}
