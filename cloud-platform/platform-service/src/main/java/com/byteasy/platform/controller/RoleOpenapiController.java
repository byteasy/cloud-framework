package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.controller.ImmutablyController;
import com.byteasy.platform.entity.RoleOpenapi;
import com.byteasy.platform.service.IRoleOpenapiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色接口授权表 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-09-16
 */
@RestController
@RequestMapping(Constant.SAPI + "/roleOpenapis")
public class RoleOpenapiController extends ImmutablyController<Long, IRoleOpenapiService, RoleOpenapi> {

    @PostMapping("/roleIds")
    @Openapi(title = "查询角色的接口权限")
    public Result<List<RoleOpenapi>> listOpenapiByRoleIds(@RequestBody Set<Long> roleIds) {
        return Result.success(baseService.listOpenapiByRoleIds(roleIds));
    }
}
