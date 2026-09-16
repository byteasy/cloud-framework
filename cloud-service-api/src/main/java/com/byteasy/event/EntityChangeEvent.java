package com.byteasy.event;

import java.time.Clock;

public class EntityChangeEvent extends Event<EntityChangeSource> {
    public EntityChangeEvent() {
    }

    public EntityChangeEvent(EntityChangeSource source) {
        super(source, EventType.ONE_INSTANCE_PER_SERVICE_GROUP);
    }

    public EntityChangeEvent(EntityChangeSource source, Clock clock) {
        super(source, clock, EventType.ONE_INSTANCE_PER_SERVICE_GROUP);
    }
}
