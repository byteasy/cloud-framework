package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.EntityDefine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = Constant.SERVICE_NAME,
        contextId = "IEntityDefineService",
        path = Constant.SAPI + "/entityDefines",
        fallback = IEntityDefineServiceFallback.class)
public interface IEntityDefineService extends IBaseEntityService<Long, EntityDefine> {

    @GetMapping("/entity/{code}")
    Result<EntityDefine> getEntityDefine(@PathVariable("code") String code);
}
