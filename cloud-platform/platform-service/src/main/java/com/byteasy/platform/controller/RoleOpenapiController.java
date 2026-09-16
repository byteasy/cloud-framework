package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.common.controller.ImmutablyController;
import com.byteasy.platform.entity.RoleOpenapi;
import com.byteasy.platform.service.IRoleOpenapiService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
