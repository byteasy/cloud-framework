package com.byteasy.platform.api;

import com.byteasy.Constant;
import com.byteasy.platform.dto.ScriptFunctionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

@FeignClient(name = "platform-service",
        contextId = "IFrameworkPlatformScriptFunctionService",
        path = "/platformSvr" + Constant.SAPI + "/scriptFunctions",
        fallback = IScriptFunctionServiceFallback.class)
public interface IScriptFunctionService {

    @PostMapping("/register")
    Collection<ScriptFunctionDTO> register(@RequestBody Collection<ScriptFunctionDTO> scriptFunctions);
}
