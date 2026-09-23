package com.byteasy.common.event;

import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.Constant;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IEntityChangeHandler;
import com.byteasy.mq.IConsumer;
import com.byteasy.mq.SubscriptionType;
import com.byteasy.util.EntityCodeUtil;
import com.google.common.collect.Maps;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class EntityChangeFactory implements IConsumer {
    private Map<String, List<IEntityChangeHandler>> cache = Maps.newHashMap();

    @PostConstruct
    public void init() {
        try {
            Map<String, IEntityChangeHandler> map = SpringUtil.getBeansOfType(IEntityChangeHandler.class);
            for (IEntityChangeHandler value : map.values()) {
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

    public void handle(EntityChangeEvent event) {
        if (cache.containsKey(event.getClassName())) {
            cache.get(event.getClassName()).forEach(handler -> {
                try {
                    handler.handle(event);
                } catch (Exception e) {
                    log.error("handle entity change error", e);
                }
            });
        }
    }

    private void addToCache(String clsName, IEntityChangeHandler handler) {
        if (cache.containsKey(clsName)) {
            cache.get(clsName).add(handler);
        } else {
            List<IEntityChangeHandler> list = new ArrayList<>();
            list.add(handler);
            cache.put(clsName, list);
        }
    }

    @Override
    public SubscriptionType subscribeType() {
        return SubscriptionType.ALL;
    }

    @Override
    public String groupId() {
        return SpringUtil.getApplicationName() + "_" + EntityCodeUtil.generateCode();
    }

    @Override
    public String topic() {
        return Constant.TOPIC_PLATFORM_ENTITY_CHANGE;
    }

    @Override
    public void receive(String key, String message) {
        EntityChangeEvent event = (EntityChangeEvent) JacksonUtil.parseObj(message);
        handle(event);
    }
}
