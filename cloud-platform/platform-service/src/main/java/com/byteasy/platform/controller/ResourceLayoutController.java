package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.common.controller.AuditController;
import com.byteasy.platform.entity.ResourceLayout;
import com.byteasy.platform.service.IResourceLayoutService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 页面布局 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-09-19
 */
@RestController
@RequestMapping(Constant.SAPI + "/resourceLayouts")
public class ResourceLayoutController extends AuditController<Long, IResourceLayoutService, ResourceLayout> {
}
