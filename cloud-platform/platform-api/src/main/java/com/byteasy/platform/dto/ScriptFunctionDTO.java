package com.byteasy.platform.dto;

import com.byteasy.platform.entity.ScriptFunction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.function.Function;

@Getter
@Setter
public class ScriptFunctionDTO extends ScriptFunction {
    @Serial
    private static final long serialVersionUID = -8836636351090924792L;

    @JsonIgnore
    private Function<Object[], Object> function;
}
