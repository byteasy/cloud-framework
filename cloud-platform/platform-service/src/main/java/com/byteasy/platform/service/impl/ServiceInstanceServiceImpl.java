package com.byteasy.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.EntityChangeSource;
import com.byteasy.platform.entity.ServiceInstance;
import com.byteasy.platform.mapper.ServiceInstanceMapper;
import com.byteasy.platform.service.IServiceInstanceService;
import com.byteasy.util.EventPublishUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 平台服务定义表 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
@Service
public class ServiceInstanceServiceImpl extends ServiceImpl<ServiceInstanceMapper, ServiceInstance> implements IServiceInstanceService {
    @Autowired
    private EventPublishUtil eventPublishUtil;

    @Override
    public boolean saveOrUpdate(ServiceInstance entity) {
        UserInfo userInfo = TokenUtil.getUserinfo();
        if (userInfo.isStartedByUser()) {
            if (entity.getId() != null) {
                eventPublishUtil.publishEvent(new EntityChangeEvent(new EntityChangeSource(EntityChangeSource.ChangeType.CREATE, getById(entity.getId()), entity, getEntityClass().getName())));
            } else {
                eventPublishUtil.publishEvent(new EntityChangeEvent(new EntityChangeSource(EntityChangeSource.ChangeType.CREATE, null, entity, getEntityClass().getName())));
            }
            return super.saveOrUpdate(entity);
        } else {
            LambdaQueryWrapper<ServiceInstance> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ServiceInstance::getCode, entity.getCode());
            ServiceInstance serviceInstance = getOne(queryWrapper);
            if (serviceInstance == null) {
                return super.saveOrUpdate(entity);
            }
            return true;
        }
    }

    @Override
    public ServiceInstance findServiceInstanceByCode(String code) {
        LambdaQueryWrapper<ServiceInstance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ServiceInstance::getCode, code);
        return getOne(queryWrapper, false);
    }
}
