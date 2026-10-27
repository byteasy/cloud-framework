package com.byteasy.event;

public interface IEntityChangeHandler {

    String type();

    void handle(String src, EntityChangeEvent event);
}
