package com.byteasy.common.component;

import com.byteasy.Result;
import com.byteasy.component.ICacheLoader;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IEntityChangeHandler;
import com.byteasy.platform.api.OpenapiClient;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.util.FieldValueUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
@Log4j2
public class OpenApiLoadingCache implements IEntityChangeHandler, ICacheLoader<String, Openapi> {

    @Autowired
    private OpenapiClient openapiService;

    private LoadingCache<String, Openapi> cache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(24)).maximumSize(10000).build(new CacheLoader<>() {
        @Override
        public Openapi load(String apiCode) {
            if (apiCode.contains("###")) {
                String[] parts = apiCode.split("###", 3);
                String actualUri = parts[0];
                String path = parts[1];
                String method = parts[2];
                return openapiService.getByUriAndMethod(actualUri, path, Openapi.HttpMethod.fromValue(method)).getIfPresent();
            }
            Map<String, Object> params;
            params = Maps.newHashMapWithExpectedSize(2);
            params.put("code", apiCode);
            params.put("codeQueryType", "eq");
            Result<List<Openapi>> result = openapiService.list(params);
            List<Openapi> openapiList = result.getIfPresent();
            if (openapiList != null && openapiList.size() == 1) {
                return openapiList.get(0);
            } else {
                return null;
            }
        }
    });

    public void register(Openapi openapi) {
        openapiService.saveOrUpdate(openapi);
    }

    @Override
    public Openapi load(String key) throws ExecutionException {
        return cache.get(key);
    }

    @Override
    public String type() {
        return Openapi.class.getName();
    }

    @Override
    public void handle(EntityChangeEvent event) {
        switch (event.getChangeType()) {
            case DELETE: {
                String code = FieldValueUtil.getFieldValue(event.getBefore(), "code", String.class);
                log.info("OpenApiFactory invalidate cache: {}", code);
                invalidateByCode(code);
                break;
            }
            case UPDATE: {
                String code = FieldValueUtil.getFieldValue(event.getAfter(), "code", String.class);
                log.info("OpenApiFactory invalidate cache: {}", code);
                invalidateByCode(code);
                break;
            }
            default:
        }
    }

    private void invalidateByCode(String code) {
        for (Map.Entry<String, Openapi> entry : cache.asMap().entrySet()) {
            if (entry.getValue().getCode().equals(code)) {
                cache.invalidate(entry.getKey());
            }
        }
    }

    public String buildCompositeKey(String actualUri, String path, Openapi.HttpMethod method) {
        return StringUtils.joinWith("###", actualUri, path, method.name());
    }
}
