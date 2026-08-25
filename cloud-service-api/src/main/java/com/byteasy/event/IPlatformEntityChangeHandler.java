package com.byteasy.event;

public interface IPlatformEntityChangeHandler {

    String type();

    void handle(EntityChangeEvent event);
}
