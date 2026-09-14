package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.api.IBaseEntityServiceFallback;
import com.byteasy.platform.entity.Openapi;

public class IOpenapiServiceFallback extends IBaseEntityServiceFallback<Long, Openapi> implements IOpenapiService {

    @Override
    public Result<Openapi> getByUriAndMethod(String actualUri, String uri, Openapi.HttpMethod method) {
        return null;
    }
}
