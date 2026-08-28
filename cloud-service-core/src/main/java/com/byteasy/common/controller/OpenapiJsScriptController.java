package com.byteasy.common.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.service.IJsScriptApiService;
import com.byteasy.util.MapUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(Constant.SAPI + "/script/api")
@Log4j2
public class OpenapiJsScriptController {

    @Autowired
    private IJsScriptApiService jsScriptApiService;

    @PostMapping("/{apiCode}")
    public Result runScriptApi(@PathVariable String apiCode, @RequestBody String body, HttpServletRequest request) throws ExecutionException {
        Map<String, String[]> params = request.getParameterMap();
        Map<String, String> headers = new HashMap<>();
        request.getHeaderNames().asIterator().forEachRemaining(key -> {
            headers.put(key, request.getHeader(key));
        });
        return jsScriptApiService.runScript(apiCode, MapUtil.convertToMap(params), headers, body);
    }

    @PostMapping("/debug/script")
    public Result debugScript(@RequestBody Map<String, Object> bodyMap, HttpServletRequest request) throws ExecutionException {
        Map<String, String[]> params = request.getParameterMap();
        Map<String, String> headers = new HashMap<>();
        request.getHeaderNames().asIterator().forEachRemaining(key -> {
            headers.put(key, request.getHeader(key));
        });
        Map<String, Object> testHeader = (Map<String, Object>) bodyMap.get("header");
        if (testHeader != null) {
            testHeader.forEach((k, v) -> {
                headers.put(k, String.valueOf(v));
            });
        }

        String body = bodyMap.get("body").toString();
        Map<String, Object> script = (Map<String, Object>) bodyMap.get("script");
        try {
            return jsScriptApiService.debugScript(script, MapUtil.convertToMap(params), headers, body);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
