package com.byteasy.common.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.SAPI + "/script/api")
@Log4j2
public class JsScriptOpenApiController {

    @PostMapping("/{apiId}")
    public Result runScriptApi(@PathVariable String apiId, @RequestBody String body) {

    }
}
