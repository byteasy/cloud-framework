package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.common.controller.AuditController;
import com.byteasy.enums.Enable;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.platform.service.IOpenapiService;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 平台接口定义 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-09-11
 */
@RestController
@RequestMapping(Constant.SAPI + "/openapis")
public class OpenapiController extends AuditController<Long, IOpenapiService, Openapi> {

    @GetMapping("/uri")
    @com.byteasy.annotations.Openapi
    public Result<Openapi> getByUriAndMethod(@RequestParam("actualUri") String actualUri, @RequestParam("uri") String uri, @RequestParam("method") Openapi.HttpMethod method) {
        return Result.success(baseService.getByUriAndMethod(actualUri, uri, method));
    }

    @GetMapping("/codes/{code}")
    @com.byteasy.annotations.Openapi
    public Result<Openapi> getByCode(@PathVariable("code") String code) {
        return Result.success(baseService.getByCode(code));
    }

    /**
     * 公网访问接口，无需登录即可访问，此接口仅能访问Openapi为public的接口，其余接口一律返回空
     *
     * @param code
     * @return
     */
    @GetMapping("/codes/noLogin/{code}")
    @com.byteasy.annotations.Openapi
    public Result<Openapi> getByCodeNoLogin(@PathVariable("code") String code) {
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
