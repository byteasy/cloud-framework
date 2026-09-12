package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.EntityDefine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "platform-service",
        contextId = "IEntityDefineService",
        path = Constant.SAPI + "/entityDefines",
        fallback = IEntityDefineServiceFallback.class)
public interface IEntityDefineService {

    @PostMapping("/batch")
    Result<List<? extends EntityDefine>> saveOrUpdateBatch(@RequestBody List<EntityDefine> entityDefines);


    @GetMapping("/entity/{code}")
    EntityDefine getEntityDefine(@PathVariable("code") String code);
}
