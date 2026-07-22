package com.byteasy.api;

import com.byteasy.Constant;
import com.byteasy.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "platform-service",
        contextId = "IFrameworkPlatformCommonService",
        path = "/platformSvr" + Constant.SAPI + "/common",
        fallback = IServiceInstanceServiceFallback.class)
public interface IPlatformCommonService {

    @GetMapping("/{className}/{id}")
    Result<Map<String, Object>> getById(@PathVariable("className") String className, @PathVariable("id") Object id);
}
