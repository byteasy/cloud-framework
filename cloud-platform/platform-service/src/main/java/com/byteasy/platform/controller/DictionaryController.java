package com.byteasy.platform.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Authentication;
import com.byteasy.controller.AuditController;
import com.byteasy.platform.entity.Dictionary;
import com.byteasy.platform.entity.DictionaryItem;
import com.byteasy.platform.service.IDictionaryService;
import com.byteasy.platform.vo.DictionaryVO;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "查询字典定义")
    @Authentication
    public Result<DictionaryVO> selectByCode(@PathVariable("code") String code) {
        return Result.success(baseService.selectByCode(code));
    }

    @GetMapping("/rpc/codes/{code}")
    @Operation(summary = "查询字典定义")
    @Authentication
    public DictionaryVO selectByCodeForRpc(@PathVariable("code") String code) {
        return baseService.selectByCode(code);
    }

    @GetMapping("/rpc/codes/{code}/items/{itemCode}")
    @Operation(summary = "查询字典项定义")
    @Authentication
    public DictionaryItem selectByCodeForRpc(@PathVariable("code") String code, @PathVariable("itemCode") String itemCode) {
        return baseService.selectByCodeAndItemCode(code, itemCode);
    }
}
