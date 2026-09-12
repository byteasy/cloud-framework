package com.byteasy.platform.api;

import com.byteasy.platform.dto.ScriptFunctionDTO;

import java.util.Collection;
import java.util.List;

public class IScriptFunctionServiceFallback implements IScriptFunctionService {


    @Override
    public Collection<ScriptFunctionDTO> register(Collection<ScriptFunctionDTO> scriptFunctions) {
        return List.of();
    }
}
