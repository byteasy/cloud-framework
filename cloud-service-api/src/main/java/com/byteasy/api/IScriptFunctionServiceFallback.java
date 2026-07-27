package com.byteasy.api;

import com.byteasy.dto.ScriptFunction;

import java.util.Collection;
import java.util.List;

public class IScriptFunctionServiceFallback implements IScriptFunctionService {


    @Override
    public Collection<ScriptFunction> register(Collection<ScriptFunction> scriptFunctions) {
        return List.of();
    }
}
