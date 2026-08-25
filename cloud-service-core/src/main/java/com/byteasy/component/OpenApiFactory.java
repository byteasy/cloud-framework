package com.byteasy.component;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.api.IPlatformCommonService;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.EntityChangeSource;
import com.byteasy.event.IPlatformEntityChangeHandler;
import com.byteasy.util.FieldValueUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.pulsar.shade.com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class OpenApiFactory implements IPlatformEntityChangeHandler, ICacheLoader<String, Map<String, Object>> {

    @Autowired
    private IPlatformCommonService platformCommonService;

    private LoadingCache<String, Map<String, Object>> cache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(24)).maximumSize(10000).build(new CacheLoader<>() {
        @Override
        public Map<String, Object> load(String apiCode) {
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
            params.put("code", apiCode);
            params.put("codeQueryType", "eq");
            Result<List<Map<String, Object>>> result = platformCommonService.list("com.byteasy.platform.entity.Openapi", params);
            if (result.isSuccess() && !result.getData().isEmpty()) {
                return result.getData().getFirst();
            } else {
                return null;
            }
        }
    });

    public Map<String, Object> getOpenApi(String apiCode) throws ExecutionException {
        return cache.get(apiCode);
    }

    @Override
    public Map<String, Object> load(String key) throws ExecutionException {
        return getOpenApi(key);
    }

    @Override
    public String type() {
        return Constant.CLS_OPEN_API;
    }

    @Override
    public void handle(EntityChangeEvent event) {
        EntityChangeSource source = event.getSource();
        switch (source.getChangeType()) {
            case DELETE: {
                String code = FieldValueUtil.getFieldValue(source.getBefore(), "code", String.class);
                cache.invalidate(code);
                break;
            }
            case UPDATE: {
                String code = FieldValueUtil.getFieldValue(source.getAfter(), "code", String.class);
                cache.invalidate(code);
                break;
            }
            default:
        }
    }
}
