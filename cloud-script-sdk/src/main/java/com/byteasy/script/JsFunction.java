package com.byteasy.script;

public interface JsFunction {

    /**
     * 返回null代表框架层基础函数，每个服务均可发起本地调用，如果不为空，说明是领域内的服务共享的函数调用
     *
     * @return
     */
    default String serviceName() {
        return null;
    }

    String group();

    String name();

    String description();

    default String scriptType() {
        return "js";
    }

    Object run(Object[] args) throws Exception;
}
