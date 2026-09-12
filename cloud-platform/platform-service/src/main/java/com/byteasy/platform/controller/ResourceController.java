package com.byteasy.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Authentication;
import com.byteasy.common.util.RequestUtil;
import com.byteasy.controller.AuditController;
import com.byteasy.platform.entity.Resource;
import com.byteasy.platform.service.IResourceService;
import com.byteasy.platform.service.IUserRoleService;
import com.byteasy.platform.vo.LeftMenuVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 平台资源定义表（菜单/目录/按钮权限） 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
@RestController
@RequestMapping(Constant.SAPI + "/resources")
public class ResourceController extends AuditController<Long, IResourceService, Resource> {

    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public Result<Page<? extends Resource>> page() throws Exception {
        Long groupId = RequestUtil.getParameter("groupId", Long.class);
        List<LeftMenuVO> leftMenuVOS = baseService.selectByGroupId(groupId);
        Page<LeftMenuVO> page = new PageDTO<>();
        page.setRecords(leftMenuVOS);
        return Result.success(page);
    }

    // TODO 优化至脚本接口中
    @GetMapping("/leftMenu")
    @Operation(summary = "查询登录用户领域内的菜单")
    @Authentication
    public Result<Collection<LeftMenuVO>> selectByRouterPath(@RequestParam("fullPath") String fullPath) {
        return Result.success(userRoleService.selectByRouterPathAndLoginUser(fullPath));
    }
}
