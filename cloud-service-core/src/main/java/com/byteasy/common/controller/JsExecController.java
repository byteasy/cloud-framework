package com.byteasy.common.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.dto.JsScriptRunDTO;
import com.byteasy.util.JsScriptExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.SAPI + "/jsExec")
public class JsExecController {

    @PostMapping("/form/run")
    public Result run(@RequestBody JsScriptRunDTO runDTO) {
        Object data = JsScriptExecutor.runScriptWithTimeout("context", runDTO.getInput(), runDTO.getScript(), 3000);
        runDTO.setOutput(data);
        return Result.success(runDTO);
    }
}
