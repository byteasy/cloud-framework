package com.byteasy.common.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.service.ICommonService;
import com.byteasy.common.util.FieldDefineUtil;
import com.byteasy.platform.entity.FieldDefine;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Comparator;

@RestController
@RequestMapping(Constant.SAPI + "/fieldDefineHelper")
@Log4j2
public class FieldDefineHelperController {

    @Autowired
    private ICommonService commonService;

    @GetMapping("/src/{className}")
    @Openapi(title = "查询模型代码字段定义", uri = "/{svrName}" + Constant.SAPI + "/fieldDefineHelper/src/{className}")
    public Result<Collection<FieldDefine>> getSrcSchemaMetaInfo(@PathVariable("className") String className) throws ClassNotFoundException {
        return Result.success(FieldDefineUtil.getColumns(Class.forName(className)).values().stream().sorted(Comparator.comparingInt(FieldDefine::getShowOrder)).toList());
    }

    @GetMapping("/database/{className}")
    @Openapi(title = "查询数据库实体模型字段定义", uri = "/{svrName}" + Constant.SAPI + "/fieldDefineHelper/database/{className}")
    public Result<Collection<FieldDefine>> getDatabaseSchemaMetaInfo(@PathVariable("className") String className) {
        return Result.success(commonService.getColumns(className));
    }

    @GetMapping("/database/currentSchema/{tableName}")
    @Operation(summary = "查询当前模式下数据库实体模型字段定义")
    @Openapi(title = "查询当前模式下数据库实体模型字段定义", uri = "/{svrName}" + Constant.SAPI + "/fieldDefineHelper/database/currentSchema/{tableName}")
    public Result<Collection<FieldDefine>> getCurrentSchemaTableColumns(@PathVariable("tableName") String tableName) {
        return Result.success(commonService.getColumnsByTableName(tableName));
    }

    @PostMapping("/database/currentSchema/{className}")
    @Openapi(title = "同步平台模型至数据库物理模型", uri = "/{svrName}" + Constant.SAPI + "/fieldDefineHelper/database/currentSchema/{className}")
    public Result saveDatabaseSchemaMetaInfo(@PathVariable("className") String className) {
        commonService.syncColumnsToDb(className);
        return Result.success();
    }
}
