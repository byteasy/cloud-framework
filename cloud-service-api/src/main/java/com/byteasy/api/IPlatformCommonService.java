package com.byteasy.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.Constant;
import com.byteasy.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "platform-service",
        contextId = "IFrameworkPlatformCommonService",
        path = "/platformSvr" + Constant.SAPI + "/common",
        fallback = IServiceInstanceServiceFallback.class)
public interface IPlatformCommonService {

    @GetMapping("/{className}/{id}")
    Result<Map<String, Object>> getById(@PathVariable("className") String className, @PathVariable("id") Object id);

    @GetMapping("/{className}")
    Result<Page<Map<String, Object>>> page(@PathVariable("className") String className, @RequestParam Map<String, Object> queryParams);
}
