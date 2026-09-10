package com.byteasy.common.controller;

import com.byteasy.Constant;
import com.byteasy.annotations.Authentication;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.dto.ScriptFunction;
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
    @Authentication(serviceOnly = true)
    public Object run(@PathVariable String name, @RequestBody String argsString) {
        Object[] args = JacksonUtil.parseObjArray(argsString);
        ScriptFunction scriptFunction = factory.getJsFunctionMap().get(name);
        if (scriptFunction == null) {
            log.error("function name: {} not found", name);
            return null;
        }
        try {
            Object result = scriptFunction.getFunction().apply(args);
            return JacksonUtil.toJson(result, true);
        } catch (Exception e) {
            log.error("function name: {} execute error", name, e);
        }
        return null;
    }
}
