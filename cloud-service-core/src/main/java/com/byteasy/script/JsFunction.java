package com.byteasy.script;

public interface JsFunction {
    String name();

    String description();

    Object run(Object[] args) throws Exception;
}
