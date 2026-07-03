package com.byteasy.event;

import java.time.Clock;

public class EntityDefineChangeEvent extends PlatformEvent<EntityDefineChangeSource> {
    public EntityDefineChangeEvent() {
    }

    public EntityDefineChangeEvent(EntityDefineChangeSource source) {
        super(source, SourceType.ALL);
    }

    public EntityDefineChangeEvent(EntityDefineChangeSource source, Clock clock) {
        super(source, clock, SourceType.ALL);
    }
}
