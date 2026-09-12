package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.controller.AuditController;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.platform.service.IOpenapiService;
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
@RequestMapping(Constant.SAPI + "/openapis")
public class OpenapiController extends AuditController<Long, IOpenapiService, Openapi> {

}
