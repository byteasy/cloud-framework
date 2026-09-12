package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.platform.entity.EntityDefine;

import java.util.List;

public class IEntityDefineServiceFallback implements IEntityDefineService {

    @Override
    public Result<List<? extends EntityDefine>> saveOrUpdateBatch(List<EntityDefine> entityDefines) {
        return null;
    }

    @Override
    public EntityDefine getEntityDefine(String code) {
        return null;
    }
}
