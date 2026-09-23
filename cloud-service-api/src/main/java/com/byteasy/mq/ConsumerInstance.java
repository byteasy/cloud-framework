package com.byteasy.mq;

import lombok.Data;

@Data
public class ConsumerInstance {
    private SubscriptionType subscribeType;
    private String topic;
    private String groupId;
    private String serviceName;
}
