package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.FieldDefine;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(Constant.SAPI + "/fieldDefines")
public interface FieldDefineClient extends IBaseEntityService<Long, FieldDefine> {

    @GetExchange("/entity/{className}")
    Result<List<FieldDefine>> listEntityFieldDefines(@PathVariable("className") String className);
}
