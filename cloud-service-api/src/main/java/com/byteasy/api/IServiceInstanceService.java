package com.byteasy.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.dto.ServiceInstanceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "platform-service",
        contextId = "IFrameworkServiceInstanceService",
        path = "/platformSvr" + Constant.SAPI + "/serviceInstances",
        fallback = IServiceInstanceServiceFallback.class)
public interface IServiceInstanceService {

    @PostMapping("")
    Result<ServiceInstanceDTO> saveOrUpdate(@RequestBody ServiceInstanceDTO serviceInstance);

    @GetMapping
    Result<Page<ServiceInstanceDTO>> page(@RequestParam("current") long current, @RequestParam("size") long size);
}
