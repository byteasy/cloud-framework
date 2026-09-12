package com.byteasy.platform.oauth2.controller;

import com.byteasy.Constant;
import com.byteasy.controller.BaseController;
import com.byteasy.platform.oauth2.entity.Oauth2Authorization;
import com.byteasy.platform.oauth2.service.IOauth2AuthorizationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.SAPI + "/oauth2Authorizations")
public class Oauth2AuthorizationController extends BaseController<IOauth2AuthorizationService, Oauth2Authorization, String> {
}
