package com.byteasy.mq;

public interface IConsumer {

    SubscriptionType subscribeType();

    String groupId();

    String topic();

    void receive(String key, String message);

}
