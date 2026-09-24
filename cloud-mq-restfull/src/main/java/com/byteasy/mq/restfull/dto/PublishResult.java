package com.byteasy.mq.restfull.dto;

import lombok.Data;

import java.util.Collection;

@Data
public class PublishResult {
    private Collection<ConsumerInstance> consumers;
    private PubCmd cmd;
}
