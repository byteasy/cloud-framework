package com.byteasy.platform.api;

import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.Openapi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = Constant.SERVICE_NAME,
        contextId = "IOpenapiService",
        path = Constant.SAPI + "/openapis",
        fallback = IOpenapiServiceFallback.class)
public interface IOpenapiService extends IBaseEntityService<Long, Openapi> {

}
