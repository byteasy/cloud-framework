package com.byteasy.component;

import com.byteasy.Constant;
import com.byteasy.api.IEntityDefineService;
import com.byteasy.dto.EntityDefineDTO;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IPlatformEntityChangeHandler;
import com.byteasy.util.FieldValueUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

@Component
@Log4j2
public class EntityDefineUtil implements IPlatformEntityChangeHandler, ICacheLoader<String, EntityDefineDTO> {
    @Autowired
    private IEntityDefineService entityDefineService;

    private LoadingCache<String, EntityDefineDTO> entityDefineCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(6)).maximumSize(5000).build(new CacheLoader<>() {
        @Override
        public EntityDefineDTO load(String key) {
            return entityDefineService.getEntityDefine(key);
        }
    });

    @Override
    public String type() {
        return Constant.CLS_ENTITY_DEFINE;
    }

    @Override
    public void handle(EntityChangeEvent event) {
        String code = null;
        if (event.getSource().getBefore() != null) {
            code = FieldValueUtil.getFieldValue(event.getSource().getBefore(), "code", String.class);
        }
        if (event.getSource().getAfter() != null) {
            code = FieldValueUtil.getFieldValue(event.getSource().getAfter(), "code", String.class);
        }
        entityDefineCacheLoader.invalidate(code);
    }

    public String getEntityName(String className) {
        EntityDefineDTO entityDefine = null;
        try {
            entityDefine = entityDefineCacheLoader.get(className);
        } catch (ExecutionException e) {
            log.error(e);
        }
        if (entityDefine != null) {
            return entityDefine.getName();
        }
        return null;
    }

    public EntityDefineDTO getEntityDefine(String className) {
        EntityDefineDTO entityDefine = null;
        try {
            entityDefine = entityDefineCacheLoader.get(className);
        } catch (ExecutionException e) {
            log.error(e);
        }
        return entityDefine;
    }

    @Override
    public EntityDefineDTO load(String key) {
        return getEntityDefine(this.type());
    }
}
