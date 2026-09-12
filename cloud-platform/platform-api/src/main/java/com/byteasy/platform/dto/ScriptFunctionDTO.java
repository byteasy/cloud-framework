package com.byteasy.platform.dto;

import com.byteasy.platform.entity.ScriptFunction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

@Getter
@Setter
public class ScriptFunctionDTO extends ScriptFunction {

    @JsonIgnore
    private Function<Object[], Object> function;
}
