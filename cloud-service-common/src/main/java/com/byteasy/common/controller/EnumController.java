package com.byteasy.common.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.component.FieldDefineLoadingCache;
import com.byteasy.dto.Column;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constant.SAPI + "/enums")
@Log4j2
public class EnumController {
    @Autowired
    private FieldDefineLoadingCache fieldDefineLoadingCache;

    @GetMapping("/{enumClassName}")
    @Operation(summary = "查询枚举对象的定义")
    @Openapi
    public Result<List<Map<String, String>>> getEnumByClassName(@PathVariable("enumClassName") String enumClassName) {
        try {
            List<Column> columns = fieldDefineLoadingCache.getColumns(enumClassName, false);
            List<Map<String, String>> result = Lists.newArrayListWithCapacity(columns.size());

            for (Column column : columns) {
                Map<String, String> map = Maps.newHashMapWithExpectedSize(4);
                map.put("label", column.getDisplayName());
                map.put("value", column.getName());
                map.put("css", column.getTypeClassName());
                result.add(map);
            }
            return Result.success(result);
        } catch (Exception e) {
            log.error("getEnumByClassName: " + enumClassName, e);
            return Result.error(e.getLocalizedMessage());
        }
    }
}
