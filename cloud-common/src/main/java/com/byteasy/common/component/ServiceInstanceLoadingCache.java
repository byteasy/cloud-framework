package com.byteasy.common.component;

import com.byteasy.Result;
import com.byteasy.component.ICacheLoader;
import com.byteasy.infinispan.util.CacheEvictHelper;
import com.byteasy.platform.Constant;
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
    @Autowired
    private CacheEvictHelper cacheEvictHelper;

    @Override
    @Cacheable(cacheNames = Constant.PLATFORM_REP_CACHE, key = "'com.byteasy.platform.entity.ServiceInstance:' + #code")
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
    public void invalid(Object entity) {
        if (entity instanceof ServiceInstance) {
            cacheEvictHelper.evictAfterCommit(Constant.PLATFORM_REP_CACHE, ServiceInstance.class.getName() + ":" + ((ServiceInstance) entity).getCode());
        }
    }

    @Override
    public String[] invalidType() {
        return new String[]{ServiceInstance.class.getName()};
    }

    @Override
    public String cacheType() {
        return ServiceInstance.class.getName();
    }

}
