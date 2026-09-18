package com.byteasy.platform.api;

import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.OpenapiLog;
import com.byteasy.platform.entity.Role;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = Constant.SERVICE_NAME,
        contextId = "IRoleService",
        path = Constant.SAPI + "/roles",
        fallback = IRoleServiceFallback.class)
public interface IRoleService extends IBaseEntityService<Long, Role> {

}
