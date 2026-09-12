package com.byteasy.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byteasy.platform.entity.RoleResource;
import com.byteasy.platform.vo.RolePermissionVO;

import java.util.Set;

/**
 * <p>
 * 平台资源角色授权表 服务类
 * </p>
 *
 * @author devops
 * @since 2026-04-02
 */
public interface IRoleResourceService extends IService<RoleResource> {

    Set<Long> selectByRoleIdAndGroupId(Long roleId, Long groupId);

    void removeByRoleId(Long roleId, Long groupId);

    void saveRoleResource(Long roleId, Set<Long> resourceIds);

    /**
     * 查询某个微应用下的所有菜单及角色分配的所有菜单
     *
     * @param groupId
     * @param roleId
     * @return
     */
    RolePermissionVO getPermission(Long groupId, Long roleId);

    /**
     * 更新角色的权限信息
     *
     * @param rolePermissionVO
     */
    void updatePermission(RolePermissionVO rolePermissionVO);
}
