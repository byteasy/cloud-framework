package com.byteasy.common.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.component.SchemaDefineUtil;
import com.byteasy.dto.Column;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(Constant.SAPI + "/schemas")
@Log4j2
public class SchemaController {

    @Autowired
    private SchemaDefineUtil schemaDefineUtil;

    @GetMapping("/{className}")
    @Operation(summary = "查询业务对象的字段信息")
    public Result<Collection<Column>> getSchemaMetaInfo(@PathVariable("className") String className) {
        return Result.success(schemaDefineUtil.getColumns(className, false));
    }

    @GetMapping("/src/{className}")
    @Operation(summary = "查询代码中业务对象的字段信息")
    public Result<Collection<Column>> getSrcSchemaMetaInfo(@PathVariable("className") String className) {
        return Result.success(schemaDefineUtil.getColumns(className, true));
    }
}
