package com.byteasy.controller;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.entity.AuditEntity;
import com.byteasy.entity.LogicDeleteEntity;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
public abstract class AuditController<ID extends java.io.Serializable, SERVICE extends IService<ENTITY>, ENTITY extends AuditEntity<ID>> extends BaseController<SERVICE, ENTITY, ID> {

    @Override
    @Transactional
    @Openapi
    public Result<Boolean> removeById(ID id) throws Exception {
        prepareLogicDelete(id);
        return super.removeById(id);
    }

    @Override
    @Transactional
    @Openapi
    public Result<Boolean> removeByIds(List<ENTITY> entities) throws Exception {
        for (ENTITY entity : entities) {
            prepareLogicDelete(entity.getId());
        }
        return super.removeByIds(entities);
    }

    @Override
    @Transactional
    @Openapi
    public Result<Boolean> removeByWrapper(ENTITY entity) throws Exception {
        prepareLogicDelete(entity.getId());
        return super.removeByWrapper(entity);
    }


    protected void prepareLogicDelete(ID id) {
        if (id == null) {
            return;
        }
        if (!LogicDeleteEntity.class.isAssignableFrom(baseService.getEntityClass())) {
            return;
        }
        ENTITY entity = ReflectUtil.newInstance(baseService.getEntityClass());
        LogicDeleteEntity<ID> logicDeleteEntity = (LogicDeleteEntity<ID>) entity;
        entity.setId(id);
        logicDeleteEntity.setDeletedTime(LocalDateTime.now());
        UserInfo userInfo = TokenUtil.getUserinfo();
        // Job更新数据，拿不到userInfo
        if (userInfo != null) {
            logicDeleteEntity.setDeletedBy(StringUtils.deleteWhitespace(userInfo.getName()));
            logicDeleteEntity.setDeleterId(userInfo.getUserId());
        }
        baseService.updateById(entity);
    }
}
