package com.byteasy.event;

import java.time.Clock;

public class EntityChangeEvent extends Event<EntityChangeSource> {
    public EntityChangeEvent() {
    }

    public EntityChangeEvent(EntityChangeSource source) {
        super(source, EventType.ALL);
    }

    public EntityChangeEvent(EntityChangeSource source, EventType eventType) {
        super(source, eventType);
    }

    public EntityChangeEvent(EntityChangeSource source, Clock clock) {
        super(source, clock, EventType.ALL);
    }

    public EntityChangeEvent(EntityChangeSource source, EventType eventType, Clock clock) {
        super(source, clock, eventType);
    }
}
