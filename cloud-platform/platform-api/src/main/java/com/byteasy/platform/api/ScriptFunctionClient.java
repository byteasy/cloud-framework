package com.byteasy.platform.api;

import com.byteasy.Constant;
import com.byteasy.platform.dto.ScriptFunctionDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Collection;

@HttpExchange("/platformSvr" + Constant.SAPI + "/scriptFunctions")
public interface ScriptFunctionClient {

    @PostExchange("/register")
    Collection<ScriptFunctionDTO> register(@RequestBody Collection<ScriptFunctionDTO> scriptFunctions);
}
