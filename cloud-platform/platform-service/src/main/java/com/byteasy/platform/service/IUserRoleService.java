package com.byteasy.platform.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.byteasy.platform.dto.RoleUserDTO;
import com.byteasy.platform.dto.UserRoleDTO;
import com.byteasy.platform.entity.UserRole;
import com.byteasy.platform.vo.LeftMenuVO;
import com.byteasy.platform.vo.ResourceCategoryVO;
import com.byteasy.platform.vo.UserRoleVO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 平台角色授权表 服务类
 * </p>
 *
 * @author devops
 * @since 2026-04-02
 */
public interface IUserRoleService extends IService<UserRole> {

    UserRoleDTO getUserRoleInfo();

    RoleUserDTO getUsersByRoleId(Long roleId);

    void saveUserRole(UserRoleVO userRoleVO);

    Set<Long> getUserRoleIds(Long userId);

    void removeByUserId(Long userId);

    void saveUserRole(Long userId, Set<Long> roleIds);


    /**
     * 前端触发，需要根据当前路由找到具体的微应用，并结合用户所拥有的权限来返回左侧菜单的可见视图
     *
     * @param routerPath
     * @return
     */
    Collection<LeftMenuVO> selectByRouterPathAndLoginUser(String routerPath);

    /**
     * 获取当前登录用户拥有的顶部菜单访问权限
     * @return
     */
    List<ResourceCategoryVO> selectCategoryByLoginUser();
}
