package com.byteasy.api;

import com.byteasy.Result;

import java.util.Map;

public class IPlatformCommonServiceFallback implements IPlatformCommonService {

    @Override
    public Result<Map<String, Object>> getById(String className, Object id) {
        return null;
    }
}
