package com.byteasy.event;

import java.time.Clock;

public class EntityChangeEvent extends PlatformEvent<EntityChangeSource<?>> {
    public EntityChangeEvent() {
    }

    public EntityChangeEvent(EntityChangeSource<?> source) {
        super(source, SourceType.ONE_INSTANCE_PER_SERVICE_GROUP);
    }

    public EntityChangeEvent(EntityChangeSource<?> source, Clock clock) {
        super(source, clock, SourceType.ONE_INSTANCE_PER_SERVICE_GROUP);
    }
}
