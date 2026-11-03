package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.controller.ImmutablyController;
import com.byteasy.platform.entity.Role;
import com.byteasy.platform.entity.RoleOpenapi;
import com.byteasy.platform.service.IRoleOpenapiService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/openapis/{openapiCode}")
    @Openapi(title = "查询接口已分配角色")
    public Result<List<Role>> listRolesByOpenapi(@PathVariable("openapiCode") String openapiCode) {
        return Result.success(baseService.listRolesByOpenapi(openapiCode));
    }
}
