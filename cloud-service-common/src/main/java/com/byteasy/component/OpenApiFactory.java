package com.byteasy.component;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.EntityChangeSource;
import com.byteasy.event.IPlatformEntityChangeHandler;
import com.byteasy.platform.api.IOpenapiService;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.util.FieldValueUtil;
import com.byteasy.util.RequestMappingResolver;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.pulsar.shade.com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
@Log4j2
public class OpenApiFactory implements IPlatformEntityChangeHandler, ICacheLoader<String, Openapi> {

    @Autowired
    private IOpenapiService openapiService;

    private LoadingCache<String, Openapi> cache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(24)).maximumSize(10000).build(new CacheLoader<>() {
        @Override
        public Openapi load(String apiCode) {
            Map<String, Object> params;
            if (apiCode.contains("###")) {
                String[] parts = apiCode.split("###", 2);
                if (parts.length < 2) {
                    return null;
                }
                String uri = parts[0];
                String[] segments = RequestMappingResolver.splitByPathVariable(uri);
                Openapi.HttpMethod method = Openapi.HttpMethod.fromValue(parts[1]);
                params = Maps.newHashMapWithExpectedSize(4);
                params.put("uri", segments);
                params.put("uriQueryType", "like");
                params.put("httpMethod", method);
                params.put("httpMethodQueryType", "eq");
            } else {
                params = Maps.newHashMapWithExpectedSize(2);
                params.put("code", apiCode);
                params.put("codeQueryType", "eq");
            }
            Result<List<Openapi>> result = openapiService.list(params);
            List<Openapi> openapiList = result.getIfPresent();
            if (openapiList != null && openapiList.size() == 1) {
                return openapiList.get(0);
            } else {
                return null;
            }
        }
    });

    public Openapi getOpenApi(String apiCode) throws ExecutionException {
        return cache.get(apiCode);
    }

    @Override
    public Openapi load(String key) throws ExecutionException {
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
                log.info("OpenApiFactory invalidate cache: {}", code);

                String uri = FieldValueUtil.getFieldValue(source.getBefore(), "uri", String.class);
                String httpMethod = FieldValueUtil.getFieldValue(source.getBefore(), "httpMethod", String.class);
                if (StringUtils.isNotEmpty(uri) && StringUtils.isNotEmpty(httpMethod)) {
                    cache.invalidate(buildCompositeKey(uri, Openapi.HttpMethod.fromValue(httpMethod)));
                    log.info("OpenApiFactory invalidate uri: {} httpMethod: {}", uri, httpMethod);
                }
                break;
            }
            case UPDATE: {
                String code = FieldValueUtil.getFieldValue(source.getAfter(), "code", String.class);
                cache.invalidate(code);
                log.info("OpenApiFactory invalidate cache: {}", code);

                String uri = FieldValueUtil.getFieldValue(source.getAfter(), "uri", String.class);
                String httpMethod = FieldValueUtil.getFieldValue(source.getAfter(), "httpMethod", String.class);
                if (StringUtils.isNotEmpty(uri) && StringUtils.isNotEmpty(httpMethod)) {
                    cache.invalidate(buildCompositeKey(uri, Openapi.HttpMethod.fromValue(httpMethod)));
                    log.info("OpenApiFactory invalidate uri: {} httpMethod: {}", uri, httpMethod);
                }
                break;
            }
            default:
        }
    }

    public String buildCompositeKey(String path, Openapi.HttpMethod method) {
        return StringUtils.joinWith("###", path, method.name());
    }
}
