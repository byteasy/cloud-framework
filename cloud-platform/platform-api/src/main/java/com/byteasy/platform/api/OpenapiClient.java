package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.Openapi;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(Constant.SAPI + "/openapis")
public interface OpenapiClient extends IBaseEntityService<Long, Openapi> {

    @GetExchange("/uri")
    Result<Openapi> getByUriAndMethod(@RequestParam("actualUri") String actualUri,
                                      @RequestParam("uri") String uri,
                                      @RequestParam("method") Openapi.HttpMethod method);

    @GetExchange("/codes/{code}")
    Result<Openapi> getByCode(@PathVariable("code") String code);
}
