package com.byteasy.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.Constant;
import com.byteasy.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "platform-service",
        contextId = "IFrameworkPlatformCommonService",
        path = "/platformSvr" + Constant.SAPI + "/platform",
        fallback = IPlatformCommonServiceFallback.class)
public interface IPlatformCommonService {

    @GetMapping("/{className}/{key}")
    Result<Map<String, Object>> getByPrimaryKey(@PathVariable("className") String className, @PathVariable("key") Object id);

    @GetMapping("/{className}")
    Result<Page<Map<String, Object>>> page(@PathVariable("className") String className, @RequestParam Map<String, Object> queryParams);

    @GetMapping("/{className}/all")
    Result<List<Map<String, Object>>> list(@PathVariable("className") String className, @RequestParam Map<String, Object> queryParams);
}
