package com.byteasy.common.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Authentication;
import com.byteasy.annotations.Log;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.component.FieldDefineUtil;
import com.byteasy.component.I18nUtil;
import com.byteasy.dto.Column;
import com.byteasy.enums.ColumnType;
import com.byteasy.enums.YesNo;
import com.byteasy.exception.BizException;
import com.byteasy.service.ICommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 内部api接口调用入口
 */
@RestController
@RequestMapping(Constant.SAPI + "/platform")
@Log4j2
public class PlatformCommonController {
    @Autowired
    private ICommonService commonService;
    @Autowired
    private FieldDefineUtil fieldDefineUtil;

    @GetMapping("/{className}")
    @Authentication
    @Operation(summary = "分页查询")
    public Result<Page<Map<String, Object>>> page(@PathVariable("className") String className) throws Exception {
        return Result.success(commonService.page(className));
    }

    @GetMapping("/{className}/all")
    @Authentication
    @Operation(summary = "查询所有")
    public Result<List<Map<String, Object>>> list(@PathVariable("className") String className) throws Exception {
        return Result.success(commonService.list(className));
    }

    @PostMapping("/{className}")
    @Authentication
    @Operation(summary = "存在更新记录，否则插入一条记录")
    @Log
    public Result<Map<String, Object>> saveOrUpdate(@PathVariable("className") String className, @RequestBody Map<String, Object> entity) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("className: {}, saveOrUpdate: {}", className, JacksonUtil.toJson(entity));
        }
        return Result.success(commonService.saveOrUpdate(className, entity));
    }

    @GetMapping("/{className}/{primaryKey}")
    @Authentication
    @Operation(summary = "根据主键查找")
    @Parameters({@Parameter(name = "id", description = "主键")})
    public Result<Map<String, Object>> getByPrimaryKey(@PathVariable("className") String className, @PathVariable("primaryKey") Object primaryKey) throws Exception {
        return Result.success(commonService.getByPrimaryKey(className, primaryKey));
    }

    @DeleteMapping("/{className}")
    @Authentication
    @Operation(summary = "根据主键删除记录")
    @Log
    public Result<Boolean> removeByEntity(@PathVariable("className") String className, @RequestBody Map<String, Object> entity) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("className: {}, removeByEntity: {}", className, JacksonUtil.toJson(entity));
        }
        return Result.success(commonService.removeByPrimaryKey(className, entity));
    }

    @DeleteMapping("/{className}/{primaryKey}")
    @Authentication
    @Operation(summary = "根据主键删除记录")
    @Log
    public Result<Boolean> removeByPrimaryKey(@PathVariable("className") String className, @PathVariable("primaryKey") Object primaryKey) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("className: {}, removeById: {}", className, primaryKey);
        }
        return Result.success(commonService.removeByPrimaryKey(className, primaryKey));
    }

    @DeleteMapping("/{className}/batch")
    @Authentication
    @Operation(summary = "批量删除")
    @Log
    public Result<Boolean> batchDelete(@PathVariable("className") String className, @RequestBody List<Map<String, Object>> entities) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("className: {}, batchRemove: {}", className, JacksonUtil.toJson(entities));
        }
        commonService.batchDelete(className, entities);
        return Result.success();
    }

    protected String valid(Map<String, Object> entity, List<Column> columns) {
        StringBuilder builder = new StringBuilder();
        for (Column column : columns) {
            Object value = entity.get(column.getName());
            if (YesNo.YES.equals(column.getIsRequire())) {
                if (ObjectUtil.isEmpty(value)) {
                    builder.append(I18nUtil.getLanguageValue(column.getDisplayName())).append(":").append(I18nUtil.getMessage("excel.import.fieldMustNotNull")).append(";");
                    continue;
                }
            }
            if (!ObjectUtil.isEmpty(value) && YesNo.YES.equals(column.getIsI18n())) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(String.valueOf(value));
                    for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                        String i18nValue = String.valueOf(entry.getValue());
                        if (StringUtils.isEmpty(i18nValue)) {
                            builder.append(I18nUtil.getLanguageValue(column.getDisplayName())).append("(").append(entry.getKey()).append(")").append(":").append(I18nUtil.getMessage("validator.error.field.minLength")).append(column.getMinLength()).append(";");
                        } else {
                            if (i18nValue.length() > column.getMaxLength()) {
                                builder.append(I18nUtil.getLanguageValue(column.getDisplayName())).append("(").append(entry.getKey()).append(")").append(":").append(I18nUtil.getMessage("validator.error.field.maxLength")).append(column.getMaxLength()).append(";");
                            }
                            if (i18nValue.length() < column.getMinLength()) {
                                builder.append(I18nUtil.getLanguageValue(column.getDisplayName())).append("(").append(entry.getKey()).append(")").append(":").append(I18nUtil.getMessage("validator.error.field.minLength")).append(column.getMinLength()).append(";");
                            }
                        }
                    }
                } catch (Exception e) {
                    builder.append(I18nUtil.getLanguageValue(column.getDisplayName())).append(":").append(I18nUtil.getMessage("validator.error.field.i18nFormat")).append(";");
                }
            }
            if (value != null && ColumnType.STRING.equals(column.getType()) && YesNo.NO.equals(column.getIsI18n())) {
                String strValue = String.valueOf(value);
                if (StringUtils.isNotEmpty(strValue)) {
                    if (strValue.length() > column.getMaxLength()) {
                        builder.append(I18nUtil.getLanguageValue(column.getDisplayName())).append(":").append(I18nUtil.getMessage("validator.error.field.maxLength")).append(column.getMaxLength()).append(";");
                    }
                    if (strValue.length() < column.getMinLength()) {
                        builder.append(I18nUtil.getLanguageValue(column.getDisplayName())).append(":").append(I18nUtil.getMessage("validator.error.field.minLength")).append(column.getMinLength()).append(";");
                    }
                }
            }
            if (value != null && (ColumnType.BYTE.equals(column.getType())
                    || ColumnType.SHORT.equals(column.getType())
                    || ColumnType.INT.equals(column.getType())
                    || ColumnType.LONG.equals(column.getType())
                    || ColumnType.FLOAT.equals(column.getType())
                    || ColumnType.DOUBLE.equals(column.getType()))
            ) {
                if (value.getClass().isPrimitive()) {
                    long no = (long) value;
                    if (no > column.getMax()) {
                        builder.append(I18nUtil.getLanguageValue(column.getDisplayName())).append(":").append(I18nUtil.getMessage("validator.error.field.max")).append(column.getMax()).append(";");
                    }
                    if (no < column.getMin()) {
                        builder.append(I18nUtil.getLanguageValue(column.getDisplayName())).append(":").append(I18nUtil.getMessage("validator.error.field.min")).append(column.getMin()).append(";");
                    }
                } else {
                    long no = Long.parseLong(String.valueOf(value));
                    if (no > column.getMax()) {
                        builder.append(I18nUtil.getLanguageValue(column.getDisplayName())).append(":").append(I18nUtil.getMessage("validator.error.field.max")).append(column.getMax()).append(";");
                    }
                    if (no < column.getMin()) {
                        builder.append(I18nUtil.getLanguageValue(column.getDisplayName())).append(":").append(I18nUtil.getMessage("validator.error.field.min")).append(column.getMin()).append(";");
                    }
                }
            }
        }
        return builder.toString();
    }

    @PostMapping("/{className}/batch")
    @Authentication
    @Operation(summary = "存在更新记录，否则插入记录（批量）")
    @Log
    public Result<List<Map<String, Object>>> saveOrUpdateBatch(@PathVariable("className") String className, @RequestBody List<Map<String, Object>> entities) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("saveOrUpdateBatch: {}", JacksonUtil.toJson(entities));
        }
        List<Column> columns = fieldDefineUtil.getColumns(className, false);
        for (Map<String, Object> entity : entities) {
            validEntity(entity, columns);
        }
        if (!commonService.saveOrUpdateBatch(className, entities)) {
            throw new BizException(I18nUtil.getMessage("saveRecordFailed"));
        }
        return Result.success(entities);
    }


    protected void validEntity(Map<String, Object> entity, List<Column> columns) throws Exception {
        String msg = valid(entity, columns);
        if (StringUtils.isNotEmpty(msg)) {
            throw new BizException(msg);
        }
    }
}
