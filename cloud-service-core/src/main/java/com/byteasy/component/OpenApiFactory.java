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
import org.apache.commons.lang3.StringUtils;
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
            Map<String, Object> params;
            if (apiCode.contains("#")) {
                String[] parts = apiCode.split("#", 2);
                if (parts.length < 2) {
                    return null;
                }
                String className = parts[0];
                String type = parts[1];
                params = Maps.newHashMapWithExpectedSize(4);
                params.put("className", className);
                params.put("classNameQueryType", "eq");
                params.put("type", type);
                params.put("typeQueryType", "eq");
            } else {
                params = Maps.newHashMapWithExpectedSize(2);
                params.put("code", apiCode);
                params.put("codeQueryType", "eq");
            }
            Result<List<Map<String, Object>>> result = platformCommonService.list(Constant.CLS_OPEN_API, params);
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

                String className = FieldValueUtil.getFieldValue(source.getBefore(), "className", String.class);
                String type = FieldValueUtil.getFieldValue(source.getBefore(), "type", String.class);
                if (StringUtils.isNotEmpty(className) && StringUtils.isNotEmpty(type)) {
                    cache.invalidate(buildCompositeKey(className, type));
                }
                break;
            }
            case UPDATE: {
                String code = FieldValueUtil.getFieldValue(source.getAfter(), "code", String.class);
                cache.invalidate(code);

                String className = FieldValueUtil.getFieldValue(source.getAfter(), "className", String.class);
                String type = FieldValueUtil.getFieldValue(source.getAfter(), "type", String.class);
                if (StringUtils.isNotEmpty(className) && StringUtils.isNotEmpty(type)) {
                    cache.invalidate(buildCompositeKey(className, type));
                }
                break;
            }
            default:
        }
    }

    public String buildCompositeKey(String className, String type) {
        return className + "#" + type;
    }
}
