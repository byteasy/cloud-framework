package com.byteasy.dto;

import lombok.Data;

import java.util.Map;

@Data
public class JsScriptRunDTO {
    private String script;
    private Map<String, Object> input;
    private Object output;
}
