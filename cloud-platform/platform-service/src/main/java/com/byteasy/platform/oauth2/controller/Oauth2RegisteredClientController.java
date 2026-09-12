package com.byteasy.platform.oauth2.controller;

import com.byteasy.Constant;
import com.byteasy.controller.AuditController;
import com.byteasy.platform.oauth2.entity.Oauth2RegisteredClient;
import com.byteasy.platform.oauth2.service.IOauth2RegisteredClientService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 平台角色定义表 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-04-02
 */
@RestController
@RequestMapping(Constant.SAPI + "/oauth2RegisteredClients")
public class Oauth2RegisteredClientController extends AuditController<String, IOauth2RegisteredClientService, Oauth2RegisteredClient> {
}
