package com.byteasy.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityDefineChangeSource extends EventSource {
    private String className;

    public EntityDefineChangeSource() {
    }

    public EntityDefineChangeSource(String className) {
        this.className = className;
    }
}
