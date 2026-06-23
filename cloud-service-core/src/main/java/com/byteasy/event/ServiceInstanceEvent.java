package com.byteasy.event;

import java.time.Clock;

public class ServiceInstanceEvent extends PlatformEvent<ServiceInstanceSource> {
    public ServiceInstanceEvent() {
    }

    public ServiceInstanceEvent(ServiceInstanceSource source) {
        super(source, SourceType.ALL);
    }

    public ServiceInstanceEvent(ServiceInstanceSource source, Clock clock) {
        super(source, clock, SourceType.ALL);
    }
}
