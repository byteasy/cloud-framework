package com.byteasy.platform.api;

import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.JsScript;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = Constant.SERVICE_NAME,
        contextId = "IJsScriptService",
        path = Constant.SAPI + "/jsScripts",
        fallback = IOpenapiServiceFallback.class)
public interface IJsScriptService extends IBaseEntityService<Long, JsScript> {

}
