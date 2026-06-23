package com.byteasy.event;

/**
 * 通用事件处理接口
 */
public interface IPlatformEventHandler<E extends PlatformEvent> {

    void handle(E event);

    Class<E> getEventType();
}
