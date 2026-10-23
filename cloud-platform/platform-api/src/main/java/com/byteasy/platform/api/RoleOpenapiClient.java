package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.api.IBaseEntityService;
import com.byteasy.platform.Constant;
import com.byteasy.platform.entity.RoleOpenapi;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;
import java.util.Set;

@HttpExchange(Constant.SAPI + "/roleOpenapis")
public interface RoleOpenapiClient extends IBaseEntityService<Long, RoleOpenapi> {

    @PostExchange("/roleIds")
    Result<List<RoleOpenapi>> listOpenapiByRoleIds(@RequestBody Set<Long> roleIds);
}
