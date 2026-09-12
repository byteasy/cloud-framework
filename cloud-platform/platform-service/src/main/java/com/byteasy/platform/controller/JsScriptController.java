package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.controller.AuditController;
import com.byteasy.platform.entity.JsScript;
import com.byteasy.platform.service.IJsScriptService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * JS脚本 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-09-12
 */
@RestController
@RequestMapping(Constant.SAPI + "/jsScripts")
public class JsScriptController extends AuditController<Long, IJsScriptService, JsScript> {

}
