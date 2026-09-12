package com.byteasy.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteasy.exception.BizException;
import com.byteasy.platform.entity.RoleResource;
import com.byteasy.platform.mapper.RoleResourceMapper;
import com.byteasy.platform.service.IResourceService;
import com.byteasy.platform.service.IRoleResourceService;
import com.byteasy.platform.vo.RolePermissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 平台资源角色授权表 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-04-02
 */
@Service
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements IRoleResourceService {

    @Autowired
    private IResourceService resourceService;

    @Override
    public Set<Long> selectByRoleIdAndGroupId(Long roleId, Long groupId) {
        return baseMapper.selectByRoleIdAndGroupId(roleId, groupId);
    }

    @Override
    public void removeByRoleId(Long roleId, Long groupId) {
        baseMapper.removeByRoleIdAndGroupId(roleId, groupId);
    }

    @Override
    @Transactional
    public void saveRoleResource(Long roleId, Set<Long> resourceIds) {
        List<RoleResource> roleResourceList = new ArrayList<>();
        for (Long resourceId : resourceIds) {
            RoleResource roleResource = new RoleResource();
            roleResource.setResourceId(resourceId);
            roleResource.setRoleId(roleId);
            roleResourceList.add(roleResource);
        }
        saveBatch(roleResourceList);
    }

    @Override
    public RolePermissionVO getPermission(Long groupId, Long roleId) {
        RolePermissionVO rolePermissionVO = new RolePermissionVO();
        rolePermissionVO.setLeftMenuVOS(resourceService.selectByGroupId(groupId));
        rolePermissionVO.setPermissionIds(selectByRoleIdAndGroupId(roleId, groupId));
        return rolePermissionVO;
    }

    @Override
    @Transactional
    public void updatePermission(RolePermissionVO rolePermissionVO) {
        if (rolePermissionVO.getRoleId() == null) {
            throw new BizException("角色信息缺失");
        }
        removeByRoleId(rolePermissionVO.getRoleId(), rolePermissionVO.getGroupId());
        if (rolePermissionVO.getPermissionIds() != null && !rolePermissionVO.getPermissionIds().isEmpty()) {
            saveRoleResource(rolePermissionVO.getRoleId(), rolePermissionVO.getPermissionIds());
        }
    }
}
