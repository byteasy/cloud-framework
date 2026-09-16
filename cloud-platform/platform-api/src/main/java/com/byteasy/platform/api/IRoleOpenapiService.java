package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.RoleOpenapi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@FeignClient(name = Constant.SERVICE_NAME,
        contextId = "IRoleOpenapiService",
        path = Constant.SAPI + "/roleOpenapis",
        fallback = IRoleOpenapiServiceFallback.class)
public interface IRoleOpenapiService extends IBaseEntityService<Long, RoleOpenapi> {

    @PostMapping("/roleIds")
    Result<List<RoleOpenapi>> listOpenapiByRoleIds(@RequestBody Set<Long> roleIds);
}
