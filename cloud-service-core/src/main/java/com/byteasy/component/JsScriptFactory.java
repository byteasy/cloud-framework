package com.byteasy.component;

import com.byteasy.Result;
import com.byteasy.api.IPlatformCommonService;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.EntityChangeSource;
import com.byteasy.event.IPlatformEntityChangeHandler;
import com.byteasy.util.FieldValueUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.log4j.Log4j2;
import org.graalvm.polyglot.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class JsScriptFactory implements IPlatformEntityChangeHandler {

    @Autowired
    private IPlatformCommonService platformCommonService;

    private static Cache<String, Source> SCRIPT_CACHE = CacheBuilder.newBuilder().maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build();

    private LoadingCache<Long, Map<String, Object>> cache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(24)).maximumSize(10000).build(new CacheLoader<>() {
        @Override
        public Map<String, Object> load(Long scriptId) {
            Result<Map<String, Object>> result = platformCommonService.getById("com.byteasy.platform.entity.JsScript", scriptId);
            if (result.isSuccess()) {
                return result.getData();
            } else {
                return null;
            }
        }
    });

    public Map<String, Object> getScript(Long scriptId) throws ExecutionException {
        return cache.get(scriptId);
    }

    @Override
    public String className() {
        return "com.byteasy.platform.entity.JsScript";
    }

    @Override
    public void handle(EntityChangeEvent event) {
        EntityChangeSource source = event.getSource();
        switch (source.getChangeType()) {
            case DELETE: {
                Long id = FieldValueUtil.getFieldValue(source.getBefore(), "id", Long.class);
                cache.invalidate((Long) id);
                SCRIPT_CACHE.invalidate(String.valueOf(id));
                break;
            }
            case UPDATE: {
                Long id = FieldValueUtil.getFieldValue(source.getAfter(), "id", Long.class);
                cache.invalidate((Long) id);
                SCRIPT_CACHE.invalidate(String.valueOf(id));
                break;
            }
            default:
        }
    }

    public static Source getScriptSource(String id) {
        return SCRIPT_CACHE.getIfPresent(id);
    }

    public static void setScriptSource(String id, Source source) {
        SCRIPT_CACHE.put(id, source);
    }

    public static void invalidate(String id) {
        SCRIPT_CACHE.invalidate(id);
    }
}
