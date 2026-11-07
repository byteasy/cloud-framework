package com.byteasy.common.component;

import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IEntityChangeHandler;
import com.byteasy.platform.entity.EntityDefine;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.util.JacksonUtil;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class EntityChangeEventPublishHandler implements IEntityChangeHandler {

    @Autowired
    private EventBus eventBus;
    @Autowired
    private EntityDefineLoadingCache entityDefineLoadingCache;

    @Override
    public String type() {
        return "EntityChangeEventPublishHandler";
    }

    @Override
    public void handle(String src, EntityChangeEvent event) {
        EntityDefine entityDefine = entityDefineLoadingCache.load(event.getEntityClass());
        if (entityDefine != null) {
            if (entityDefine.getTrackable() != null && entityDefine.getTrackable()) {
                eventBus.publish(event.getEntityClass(), JacksonUtil.toJson(event), new DeliveryOptions().setLocalOnly(false));
            }
        } else {
            log.warn("Entity: {} is not define.", event.getEntityClass());
        }
    }

    @PostConstruct
    public void init() {
        eventBus.consumer(Openapi.class.getName(), message -> {
            log.info("name: {} consumer: {}", SpringUtil.getApplicationName(), message.body());
        });
    }

}
