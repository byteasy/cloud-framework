package com.byteasy.event;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public abstract class EventSource {
    private Set<String> serviceIds;
}
