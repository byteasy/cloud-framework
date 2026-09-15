package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.api.IBaseEntityServiceFallback;
import com.byteasy.platform.entity.EntityDefine;

public class IEntityDefineServiceFallback extends IBaseEntityServiceFallback<Long, EntityDefine> implements IEntityDefineService {
    @Override
    public Result<EntityDefine> getEntityDefine(String code) {
        return Result.error();
    }
}
