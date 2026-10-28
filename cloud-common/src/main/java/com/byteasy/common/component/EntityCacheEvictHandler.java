package com.byteasy.common.component;

import com.byteasy.common.service.impl.CommonServiceImpl;
import com.byteasy.component.CacheFactory;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IEntityChangeHandler;
import com.byteasy.util.CastUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

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
    public void handle(String src, EntityChangeEvent event) {
        if (RepositoryMutationAspect.class.getName().equals(src)) {
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
        } else if (CommonServiceImpl.class.getName().equals(src)) {
            Class cls = null;
            try {
                cls = Class.forName(event.getEntityClass());
            } catch (Exception e) {
            }
            if (cls != null) {
                switch (event.getChangeType()) {
                    case DELETE -> {
                        for (Object beforeEntity : event.getBeforeEntities()) {
                            cacheFactory.invalid(CastUtil.mapToBean((Map) beforeEntity, cls));
                        }
                    }
                    case UPDATE -> {
                        for (Object afterEntity : event.getAfterEntities()) {
                            cacheFactory.invalid(CastUtil.mapToBean((Map) afterEntity, cls));
                        }
                    }
                }
            }
        }
    }
}
