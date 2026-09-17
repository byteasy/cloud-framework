package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.controller.ImmutablyController;
import com.byteasy.platform.entity.OpenapiLog;
import com.byteasy.platform.service.IOpenapiLogService;
import com.byteasy.platform.service.impl.OpenapiReplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private OpenapiReplayService openapiReplayService;

    @PostMapping("/{id}/replay")
    @Openapi(title = "重放接口调用")
    public Result<String> replayLog(@PathVariable("id") Long id, @RequestParam(value = "targetHost", required = false) String targetHost) throws Exception {
        return Result.success(openapiReplayService.replay(id, targetHost));
    }
}
