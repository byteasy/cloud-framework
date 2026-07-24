package com.byteasy.component;

import cn.hutool.core.util.ReflectUtil;
import com.byteasy.Result;
import com.byteasy.api.IPlatformCommonService;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.EntityChangeSource;
import com.byteasy.event.IPlatformEntityChangeHandler;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
@Log4j2
public class JsScriptFactory implements IPlatformEntityChangeHandler {

    @Autowired
    private IPlatformCommonService platformCommonService;

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
                Object id = ReflectUtil.getFieldValue(source.getBefore(), "id");
                cache.invalidate(id);
                break;
            }
            case UPDATE: {
                Object id = ReflectUtil.getFieldValue(source.getAfter(), "id");
                cache.invalidate(id);
                break;
            }
            default:
        }
    }

}
