package com.byteasy.mq;

import lombok.Data;

@Data
public class Message {
    private String key;
    private String message;

    public Message() {
    }

    public Message(String key, String message) {
        this.key = key;
        this.message = message;
    }
}
