package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.common.controller.AuditController;
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

}
