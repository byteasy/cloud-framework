package com.byteasy.platform.api;

import com.byteasy.Result;
import com.byteasy.api.IBaseEntityServiceFallback;
import com.byteasy.platform.entity.FieldDefine;

import java.util.List;

public class IFieldDefineServiceFallback extends IBaseEntityServiceFallback<Long, FieldDefine> implements IFieldDefineService {
    @Override
    public Result<List<FieldDefine>> listEntityFieldDefines(String className) {
        return Result.error();
    }
}
