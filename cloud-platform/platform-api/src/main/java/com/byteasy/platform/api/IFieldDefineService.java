package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.FieldDefine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = Constant.SERVICE_NAME,
        contextId = "IFieldDefineService",
        path = Constant.SAPI + "/fieldDefines",
        fallback = IFieldDefineServiceFallback.class)
public interface IFieldDefineService extends IBaseEntityService<Long, FieldDefine> {

    @GetMapping("/entity/{className}")
    Result<List<FieldDefine>> listEntityFieldDefines(@PathVariable("className") String className);
}
