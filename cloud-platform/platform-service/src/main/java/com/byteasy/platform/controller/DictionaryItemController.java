package com.byteasy.platform.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import com.byteasy.Constant;
import com.byteasy.platform.entity.DictionaryItem;
import com.byteasy.platform.service.IDictionaryItemService;
import org.springframework.web.bind.annotation.RestController;
import com.byteasy.controller.AuditController;

/**
 * <p>
 * 字典项目定义表 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-03-24
 */
@RestController
@RequestMapping(Constant.SAPI + "/dictionaryItems")
public class DictionaryItemController extends AuditController<Long, IDictionaryItemService, DictionaryItem> {

}
