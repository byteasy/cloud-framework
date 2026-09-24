package com.byteasy.mq.restfull.dto;

import com.byteasy.mq.SubscriptionType;
import lombok.Data;

@Data
public class ConsumerInstance {
    private SubscriptionType subscribeType;
    private String topic;
    private String groupId;
    private String serviceName;
}
