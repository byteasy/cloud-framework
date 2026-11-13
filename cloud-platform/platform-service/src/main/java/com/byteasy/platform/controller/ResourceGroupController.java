package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.controller.AuditController;
import com.byteasy.platform.dto.ResourceGroupDTO;
import com.byteasy.platform.entity.ResourceGroup;
import com.byteasy.platform.service.IResourceGroupService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 平台资源分组定义表（用于对资源进行分类管理，如：系统管理、业务管理、报表管理等） 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
@RestController
@RequestMapping(Constant.SAPI + "/resourceGroups")
public class ResourceGroupController extends AuditController<Long, IResourceGroupService, ResourceGroup> {

    @GetMapping("/entity/{className}")
    @Openapi
    public Result<ResourceGroupDTO> findGroupByClassName(@PathVariable("className") String className) {
        return Result.success(baseService.findByClassName(className));
    }
}
