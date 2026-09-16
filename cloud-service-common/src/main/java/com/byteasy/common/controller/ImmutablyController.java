package com.byteasy.common.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byteasy.Result;
import com.byteasy.entity.ImmutablyEntity;
import com.byteasy.exception.BizException;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public abstract class ImmutablyController<ID extends java.io.Serializable, SERVICE extends IService<ENTITY>, ENTITY extends ImmutablyEntity<ID>> extends BaseController<SERVICE, ENTITY, ID> {

    @Override
    public Result<ENTITY> saveOrUpdate(ENTITY entity) throws Exception {
        throw new BizException(I_18_N_FORBIDDEN_OP);
    }

    @Override
    public Result<List<ENTITY>> saveOrUpdateBatch(List<ENTITY> entities) throws Exception {
        throw new BizException(I_18_N_FORBIDDEN_OP);
    }

    @Override
    public Result<ENTITY> updateById(ENTITY entity) throws Exception {
        throw new BizException(I_18_N_FORBIDDEN_OP);
    }

    @Override
    public Result<List<ENTITY>> updateBatchById(List<ENTITY> entities) throws Exception {
        throw new BizException(I_18_N_FORBIDDEN_OP);
    }

    @Override
    public Result<Boolean> removeById(ID id) throws Exception {
        throw new BizException(I_18_N_FORBIDDEN_OP);
    }

    @Override
    public Result<Boolean> removeByIds(List<ENTITY> entities) throws Exception {
        throw new BizException(I_18_N_FORBIDDEN_OP);
    }

    @Override
    public Result<Boolean> removeByWrapper(ENTITY entity) throws Exception {
        throw new BizException(I_18_N_FORBIDDEN_OP);
    }
}
