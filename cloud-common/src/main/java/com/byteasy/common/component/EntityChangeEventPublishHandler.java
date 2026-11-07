package com.byteasy.common.component;

import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IEntityChangeHandler;
import com.byteasy.platform.entity.EntityDefine;
import com.byteasy.util.JacksonUtil;
import io.vertx.core.eventbus.EventBus;
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
        return EntityChangeEventPublishHandler.class.getName();
    }

    @Override
    public void handle(String src, EntityChangeEvent event) {
        EntityDefine entityDefine = entityDefineLoadingCache.load(event.getEntityClass());
        if (entityDefine != null) {
            // 广播所有实体变更的通知
            if (entityDefine.getTrackable() != null && entityDefine.getTrackable()) {
                eventBus.publish(event.getEntityClass(), JacksonUtil.toJson(event));
            }

        } else {
            log.warn("Entity: {} is not define.", event.getEntityClass());
        }

    }

}
