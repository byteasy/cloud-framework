package com.byteasy.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteasy.common.util.CastUtil;
import com.byteasy.platform.entity.ResourceGroup;
import com.byteasy.platform.mapper.ResourceGroupMapper;
import com.byteasy.platform.service.IResourceGroupService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 平台资源分组定义表（用于对资源进行分类管理，如：系统管理、业务管理、报表管理等） 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
@Service
public class ResourceGroupServiceImpl extends ServiceImpl<ResourceGroupMapper, ResourceGroup> implements IResourceGroupService {

    @Override
    public Map<Long, ResourceGroup> selectByIds(Set<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<ResourceGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ResourceGroup::getId, ids);
        List<ResourceGroup> list = list(queryWrapper);
        return CastUtil.listToMap(list, ResourceGroup::getId);
    }

    @Override
    public List<ResourceGroup> selectByRoleIds(Set<Long> roleIds) {
        return baseMapper.selectByRoleIds(roleIds);
    }

    @Override
    public void removeByCategoryIds(Set<Long> ids) {
        LambdaQueryWrapper<ResourceGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ResourceGroup::getCategoryId, ids);
        remove(queryWrapper);
    }

    @Override
    public List<ResourceGroup> selectByCategoryId(Set<Long> categoryIds) {
        if (categoryIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<ResourceGroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ResourceGroup::getCategoryId, categoryIds);
        return list(queryWrapper);
    }
}
