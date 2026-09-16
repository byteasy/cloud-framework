package com.byteasy.platform.api;

import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.RoleOpenapi;
import com.byteasy.platform.entity.ServiceInstance;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = Constant.SERVICE_NAME,
        contextId = "IRoleOpenapiService",
        path = Constant.SAPI + "/roleOpenapis",
        fallback = IRoleOpenapiServiceFallback.class)
public interface IRoleOpenapiService extends IBaseEntityService<Long, RoleOpenapi> {

}
