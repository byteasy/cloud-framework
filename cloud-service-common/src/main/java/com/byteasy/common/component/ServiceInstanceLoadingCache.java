package com.byteasy.common.component;

import com.byteasy.Result;
import com.byteasy.component.ICacheLoader;
import com.byteasy.platform.api.ServiceInstanceClient;
import com.byteasy.platform.entity.ServiceInstance;
import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
@Log4j2
public class ServiceInstanceLoadingCache implements ICacheLoader<String, ServiceInstance> {

    @Autowired
    private ServiceInstanceClient serviceInstanceClient;


    @Override
    @Cacheable(cacheNames = "dict", key = "'ServiceInstance:' + #code")
    public ServiceInstance load(String code) throws ExecutionException {
        Map<String, Object> params = Maps.newHashMapWithExpectedSize(2);
        params.put("code", code);
        params.put("codeQueryType", "eq");
        Result<List<ServiceInstance>> list = serviceInstanceClient.list(params);
        List<ServiceInstance> serviceInstances = list.getIfPresent();
        if (serviceInstances != null && !serviceInstances.isEmpty()) {
            return serviceInstances.getFirst();
        }
        return null;
    }

    @Override
    public String type() {
        return ServiceInstance.class.getName();
    }

}
