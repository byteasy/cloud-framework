package com.byteasy.common.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.component.FieldDefineLoadingCache;
import com.byteasy.common.service.ICommonService;
import com.byteasy.platform.entity.FieldDefine;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(Constant.SAPI + "/fields")
@Log4j2
public class FieldController {

    @Autowired
    private FieldDefineLoadingCache fieldDefineLoadingCache;
    @Autowired
    private ICommonService commonService;

    @GetMapping("/src/{className}")
    @Operation(summary = "查询模型代码字段定义")
    @Openapi
    public Result<Collection<FieldDefine>> getSrcSchemaMetaInfo(@PathVariable("className") String className) {
        return Result.success(fieldDefineLoadingCache.getColumns(className, true));
    }

    @GetMapping("/database/{className}")
    @Operation(summary = "查询数据库实体模型字段定义")
    @Openapi
    public Result<Collection<FieldDefine>> getDatabaseSchemaMetaInfo(@PathVariable("className") String className) {
        return Result.success(commonService.getColumns(className));
    }

    @GetMapping("/database/currentSchema/{tableName}")
    @Operation(summary = "查询当前模式下数据库实体模型字段定义")
    @Openapi
    public Result<Collection<FieldDefine>> getCurrentSchemaTableColumns(@PathVariable("tableName") String tableName) {
        return Result.success(commonService.getColumnsByTableName(tableName));
    }

    @PostMapping("/database/currentSchema/{className}")
    @Operation(summary = "同步平台模型至数据库物理模型")
    @Openapi
    public Result saveDatabaseSchemaMetaInfo(@PathVariable("className") String className) {
        commonService.syncColumnsToDb(className);
        return Result.success();
    }
}
