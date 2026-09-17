package com.byteasy.platform.api;

import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.OpenapiLog;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = Constant.SERVICE_NAME,
        contextId = "IOpenapiLogService",
        path = Constant.SAPI + "/openapiLogs",
        fallback = IOpenapiLogServiceFallback.class)
public interface IOpenapiLogService extends IBaseEntityService<Long, OpenapiLog> {

}
