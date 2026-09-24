package com.byteasy.mq;

public interface IProducer {

    void publish(String topic, String key, String message);

    void publishMessage(String topic, String key, Object message);
}
