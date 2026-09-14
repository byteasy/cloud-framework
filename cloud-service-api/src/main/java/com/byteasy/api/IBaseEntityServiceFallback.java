package com.byteasy.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.Result;

import java.util.List;
import java.util.Map;

public abstract class IBaseEntityServiceFallback<ID, ENTITY> implements IBaseEntityService<ID, ENTITY> {

    @Override
    public Result getById(Object o) {
        return Result.error();
    }

    @Override
    public Result<Page> page(Map params) {
        return Result.error();
    }

    @Override
    public Result<List<ENTITY>> list(Map<String, Object> params) {
        return Result.error();
    }

    @Override
    public Result<ENTITY> saveOrUpdate(ENTITY entity) {
        return Result.error();
    }

    @Override
    public Result<List<ENTITY>> saveOrUpdateBatch(List<ENTITY> entities) {
        return Result.error();
    }

    @Override
    public Result<ENTITY> updateById(ENTITY entity) {
        return Result.error();
    }

    @Override
    public Result<List<ENTITY>> updateBatchById(List<ENTITY> entities) {
        return Result.error();
    }

    @Override
    public Result<Boolean> removeById(ID id) {
        return Result.error();
    }

    @Override
    public Result<Boolean> removeByWrapper(ENTITY entity) {
        return Result.error();
    }
}
