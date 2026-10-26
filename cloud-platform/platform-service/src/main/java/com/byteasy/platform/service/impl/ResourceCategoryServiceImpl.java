package com.byteasy.platform.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.byteasy.util.CastUtil;
import com.byteasy.dto.TreeNodeDTO;
import com.byteasy.platform.entity.ResourceCategory;
import com.byteasy.platform.entity.ResourceGroup;
import com.byteasy.platform.mapper.ResourceCategoryMapper;
import com.byteasy.platform.service.IResourceCategoryService;
import com.byteasy.platform.service.IResourceGroupService;
import com.byteasy.platform.vo.ResourceCategoryVO;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 平台资源分类表 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-03-17
 */
@Service
public class ResourceCategoryServiceImpl extends ServiceImpl<ResourceCategoryMapper, ResourceCategory> implements IResourceCategoryService {

    @Override
    public List<ResourceCategoryVO> listTree() {
        LambdaQueryWrapper<ResourceCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(ResourceCategory::getShowOrder);
        List<ResourceCategory> list = list(queryWrapper);

        List<ResourceCategoryVO> resourceCategoryTreeVOS = CastUtil.copyList(list, ResourceCategoryVO.class);

        Map<Long, ResourceCategoryVO> map = CastUtil.listToMap(resourceCategoryTreeVOS, ResourceCategoryVO::getId);

        List<ResourceCategoryVO> resultList = new ArrayList<>();
        for (ResourceCategoryVO resourceCategoryTreeVO : resourceCategoryTreeVOS) {
            if (resourceCategoryTreeVO.getParentId() != null) {
                ResourceCategoryVO parent = map.get(resourceCategoryTreeVO.getParentId());
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(resourceCategoryTreeVO);
            }
            if (resourceCategoryTreeVO.getParentId() == null) {
                resultList.add(resourceCategoryTreeVO);
            }
        }
        return resultList;
    }

    @Override
    @Transactional
    public void deleteTreeNode(ResourceCategoryVO resourceCategoryTreeVO) {
        Set<Long> ids = Sets.newHashSet(resourceCategoryTreeVO.getId());
        if (resourceCategoryTreeVO.getChildren() != null) {
            ids.addAll(resourceCategoryTreeVO.getChildren().stream().map(ResourceCategoryVO::getId).collect(Collectors.toSet()));
        }
        LambdaQueryWrapper<ResourceCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ResourceCategory::getId, ids);
        remove(queryWrapper);
        IResourceGroupService resourceGroupService = SpringUtil.getBean(IResourceGroupService.class);
        resourceGroupService.removeByCategoryIds(ids);
    }

    @Override
    public List<ResourceCategoryVO> listResourceGroupTree() {
        List<ResourceCategoryVO> tree = listTree();
        Set<Long> ids = Sets.newHashSet();

        Map<Long, ResourceCategoryVO> tmpMap = Maps.newHashMap();
        for (ResourceCategoryVO resourceCategoryTreeVO : tree) {
            ids.add(resourceCategoryTreeVO.getId());
            if (resourceCategoryTreeVO.getChildren() != null) {
                for (ResourceCategoryVO child : resourceCategoryTreeVO.getChildren()) {
                    ids.add(child.getId());
                    tmpMap.put(child.getId(), child);
                }
            }
        }

        List<ResourceGroup> resourceGroups = SpringUtil.getBean(IResourceGroupService.class).selectByCategoryId(ids);

        Map<Long, List<ResourceGroup>> map = resourceGroups.stream().collect(Collectors.groupingBy(ResourceGroup::getCategoryId));

        for (Map.Entry<Long, List<ResourceGroup>> entry : map.entrySet()) {
            ResourceCategoryVO parent = tmpMap.get(entry.getKey());
            parent.setChildren(CastUtil.copyList(entry.getValue(), ResourceCategoryVO.class));
        }
        return tree;
    }

    @Override
    public List<TreeNodeDTO> listTreeNode() {
        LambdaQueryWrapper<ResourceCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(ResourceCategory::getShowOrder);
        List<ResourceCategory> list = list(queryWrapper);

        List<TreeNodeDTO> treeNodeDTOS = CastUtil.copyList(list, TreeNodeDTO.class, node -> {
            node.setIcon("Folder");
            node.setType("Category");
        });
        Map<Object, TreeNodeDTO> map = CastUtil.listToMap(treeNodeDTOS, TreeNodeDTO::getId);

        List<TreeNodeDTO> resultList = new ArrayList<>();
        for (TreeNodeDTO resourceCategoryTreeVO : treeNodeDTOS) {
            if (resourceCategoryTreeVO.getParentId() != null) {
                TreeNodeDTO parent = map.get(resourceCategoryTreeVO.getParentId());
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(resourceCategoryTreeVO);
            }
            if (resourceCategoryTreeVO.getParentId() == null) {
                resultList.add(resourceCategoryTreeVO);
            }
        }
        return resultList;
    }
}
