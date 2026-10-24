package com.byteasy.script.controller;

import com.byteasy.Constant;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.util.JacksonUtil2;
import com.byteasy.platform.dto.ScriptFunctionDTO;
import com.byteasy.script.JsFunctionFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constant.SAPI + "/scriptFunctionRunner")
@Log4j2
public class ScriptFunctionRunnerController {

    @Autowired
    private JsFunctionFactory factory;

    @PostMapping("/run/{name}")
    @Openapi
    public Object run(@PathVariable String name, @RequestBody String argsString) {
        Object[] args = JacksonUtil2.parseObjArray(argsString);
        ScriptFunctionDTO scriptFunction = factory.getJsFunctionMap().get(name);
        if (scriptFunction == null) {
            log.error("function name: {} not found", name);
            return null;
        }
        try {
            Object result = scriptFunction.getFunction().apply(args);
            return JacksonUtil2.toJson(result, true);
        } catch (Exception e) {
            log.error("function name: {} execute error", name, e);
        }
        return null;
    }
}
