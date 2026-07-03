package com.byteasy.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

@Getter
@Setter
public abstract class PlatformEvent<T extends EventSource> extends ApplicationEvent {
    private final SourceType sourceType;

    public PlatformEvent() {
        super(new NullSource());
        sourceType = SourceType.SINGLETON;
    }

    public PlatformEvent(T source, SourceType sourceType) {
        super(source);
        this.sourceType = sourceType;
    }

    public PlatformEvent(T source, Clock clock, SourceType sourceType) {
        super(source, clock);
        this.sourceType = sourceType;
    }

    @Override
    public T getSource() {
        return (T) super.getSource();
    }

    public void setSource(T source) {
        this.source = source;
    }
}
