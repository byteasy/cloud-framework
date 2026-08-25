package com.byteasy.common.event;

import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.EntityChangeSource;
import com.byteasy.event.IPlatformEntityChangeHandler;
import com.byteasy.event.IPlatformEventHandler;
import com.google.common.collect.Maps;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class EntityChangeHandler implements IPlatformEventHandler<EntityChangeEvent> {
    private Map<String, List<IPlatformEntityChangeHandler>> cache = Maps.newHashMap();

    @PostConstruct
    public void init() {
        try {
            Map<String, IPlatformEntityChangeHandler> map = SpringUtil.getBeansOfType(IPlatformEntityChangeHandler.class);
            for (IPlatformEntityChangeHandler value : map.values()) {
                log.info("register entity change handler, type: {}, handler: {}", value.type(), value.getClass().getName());
                if (value.type().contains(",")) {
                    String[] clsNames = value.type().split(",");
                    for (String clsName : clsNames) {
                        addToCache(clsName, value);
                    }
                } else {
                    addToCache(value.type(), value);
                }
            }
        } catch (Exception e) {
            log.warn("EntityChangeHandler init error", e);
        }
    }

    @Override
    public void handle(EntityChangeEvent event) {
        EntityChangeSource source = event.getSource();
        if (cache.containsKey(source.getClassName())) {
            cache.get(source.getClassName()).forEach(handler -> {
                try {
                    handler.handle(event);
                } catch (Exception e) {
                    log.error("handle entity change error", e);
                }
            });
        }
    }

    @Override
    public Class<EntityChangeEvent> getEventType() {
        return EntityChangeEvent.class;
    }

    private void addToCache(String clsName, IPlatformEntityChangeHandler handler) {
        if (cache.containsKey(clsName)) {
            cache.get(clsName).add(handler);
        } else {
            List<IPlatformEntityChangeHandler> list = new ArrayList<>();
            list.add(handler);
            cache.put(clsName, list);
        }
    }
}
