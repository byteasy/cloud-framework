package com.byteasy.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceInstanceSource extends EventSource {
    private String serviceId;

    public ServiceInstanceSource() {
    }

    public ServiceInstanceSource(String serviceId) {
        this.serviceId = serviceId;
    }
}