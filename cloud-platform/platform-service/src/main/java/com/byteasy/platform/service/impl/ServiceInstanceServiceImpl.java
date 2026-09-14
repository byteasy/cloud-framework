package com.byteasy.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.byteasy.platform.entity.ServiceInstance;
import com.byteasy.platform.mapper.ServiceInstanceMapper;
import com.byteasy.platform.service.IServiceInstanceService;
import com.byteasy.service.EntityChangeService;
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
public class ServiceInstanceServiceImpl extends EntityChangeService<Long, ServiceInstanceMapper, ServiceInstance> implements IServiceInstanceService {

    @Override
    public ServiceInstance findServiceInstanceByCode(String code) {
        LambdaQueryWrapper<ServiceInstance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ServiceInstance::getCode, code);
        return getOne(queryWrapper, false);
    }
}
