package com.byteasy.component;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.Result;
import com.byteasy.api.IServiceInstanceService;
import com.byteasy.common.util.CastUtil;
import com.byteasy.dto.ServiceInstanceDTO;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IPlatformEntityChangeHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class ServiceInstanceUtil implements IPlatformEntityChangeHandler {

    @Autowired
    private IServiceInstanceService serviceInstanceService;

    private Map<String, ServiceInstanceDTO> serviceInstanceCache = new HashMap<>();
    private long loadedTime = -1;

    public synchronized ServiceInstanceDTO getServiceInstance(String serviceId) {
        init();
        return serviceInstanceCache.get(serviceId);
    }

    private void init() {
        if (System.currentTimeMillis() - loadedTime > 6 * 3600 * 1000) {
            serviceInstanceCache.clear();
            Result<Page<ServiceInstanceDTO>> page = serviceInstanceService.page(1, 10000);
            serviceInstanceCache.putAll(CastUtil.listToMap(page.getData().getRecords(), ServiceInstanceDTO::getCode));
            loadedTime = System.currentTimeMillis();
            log.info("Load service define. size: {}", serviceInstanceCache.size());
        }
    }

    @Override
    public String className() {
        return "com.byteasy.platform.entity.ServiceInstance";
    }

    @Override
    public void handle(EntityChangeEvent event) {
        serviceInstanceCache.clear();
        loadedTime = -1;
    }
}
