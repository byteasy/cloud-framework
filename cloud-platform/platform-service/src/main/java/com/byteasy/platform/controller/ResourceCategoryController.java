package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Authentication;
import com.byteasy.controller.AuditController;
import com.byteasy.enums.YesNo;
import com.byteasy.platform.entity.ResourceCategory;
import com.byteasy.platform.entity.ResourceGroup;
import com.byteasy.platform.service.IResourceCategoryService;
import com.byteasy.platform.service.IUserRoleService;
import com.byteasy.platform.vo.ResourceCategoryVO;
import com.byteasy.platform.vo.TopMenuVO;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 平台资源分类表 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-03-17
 */
@RestController
@RequestMapping(Constant.SAPI + "/resourceCategorys")
public class ResourceCategoryController extends AuditController<Long, IResourceCategoryService, ResourceCategory> {

    @Autowired
    private IUserRoleService userRoleService;

    @GetMapping("/topMenu")
    @Operation(summary = "根据登录用户查询用户拥有的业务领域一级、二级、三级菜单")
    @Authentication
    public Result<TopMenuVO> selectResourceCategory() {
        TopMenuVO topMenuVO = new TopMenuVO();
        topMenuVO.setResourceCategoryVOS(userRoleService.selectCategoryByLoginUser());

        List<ResourceGroup> quickGroups = Lists.newArrayList();
        for (ResourceCategoryVO resourceCategoryVO : topMenuVO.getResourceCategoryVOS()) {
            for (ResourceCategoryVO child : resourceCategoryVO.getChildren()) {
                for (ResourceGroup resourceGroup : child.getResourceGroups()) {
                    if (YesNo.YES.equals(resourceGroup.getNavbarMenu())) {
                        quickGroups.add(resourceGroup);
                    }
                }
            }
        }
        topMenuVO.setResourceGroups(quickGroups);
        return Result.success(topMenuVO);
    }

    @GetMapping("/tree")
    @Operation(summary = "查询领域分类及领域分组，构建二级目录")
    @Authentication
    public Result<List<ResourceCategoryVO>> listTree() {
        return Result.success(baseService.listTree());
    }

    @DeleteMapping("/treeNode")
    @Operation(summary = "删除树节点及子节点及关联的ResourceGroup")
    @Authentication
    public Result deleteTreeNode(@RequestBody ResourceCategoryVO resourceCategoryTreeVO) {
        baseService.deleteTreeNode(resourceCategoryTreeVO);
        return Result.success();
    }

    @GetMapping("/resourceGroupTree")
    @Operation(summary = "查询所有的业务领域一级、二级、三级菜单")
    @Authentication
    public Result<List<ResourceCategoryVO>> listResourceGroupTree() {
        return Result.success(baseService.listResourceGroupTree());
    }
}
