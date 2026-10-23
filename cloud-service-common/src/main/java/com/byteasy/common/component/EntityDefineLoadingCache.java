package com.byteasy.common.component;

import com.byteasy.component.ICacheLoader;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IEntityChangeHandler;
import com.byteasy.platform.api.EntityDefineClient;
import com.byteasy.platform.entity.EntityDefine;
import com.byteasy.util.FieldValueUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Log4j2
public class EntityDefineLoadingCache implements IEntityChangeHandler, ICacheLoader<String, EntityDefine> {

    @Autowired
    private EntityDefineClient entityDefineService;

    private LoadingCache<String, EntityDefine> cache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(6)).maximumSize(5000).build(new CacheLoader<>() {
        @Override
        public EntityDefine load(String key) {
            return entityDefineService.getEntityDefine(key).getIfPresent();
        }
    });

    @Override
    public String type() {
        return EntityDefine.class.getName();
    }

    @Override
    public void handle(EntityChangeEvent event) {
        String code = null;
        if (event.getBefore() != null) {
            code = FieldValueUtil.getFieldValue(event.getBefore(), "code", String.class);
        }
        if (event.getAfter() != null) {
            code = FieldValueUtil.getFieldValue(event.getAfter(), "code", String.class);
        }
        log.info("EntityDefineLoadingCache invalidate code: {}", code);
        cache.invalidate(code);
    }

    public String getEntityName(String className) {
        EntityDefine entityDefine = load(className);
        if (entityDefine != null) {
            return entityDefine.getName();
        }
        return null;
    }

    @Override
    public EntityDefine load(String key) {
        return cache.getUnchecked(key);
    }
}
