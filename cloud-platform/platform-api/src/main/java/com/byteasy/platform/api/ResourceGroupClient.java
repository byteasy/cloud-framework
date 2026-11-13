package com.byteasy.platform.api;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.dto.ResourceGroupDTO;
import com.byteasy.platform.dto.ScriptFunctionDTO;
import com.byteasy.platform.entity.ResourceGroup;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Collection;

@HttpExchange("/platformSvr" + Constant.SAPI + "/resourceGroups")
public interface ResourceGroupClient extends IBaseEntityService<Long, ResourceGroup> {

    @GetExchange("/entity/{className}")
    Result<ResourceGroupDTO> findGroupByClassName(@PathVariable("className") String className);
}
