package com.byteasy.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteasy.common.util.CastUtil;
import com.byteasy.platform.entity.Resource;
import com.byteasy.platform.mapper.ResourceMapper;
import com.byteasy.platform.service.IResourceService;
import com.byteasy.platform.vo.LeftMenuVO;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 平台资源定义表（菜单/目录/按钮权限） 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @Override
    public Map<Long, Resource> selectByIds(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Resource::getId, ids);
        List<Resource> list = list(queryWrapper);
        return CastUtil.listToMap(list, Resource::getId);
    }

    @Override
    public List<LeftMenuVO> selectByGroupId(Long groupId) {
        LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Resource::getGroupId, groupId);
        queryWrapper.orderByAsc(Resource::getShowOrder);
        List<Resource> list = list(queryWrapper);
        return buildTree(list);
    }

    @Override
    public List<Resource> selectByRoleIdsAndGroupIds(Set<Long> roleIds, Long groupId) {
        return baseMapper.selectByRoleIds(roleIds, groupId);
    }

    @Override
    public List<Resource> selectByRoleIdsAndGroupIds(Set<Long> roleIds, Set<Long> groupIds) {
        return baseMapper.selectByRoleIdsAndGroupIds(roleIds, groupIds);
    }

    @Override
    public boolean hasCode(String code) {
        LambdaQueryWrapper<Resource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Resource::getCode, code);
        return count(queryWrapper) > 0;
    }

    protected List<LeftMenuVO> buildTree(List<Resource> list) {
        List<LeftMenuVO> leftMenuVOS = CastUtil.copyList(list, LeftMenuVO.class);

        Map<Long, LeftMenuVO> map = CastUtil.listToMap(leftMenuVOS, LeftMenuVO::getId);

        List<LeftMenuVO> resultList = new ArrayList<>();
        for (LeftMenuVO leftMenuVO : leftMenuVOS) {
            if (leftMenuVO.getParentId() == null) {
                resultList.add(leftMenuVO);
            } else {
                LeftMenuVO parent = map.get(leftMenuVO.getParentId());
                if (parent != null) {
                    if (parent.getChildrenList() == null) {
                        parent.setChildrenList(new ArrayList<>());
                        parent.getChildrenList().add(leftMenuVO);
                    } else {
                        parent.getChildrenList().add(leftMenuVO);
                    }
                }
            }
        }
        return resultList;
    }
}
