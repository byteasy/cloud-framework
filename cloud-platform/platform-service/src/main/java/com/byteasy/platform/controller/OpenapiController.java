package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.controller.AuditController;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.platform.service.IOpenapiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping(Constant.SAPI + "/openapis")
public class OpenapiController extends AuditController<Long, IOpenapiService, Openapi> {

    @GetMapping("/uri")
    @com.byteasy.annotations.Openapi
    public Result<Openapi> getByUriAndMethod(@RequestParam("actualUri") String actualUri, @RequestParam("uri") String uri, @RequestParam("method") Openapi.HttpMethod method) {
        return Result.success(baseService.getByUriAndMethod(actualUri, uri, method));
    }
}
