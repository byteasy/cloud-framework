package com.byteasy.common.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.service.IJsScriptApiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(Constant.SAPI + "/script/api")
@Log4j2
public class JsScriptOpenApiController {

    @Autowired
    private IJsScriptApiService jsScriptApiService;

    @PostMapping("/{apiId}")
    public Result runScriptApi(@PathVariable Long apiId, @RequestBody String body, HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        Map<String, String> headers = new HashMap<>();
        request.getHeaderNames().asIterator().forEachRemaining(key -> {
            headers.put(key, request.getHeader(key));
        });
        return jsScriptApiService.runApiScript(apiId, params, headers, body);
    }
}
