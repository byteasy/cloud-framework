package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.Openapi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = Constant.SERVICE_NAME,
        contextId = "IOpenapiService",
        path = Constant.SAPI + "/openapis",
        fallback = IOpenapiServiceFallback.class)
public interface IOpenapiService extends IBaseEntityService<Long, Openapi> {

    @GetMapping("/uri")
    Result<Openapi> getByUriAndMethod(@RequestParam("actualUri") String actualUri,
                                      @RequestParam("uri") String uri,
                                      @RequestParam("method") Openapi.HttpMethod method);
}
