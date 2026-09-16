package com.byteasy.script.component;

import com.byteasy.component.ICacheLoader;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.EntityChangeSource;
import com.byteasy.event.IEntityChangeHandler;
import com.byteasy.platform.api.IJsScriptService;
import com.byteasy.platform.entity.JsScript;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class JsScriptFactory implements IEntityChangeHandler, ICacheLoader<Long, JsScript> {

    @Autowired
    private IJsScriptService jsScriptService;

    private static Cache<String, Source> SCRIPT_CACHE = CacheBuilder.newBuilder().maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build();

    private LoadingCache<Long, JsScript> cache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(24)).maximumSize(10000).build(new CacheLoader<>() {
        @Override
        public JsScript load(Long scriptId) {
            return jsScriptService.getById(scriptId).getIfPresent();
        }
    });

    public JsScript getScript(Long scriptId) throws ExecutionException {
        return cache.get(scriptId);
    }

    @Override
    public JsScript load(Long key) throws ExecutionException {
        return getScript(key);
    }

    @Override
    public String type() {
        return JsScript.class.getName();
    }

    @Override
    public void handle(EntityChangeEvent event) {
        EntityChangeSource source = event.getSource();
        switch (source.getChangeType()) {
            case DELETE: {
                Long id = FieldValueUtil.getFieldValue(source.getBefore(), "id", Long.class);
                log.info("JsScriptFactory cache delete id:{}", id);
                cache.invalidate((Long) id);
                SCRIPT_CACHE.invalidate(String.valueOf(id));
                break;
            }
            case UPDATE: {
                Long id = FieldValueUtil.getFieldValue(source.getAfter(), "id", Long.class);
                log.info("JsScriptFactory cache update id:{}", id);
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
