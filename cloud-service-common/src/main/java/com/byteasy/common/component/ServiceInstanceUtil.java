package com.byteasy.common.component;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.common.util.CastUtil;
import com.byteasy.component.ICacheLoader;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IPlatformEntityChangeHandler;
import com.byteasy.platform.api.IServiceInstanceService;
import com.byteasy.platform.entity.ServiceInstance;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class ServiceInstanceUtil implements IPlatformEntityChangeHandler, ICacheLoader<String, ServiceInstance> {

    @Autowired
    private IServiceInstanceService serviceInstanceService;

    private Map<String, ServiceInstance> serviceInstanceCache = new HashMap<>();
    private long loadedTime = -1;

    public synchronized ServiceInstance getServiceInstance(String serviceId) {
        init();
        return serviceInstanceCache.get(serviceId);
    }

    private void init() {
        if (System.currentTimeMillis() - loadedTime > 6 * 3600 * 1000) {
            serviceInstanceCache.clear();
            Result<List<ServiceInstance>> result = serviceInstanceService.list(Map.of());
            if (result.isSuccess()) {
                serviceInstanceCache.putAll(CastUtil.listToMap(result.getData(), ServiceInstance::getCode));
            }
            loadedTime = System.currentTimeMillis();
            log.info("Load service define. size: {}", serviceInstanceCache.size());
        }
    }

    @Override
    public ServiceInstance load(String key) {
        return getServiceInstance(key);
    }

    @Override
    public String type() {
        return Constant.CLS_SERVICE_INSTANCE;
    }

    @Override
    public void handle(EntityChangeEvent event) {
        serviceInstanceCache.clear();
        loadedTime = -1;
    }
}
