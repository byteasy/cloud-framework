package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.api.IBaseEntityServiceFallback;
import com.byteasy.platform.entity.RoleOpenapi;

import java.util.List;
import java.util.Set;

public class IRoleOpenapiServiceFallback extends IBaseEntityServiceFallback<Long, RoleOpenapi> implements IRoleOpenapiService {


    @Override
    public Result<List<RoleOpenapi>> listOpenapiByRoleIds(Set<Long> roleIds) {
        return Result.error();
    }

}
