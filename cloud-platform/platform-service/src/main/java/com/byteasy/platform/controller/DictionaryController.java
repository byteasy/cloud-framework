package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.controller.AuditController;
import com.byteasy.platform.entity.Dictionary;
import com.byteasy.platform.entity.DictionaryItem;
import com.byteasy.platform.service.IDictionaryService;
import com.byteasy.platform.vo.DictionaryVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 字典定义表 前端控制器
 * </p>
 *
 * @author devops
 * @since 2026-03-24
 */
@RestController
@RequestMapping(Constant.SAPI + "/dictionarys")
public class DictionaryController extends AuditController<Long, IDictionaryService, Dictionary> {

    @GetMapping("/codes/{code}")
    @Openapi(summary = "查询字典定义")
    public Result<DictionaryVO> selectByCode(@PathVariable("code") String code) {
        return Result.success(baseService.selectByCode(code));
    }

    @GetMapping("/rpc/codes/{code}/items/{itemCode}")
    @Openapi(summary = "查询字典项定义")
    public Result<DictionaryItem> selectByCodeForRpc(@PathVariable("code") String code, @PathVariable("itemCode") String itemCode) {
        return Result.success(baseService.selectByCodeAndItemCode(code, itemCode));
    }
}
