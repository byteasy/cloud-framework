package com.byteasy.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

@Getter
@Setter
public abstract class Event<T extends EventSource> extends ApplicationEvent {
    private final EventType eventType;

    public Event() {
        super(new NullSource());
        eventType = EventType.SINGLETON;
    }

    public Event(T source, EventType eventType) {
        super(source);
        this.eventType = eventType;
    }

    public Event(T source, Clock clock, EventType eventType) {
        super(source, clock);
        this.eventType = eventType;
    }

    @Override
    public T getSource() {
        return (T) super.getSource();
    }

    public void setSource(T source) {
        this.source = source;
    }
}
