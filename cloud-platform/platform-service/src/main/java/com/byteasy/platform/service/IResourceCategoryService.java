package com.byteasy.platform.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.byteasy.dto.TreeNodeDTO;
import com.byteasy.platform.entity.ResourceCategory;
import com.byteasy.platform.vo.ResourceCategoryVO;

import java.util.List;

/**
 * <p>
 * 平台资源分类表 服务类
 * </p>
 *
 * @author devops
 * @since 2026-03-17
 */
public interface IResourceCategoryService extends IService<ResourceCategory> {

    /**
     * 构建领域树，树的一级节点为领域分类，二级为领域分组
     */
    List<ResourceCategoryVO> listTree();

    void deleteTreeNode(ResourceCategoryVO resourceCategoryTreeVO);

    List<ResourceCategoryVO> listResourceGroupTree();

    List<TreeNodeDTO> listTreeNode();
}
