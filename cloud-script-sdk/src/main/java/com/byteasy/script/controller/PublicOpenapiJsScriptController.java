package com.byteasy.script.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.script.service.IJsScriptApiService;
import com.byteasy.util.MapUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(Constant.PAPI + "/script/api")
@Log4j2
public class PublicOpenapiJsScriptController {

    @Autowired
    private IJsScriptApiService jsScriptApiService;

    @PostMapping("/{apiCode}")
    @Openapi
    public Result runScriptApi(@PathVariable String apiCode, @RequestBody String body, HttpServletRequest request) throws ExecutionException {
        Map<String, String[]> params = request.getParameterMap();
        Map<String, String> headers = new HashMap<>();
        request.getHeaderNames().asIterator().forEachRemaining(key -> {
            headers.put(key, request.getHeader(key));
        });
        return jsScriptApiService.runScript(apiCode, MapUtil.convertToMap(params), headers, body, true);
    }

}
