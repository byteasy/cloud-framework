package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.EntityDefine;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(Constant.SAPI + "/entityDefines")
public interface EntityDefineClient extends IBaseEntityService<Long, EntityDefine> {

    @GetExchange("/entity/{code}")
    Result<EntityDefine> getEntityDefine(@PathVariable("code") String code);
}
