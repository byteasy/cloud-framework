package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.controller.AuditController;
import com.byteasy.platform.entity.ServiceInstance;
import com.byteasy.platform.service.IServiceInstanceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 平台服务定义表 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
@RestController
@RequestMapping(Constant.SAPI + "/serviceInstances")
public class ServiceInstanceController extends AuditController<Long, IServiceInstanceService, ServiceInstance> {

}
