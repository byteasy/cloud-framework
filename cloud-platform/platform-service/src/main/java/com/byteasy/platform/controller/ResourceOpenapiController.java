package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.controller.RelationController;
import com.byteasy.platform.entity.ResourceOpenapi;
import com.byteasy.platform.service.IResourceOpenapiService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * OAuth2客户端与平台资源的授权关联表 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
@RestController
@RequestMapping(Constant.SAPI + "/resourceOpenapis")
public class ResourceOpenapiController extends RelationController<IResourceOpenapiService, ResourceOpenapi> {

}
