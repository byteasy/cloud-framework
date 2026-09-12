package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.platform.entity.ServiceInstance;

public class IServiceInstanceServiceFallback implements IServiceInstanceService {


    @Override
    public Result<? extends ServiceInstance> saveOrUpdate(ServiceInstance serviceInstance) {
        return null;
    }
}
