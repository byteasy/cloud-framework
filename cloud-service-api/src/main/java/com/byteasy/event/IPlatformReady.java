package com.byteasy.event;

public interface IPlatformReady {

    /**
     * 平台服务就绪
     */
    void platformReady();

    /**
     * 执行顺序
     *
     * @return
     */
    default int order() {
        return Integer.MAX_VALUE;
    }
}
