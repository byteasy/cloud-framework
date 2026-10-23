package com.byteasy.platform.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.byteasy.platform.entity.RoleOpenapi;
import com.byteasy.platform.mapper.RoleOpenapiMapper;
import com.byteasy.platform.service.IRoleOpenapiService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色接口授权表 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-09-16
 */
@Service
public class RoleOpenapiServiceImpl extends ServiceImpl<RoleOpenapiMapper, RoleOpenapi> implements IRoleOpenapiService {

    @Override
    public List<RoleOpenapi> listOpenapiByRoleIds(Set<Long> roleIds) {
        return baseMapper.listOpenapiByRoleIds(roleIds);
    }
}
