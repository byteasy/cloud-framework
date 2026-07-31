package com.byteasy.component;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class OpenApiFactory implements IPlatformEntityChangeHandler {

    @Autowired
    private IPlatformCommonService platformCommonService;

    private LoadingCache<String, Map<String, Object>> cache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(24)).maximumSize(10000).build(new CacheLoader<>() {
        @Override
        public Map<String, Object> load(String apiCode) {
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(1);
            params.put("code", apiCode);
            Result<Page<Map<String, Object>>> result = platformCommonService.page("com.byteasy.platform.entity.Openapi", params);
            if (result.isSuccess() && result.getData() != null && result.getData().getRecords() != null && !result.getData().getRecords().isEmpty()) {
                return result.getData().getRecords().getFirst();
            } else {
                return null;
            }
        }
    });

    public Map<String, Object> getOpenApi(String apiCode) throws ExecutionException {
        return cache.get(apiCode);
    }

    @Override
    public String className() {
        return "com.byteasy.platform.entity.OpenApi";
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
