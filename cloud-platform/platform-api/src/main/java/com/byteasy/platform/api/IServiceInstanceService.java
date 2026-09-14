package com.byteasy.platform.api;

import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.ServiceInstance;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = Constant.SERVICE_NAME,
        contextId = "IServiceInstanceService",
        path = Constant.SAPI + "/serviceInstances",
        fallback = IServiceInstanceServiceFallback.class)
public interface IServiceInstanceService extends IBaseEntityService<Long, ServiceInstance> {

}
