package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.controller.ImmutablyController;
import com.byteasy.platform.entity.OpenapiLog;
import com.byteasy.platform.service.IOpenapiLogService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 接口执行日志记录表 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-09-17
 */
@RestController
@RequestMapping(Constant.SAPI + "/openapiLogs")
public class OpenapiLogController extends ImmutablyController<Long, IOpenapiLogService, OpenapiLog> {

    @PostMapping("/{id}/replay")
    @Openapi(title = "重放接口调用")
    public Result replayLog(@PathVariable("id") Long id) throws Exception {
        return baseService.replay(id);
    }
}
