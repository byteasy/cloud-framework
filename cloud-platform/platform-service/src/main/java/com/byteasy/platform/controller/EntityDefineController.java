package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.controller.AuditController;
import com.byteasy.platform.entity.EntityDefine;
import com.byteasy.platform.service.IEntityDefineService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "查询模型定义")
    public EntityDefine getEntityDefine(@PathVariable("code") String code) {
        return baseService.findByCode(code);
    }
}
