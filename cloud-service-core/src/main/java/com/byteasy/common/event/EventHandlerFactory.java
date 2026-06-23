package com.byteasy.common.event;

import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.event.IPlatformEventHandler;
import com.byteasy.event.PlatformEvent;
import com.google.common.collect.Maps;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Log4j2
public class EventHandlerFactory {

    private Map<Class<?>, IPlatformEventHandler> cache = Maps.newHashMap();

    @PostConstruct
    public void init() {
        try {
            Map<String, IPlatformEventHandler> map = SpringUtil.getBeansOfType(IPlatformEventHandler.class);
            for (IPlatformEventHandler value : map.values()) {
                log.info("register event handler, type: {}, handler: {}", value.getEventType().getName(), value.getClass().getName());
                cache.put(value.getEventType(), value);
            }
        } catch (Exception e) {
            log.warn("EntityEventChangeFactory init error", e);
        }
    }

    @EventListener
    @Async
    public void handleEvent(PlatformEvent event) {
        IPlatformEventHandler handler = cache.get(event.getClass());
        if (handler != null) {
            log.info("platform event handler: {}, event: {}", handler.getClass().getName(), JacksonUtil.toJson(event));
            handler.handle(event);
        } else {
            log.info("platform event handler: null, event: {}", JacksonUtil.toJson(event));
        }
    }
}
