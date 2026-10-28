package com.byteasy.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.byteasy.platform.entity.EntityDefine;
import com.byteasy.platform.entity.ServiceInstance;
import com.byteasy.platform.mapper.EntityDefineMapper;
import com.byteasy.platform.service.IEntityDefineService;
import com.byteasy.platform.service.IServiceInstanceService;
import com.byteasy.util.CastUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 平台模型定义 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-05-21
 */
@Service
public class EntityDefineServiceImpl extends ServiceImpl<EntityDefineMapper, EntityDefine> implements IEntityDefineService {

    @Autowired
    private IServiceInstanceService serviceInstanceService;

    @Override
    @Transactional
    public boolean saveOrUpdateBatch(Collection<EntityDefine> entityList) {
        if (entityList.isEmpty()) {
            return true;
        }
        // 仅自动保存数据库中不存在的
        String serviceCode = entityList.stream().findFirst().get().getServiceCode();
        LambdaQueryWrapper<EntityDefine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EntityDefine::getServiceCode, serviceCode);
        List<EntityDefine> list = baseMapper.selectList(queryWrapper);
        Set<String> classCodes = CastUtil.listToSet(list, EntityDefine::getCode);

        List<EntityDefine> insertList = new ArrayList<>();
        for (EntityDefine entityDefine : entityList) {
            if (!classCodes.contains(entityDefine.getCode())) {
                insertList.add(entityDefine);
            }
        }

        if (!insertList.isEmpty()) {
            saveBatch(insertList);
        }
        return true;
    }

    @Override
    public EntityDefine findByCode(String code) {
        LambdaQueryWrapper<EntityDefine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EntityDefine::getCode, code);
        EntityDefine entityDefine = getOne(queryWrapper, false);
        if (entityDefine != null) {
            ServiceInstance serviceInstance = serviceInstanceService.findServiceInstanceByCode(entityDefine.getServiceCode());
            if (serviceInstance != null) {
                entityDefine.setExtendField("serviceInstance", serviceInstance);
            }
        }
        return entityDefine;
    }
}
