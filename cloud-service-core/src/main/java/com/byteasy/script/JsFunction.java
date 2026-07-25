package com.byteasy.script;

public interface JsFunction {
    String group();

    String name();

    String description();

    Object run(Object[] args) throws Exception;
}
