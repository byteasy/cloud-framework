package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.ServiceInstance;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "platform-service",
        contextId = "IServiceInstanceService",
        path = Constant.SAPI + "/serviceInstances",
        fallback = IServiceInstanceServiceFallback.class)
public interface IServiceInstanceService {

    @PostMapping("")
    Result<? extends ServiceInstance> saveOrUpdate(@RequestBody ServiceInstance serviceInstance);
}
