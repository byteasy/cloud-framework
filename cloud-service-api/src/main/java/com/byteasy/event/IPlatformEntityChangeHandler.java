package com.byteasy.event;

public interface IPlatformEntityChangeHandler {

    String className();

    void handle(EntityChangeEvent event);
}
