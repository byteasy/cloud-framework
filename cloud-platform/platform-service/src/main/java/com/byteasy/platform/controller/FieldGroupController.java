package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.common.controller.AuditController;
import com.byteasy.platform.entity.FieldGroup;
import com.byteasy.platform.service.IFieldGroupService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 字段分组 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-09-15
 */
@RestController
@RequestMapping(Constant.SAPI + "/fieldGroups")
public class FieldGroupController extends AuditController<Long, IFieldGroupService, FieldGroup> {
}
