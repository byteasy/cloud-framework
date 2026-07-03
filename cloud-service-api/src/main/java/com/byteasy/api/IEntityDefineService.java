package com.byteasy.api;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.dto.EntityDefineDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "platform-service",
        contextId = "IFrameworkEntityDefineService",
        path = "/platformSvr" + Constant.SAPI + "/entityDefines",
        fallback = IEntityDefineServiceFallback.class)
public interface IEntityDefineService {

    @PostMapping("/batch")
    Result<List<EntityDefineDTO>> saveOrUpdateBatch(@RequestBody List<EntityDefineDTO> entityDefines);

    @GetMapping("/entity/{code}")
    EntityDefineDTO getEntityDefine(@PathVariable("code") String code);
}
