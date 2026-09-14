package com.byteasy.platform.api;

import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.EntityDefine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = Constant.SERVICE_NAME,
        contextId = "IEntityDefineService",
        path = Constant.SAPI + "/entityDefines",
        fallback = IEntityDefineServiceFallback.class)
public interface IEntityDefineService extends IBaseEntityService<Long, EntityDefine> {
}
