package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.controller.ImmutablyController;
import com.byteasy.platform.entity.LoginLog;
import com.byteasy.platform.service.ILoginLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 登录日志 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-05-06
 */
@RestController
@RequestMapping(Constant.SAPI + "/loginLogs")
public class LoginLogController extends ImmutablyController<Long, ILoginLogService, LoginLog> {

}
