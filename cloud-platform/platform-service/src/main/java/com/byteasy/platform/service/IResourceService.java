package com.byteasy.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byteasy.platform.entity.Resource;
import com.byteasy.platform.vo.LeftMenuVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 平台资源定义表（菜单/目录/按钮权限） 服务类
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
public interface IResourceService extends IService<Resource> {

    Map<Long, Resource> selectByIds(Collection<Long> ids);

    /**
     * 返回某个微应用下的所有菜单
     *
     * @param groupId
     * @return
     */
    List<LeftMenuVO> selectByGroupId(Long groupId);

    List<Resource> selectByRoleIdsAndGroupIds(Set<Long> roleIds, Long groupId);

    /**
     * 获取每个ResourceGroup下按照显示顺序的第一条Resource，用于自动设置ResourceGroup的RouterUrl，这样ResourceGroup不用再定义Router地址，自动根据菜单定义获取Router
     *
     * @param roleIds
     * @param groupIds
     * @return
     */
    List<Resource> selectByRoleIdsAndGroupIds(Set<Long> roleIds, Set<Long> groupIds);

    boolean hasCode(String code);
}
