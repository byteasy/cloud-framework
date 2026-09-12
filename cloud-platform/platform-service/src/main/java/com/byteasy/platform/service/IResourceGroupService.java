package com.byteasy.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byteasy.platform.entity.ResourceGroup;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 平台资源分组定义表（用于对资源进行分类管理，如：系统管理、业务管理、报表管理等） 服务类
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
public interface IResourceGroupService extends IService<ResourceGroup> {

    Map<Long, ResourceGroup> selectByIds(Set<Long> ids);

    List<ResourceGroup> selectByRoleIds(Set<Long> roleIds);

    void removeByCategoryIds(Set<Long> ids);

    List<ResourceGroup> selectByCategoryId(Set<Long> categoryIds);
}
