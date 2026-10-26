package com.byteasy.common.component;

import com.byteasy.Result;
import com.byteasy.component.ICacheLoader;
import com.byteasy.platform.api.OpenapiClient;
import com.byteasy.platform.entity.Openapi;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Log4j2
public class OpenApiLoadingCache implements ICacheLoader<String, Openapi> {

    @Autowired
    private OpenapiClient openapiService;

    public void register(Openapi openapi) {
        openapiService.saveOrUpdate(openapi);
    }

    @Override
    @Cacheable(cacheNames = "dict", key = "'Openapi:' + #apiCode")
    public Openapi load(String apiCode) throws ExecutionException {
        if (apiCode.contains("###")) {
            String[] parts = apiCode.split("###", 3);
            String actualUri = parts[0];
            String path = parts[1];
            String method = parts[2];
            return openapiService.getByUriAndMethod(actualUri, path, Openapi.HttpMethod.fromValue(method)).getIfPresent();
        }
        Result<Openapi> result = openapiService.getByCode(apiCode);
        return result.getIfPresent();
    }

    @Override
    public String type() {
        return Openapi.class.getName();
    }

    public String buildCompositeKey(String actualUri, String path, Openapi.HttpMethod method) {
        return StringUtils.joinWith("###", actualUri, path, method.name());
    }
}
