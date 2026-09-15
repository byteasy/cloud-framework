package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.common.controller.AuditController;
import com.byteasy.platform.entity.ScriptFunction;
import com.byteasy.platform.service.IScriptFunctionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Constant.SAPI + "/scriptFunctions")
public class ScriptFunctionController extends AuditController<Long, IScriptFunctionService, ScriptFunction> {

    @RequestMapping("/register")
    public List<ScriptFunction> register(@RequestBody List<ScriptFunction> scriptFunctions) {
        return baseService.register(scriptFunctions);
    }
}
