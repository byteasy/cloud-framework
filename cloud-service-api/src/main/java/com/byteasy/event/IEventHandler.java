package com.byteasy.event;

/**
 * 通用事件处理接口
 */
public interface IEventHandler<E extends Event> {

    void handle(E event);

    Class<E> getEventType();
}
