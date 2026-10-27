package com.byteasy.common.component;

import com.byteasy.component.CacheFactory;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IEntityChangeHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class EntityCacheEvictHandler implements IEntityChangeHandler {

    @Autowired
    private CacheFactory cacheFactory;

    @Override
    public String type() {
        return "EntityCacheEvictHandler";
    }

    @Override
    public void handle(EntityChangeEvent event) {
        switch (event.getChangeType()) {
            case DELETE -> {
                for (Object beforeEntity : event.getBeforeEntities()) {
                    cacheFactory.invalid(beforeEntity);
                }
            }
            case UPDATE -> {
                for (Object afterEntity : event.getAfterEntities()) {
                    cacheFactory.invalid(afterEntity);
                }
            }
        }
    }
}
