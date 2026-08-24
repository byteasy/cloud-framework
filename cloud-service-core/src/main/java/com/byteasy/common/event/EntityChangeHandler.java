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

import java.util.Map;

@Component
@Log4j2
public class EntityChangeHandler implements IPlatformEventHandler<EntityChangeEvent> {
    private Map<String, IPlatformEntityChangeHandler> cache = Maps.newHashMap();

    @PostConstruct
    public void init() {
        try {
            Map<String, IPlatformEntityChangeHandler> map = SpringUtil.getBeansOfType(IPlatformEntityChangeHandler.class);
            for (IPlatformEntityChangeHandler value : map.values()) {
                log.info("register entity change handler, type: {}, handler: {}", value.className(), value.getClass().getName());
                if (value.className().contains(",")) {
                    String[] clsNames = value.className().split(",");
                    for (String clsName : clsNames) {
                        cache.put(clsName, value);
                    }
                } else {
                    cache.put(value.className(), value);
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
            cache.get(source.getClassName()).handle(event);
        }
    }

    @Override
    public Class<EntityChangeEvent> getEventType() {
        return EntityChangeEvent.class;
    }
}
