package com.byteasy.api;

import com.byteasy.Result;
import com.byteasy.dto.EntityDefineDTO;

import java.util.List;

public class IEntityDefineServiceFallback implements IEntityDefineService {


    @Override
    public Result<List<EntityDefineDTO>> saveOrUpdateBatch(List<EntityDefineDTO> entityDefines) {
        return null;
    }

    @Override
    public EntityDefineDTO getEntityDefine(String code) {
        return null;
    }

}
