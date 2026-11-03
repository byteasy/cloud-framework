package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.common.controller.AuditController;
import com.byteasy.enums.Enable;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.platform.service.IOpenapiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 平台接口定义 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-09-11
 */
@RestController
@RequestMapping(Constant.PAPI + "/openapis")
public class PublicOpenapiController extends AuditController<Long, IOpenapiService, Openapi> {

    /**
     * 公网访问接口，无需登录即可访问，此接口仅能访问Openapi为public的接口，其余接口一律返回空
     *
     * @param code
     * @return
     */
    @GetMapping("/codes/{code}")
    @com.byteasy.annotations.Openapi
    public Result<Openapi> getPublicOpenapi(@PathVariable("code") String code) {
        Openapi openapi = baseService.getByCode(code);
        if (!Openapi.PermissionType.PUBLIC.equals(openapi.getPermissionType())) {
            return Result.error("api:" + code + " not exist.");
        }
        if (!Enable.ENABLE.equals(openapi.getStatus())) {
            return Result.error("api:" + code + " not exist.");
        }
        return Result.success(openapi);
    }
}
