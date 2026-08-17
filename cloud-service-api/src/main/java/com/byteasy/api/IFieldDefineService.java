package com.byteasy.api;

import com.byteasy.Constant;
import com.byteasy.dto.Column;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "platform-service",
        contextId = "IFrameworkFieldDefineService",
        path = "/platformSvr" + Constant.SAPI + "/fieldDefines",
        fallback = IFieldDefineServiceFallback.class)
public interface IFieldDefineService {

    @GetMapping("/entity/{className}")
    List<Column> getSchemaMetaInfo(@PathVariable("className") String className) throws Exception;
}
