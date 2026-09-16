package com.byteasy.common.component;

import com.byteasy.Result;
import com.byteasy.component.ICacheLoader;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IEntityChangeHandler;
import com.byteasy.platform.api.IServiceInstanceService;
import com.byteasy.platform.entity.ServiceInstance;
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
public class ServiceInstanceLoadingCache implements IEntityChangeHandler, ICacheLoader<String, ServiceInstance> {

    @Autowired
    private IServiceInstanceService serviceInstanceService;

    private LoadingCache<String, ServiceInstance> cache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(6)).maximumSize(5000).build(new CacheLoader<>() {
        @Override
        public @Nullable ServiceInstance load(String key) {
            Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
            params.put("code", key);
            params.put("codeQueryType", "eq");
            Result<List<ServiceInstance>> list = serviceInstanceService.list(params);
            List<ServiceInstance> serviceInstances = list.getIfPresent();
            if (serviceInstances != null && !serviceInstances.isEmpty()) {
                return serviceInstances.getFirst();
            }
            return null;
        }
    });

    @Override
    public ServiceInstance load(String key) throws ExecutionException {
        return cache.get(key);
    }

    @Override
    public String type() {
        return ServiceInstance.class.getName();
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
        log.info("ServiceInstanceLoadingCache invalid code: {}", code);
        cache.invalidate(code);
    }
}
