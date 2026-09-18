package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.common.controller.AuditController;
import com.byteasy.platform.entity.Role;
import com.byteasy.platform.service.IRoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.SAPI + "/roles")
public class RoleController extends AuditController<Long, IRoleService, Role> {

}
