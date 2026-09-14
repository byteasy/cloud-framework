package com.byteasy.common.component;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.component.ICacheLoader;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IPlatformEntityChangeHandler;
import com.byteasy.platform.api.IEntityDefineService;
import com.byteasy.platform.entity.EntityDefine;
import com.byteasy.util.FieldValueUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
@Log4j2
public class EntityDefineLoadingCache implements IPlatformEntityChangeHandler, ICacheLoader<String, EntityDefine> {

    @Autowired
    private IEntityDefineService entityDefineService;

    private LoadingCache<String, EntityDefine> cache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(6)).maximumSize(5000).build(new CacheLoader<>() {
        @Override
        public @Nullable EntityDefine load(String key) {
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
            params.put("code", key);
            params.put("codeQueryType", "eq");
            Result<List<EntityDefine>> list = entityDefineService.list(params);
            List<EntityDefine> entityDefines = list.getIfPresent();
            if (entityDefines != null && !entityDefines.isEmpty()) {
                return entityDefines.getFirst();
            }
            return null;
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
        log.info("EntityDefineLoadingCache invalidate code: {}", code);
        cache.invalidate(code);
    }

    public String getEntityName(String className) {
        EntityDefine entityDefine = null;
        try {
            entityDefine = cache.get(className);
        } catch (ExecutionException e) {
            log.error(e);
        }
        if (entityDefine != null) {
            return entityDefine.getName();
        }
        return null;
    }

    public EntityDefine getEntityDefine(String className) {
        EntityDefine entityDefine = null;
        try {
            entityDefine = cache.get(className);
        } catch (ExecutionException e) {
            log.error(e);
        }
        return entityDefine;
    }

    @Override
    public EntityDefine load(String key) {
        return getEntityDefine(this.type());
    }
}
