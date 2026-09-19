package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.common.controller.AuditController;
import com.byteasy.platform.entity.StdLayoutParams;
import com.byteasy.platform.service.IStdLayoutParamsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 标准布局参数 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-09-19
 */
@RestController
@RequestMapping(Constant.SAPI + "/stdLayoutParamss")
public class StdLayoutParamsController extends AuditController<Long, IStdLayoutParamsService, StdLayoutParams> {
}
