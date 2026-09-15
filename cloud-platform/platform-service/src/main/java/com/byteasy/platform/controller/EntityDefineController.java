package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.controller.AuditController;
import com.byteasy.platform.entity.EntityDefine;
import com.byteasy.platform.service.IEntityDefineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 平台模型定义 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-05-21
 */
@RestController
@RequestMapping(Constant.SAPI + "/entityDefines")
public class EntityDefineController extends AuditController<Long, IEntityDefineService, EntityDefine> {

    @GetMapping("/entity/{code}")
    @Openapi(title = "查询模型定义")
    public Result<EntityDefine> getEntityDefine(@PathVariable("code") String code) {
        return Result.success(baseService.findByCode(code));
    }
}
