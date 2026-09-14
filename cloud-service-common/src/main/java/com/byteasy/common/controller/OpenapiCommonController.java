package com.byteasy.common.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.Status;
import com.byteasy.annotations.Openapi;
import com.byteasy.common.util.CastUtil;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.common.component.EntityDefineFactory;
import com.byteasy.component.FieldDefineUtil;
import com.byteasy.component.I18nUtil;
import com.byteasy.dto.Column;
import com.byteasy.enums.ColumnType;
import com.byteasy.enums.RelationType;
import com.byteasy.enums.YesNo;
import com.byteasy.exception.BizException;
import com.byteasy.common.service.ICommonService;
import com.byteasy.util.XlsxUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 对外公开的api入口
 */
@RestController
@RequestMapping(Constant.SAPI + "/common")
@Log4j2
public class OpenapiCommonController {
    @Autowired
    private ICommonService commonService;
    @Autowired
    private FieldDefineUtil fieldDefineUtil;
    @Autowired
    private EntityDefineFactory entityDefineFactory;

    @GetMapping("/{className}")
    @Openapi
    @Operation(summary = "分页查询")
    public Result<Page<Map<String, Object>>> page(@PathVariable("className") String className) throws Exception {
        return Result.success(commonService.page(className));
    }

    @GetMapping("/{className}/all")
    @Openapi
    @Operation(summary = "查询所有")
    public Result<List<Map<String, Object>>> list(@PathVariable("className") String className) throws Exception {
        return Result.success(commonService.list(className));
    }

    @PostMapping("/{className}")
    @Openapi
    @Operation(summary = "存在更新记录，否则插入一条记录")
    public Result<Map<String, Object>> saveOrUpdate(@PathVariable("className") String className, @RequestBody String body) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("className: {}, saveOrUpdate: {}", className, body);
        }
        return Result.success(commonService.saveOrUpdate(className, JacksonUtil.nativeRead(body, new TypeReference<Map<String, Object>>() {
        })));
    }

    @GetMapping("/{className}/{primaryKey}")
    @Openapi
    @Operation(summary = "根据主键查找")
    @Parameters({@Parameter(name = "id", description = "主键")})
    public Result<Map<String, Object>> getByPrimaryKey(@PathVariable("className") String className, @PathVariable("primaryKey") Object primaryKey) throws Exception {
        return Result.success(commonService.getByPrimaryKey(className, primaryKey));
    }

    @DeleteMapping("/{className}")
    @Openapi
    @Operation(summary = "根据主键删除记录")
    public Result<Boolean> removeByEntity(@PathVariable("className") String className, @RequestBody String body) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("className: {}, removeByEntity: {}", className, body);
        }
        return Result.success(commonService.removeByPrimaryKey(className, JacksonUtil.nativeRead(body, new TypeReference<Map<String, Object>>() {
        })));
    }

    @DeleteMapping("/{className}/{primaryKey}")
    @Openapi
    @Operation(summary = "根据主键删除记录")
    public Result<Boolean> removeByPrimaryKey(@PathVariable("className") String className, @PathVariable("primaryKey") Object primaryKey) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("className: {}, removeById: {}", className, primaryKey);
        }
        return Result.success(commonService.removeByPrimaryKey(className, primaryKey));
    }

    @DeleteMapping("/{className}/batch")
    @Openapi
    @Operation(summary = "批量删除")
    public Result<Boolean> batchDelete(@PathVariable("className") String className, @RequestBody String body) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("className: {}, batchRemove: {}", className, body);
        }
        commonService.batchDelete(className, JacksonUtil.nativeRead(body, new TypeReference<List<Map<String, Object>>>() {
        }));
        return Result.success();
    }

    @GetMapping("/{className}/excel/template")
    @Openapi
    @Operation(summary = "下载Excel导入模板")
    public void downloadImportTemplate(@PathVariable("className") String className, HttpServletResponse response) throws Exception {
        List<Column> columns = fieldDefineUtil.getColumns(className, false);

        columns = columns.stream().filter(s -> YesNo.YES.equals(s.getIsForm())).toList();

        List<String> fieldNameRow = new ArrayList<>(columns.stream().filter(s -> !RelationType.DETAIL_TABLE.equals(s.getRelationType())).map(Column::getName).toList());
        fieldNameRow.add(I18nUtil.getMessage("excel.import.template.fieldNameWarning"));

        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 一次性写出内容
        int colIdx = 0;
        for (Column column : columns) {
            if (RelationType.DETAIL_TABLE.equals(column.getRelationType())) {
                continue;
            }
            String label = I18nUtil.getLanguageValue(column.getDisplayName());
            if (YesNo.YES.equals(column.getIsRequire())) {
                label = label + "*";
            }
            writer.setColumnWidth(colIdx, 20);
            if (ColumnType.LOCAL_DATE.equals(column.getType())) {
                label = label + "(" + I18nUtil.getMessage("excel.import.template.localDateFormMustBe") + "2026-03-01)";
                writer.setColumnWidth(colIdx, 40);
            } else if (ColumnType.LOCAL_DATE_TIME.equals(column.getType())) {
                label = label + "(" + I18nUtil.getMessage("excel.import.template.localDateFormMustBe") + "2026-03-01 12:32:00";
                writer.setColumnWidth(colIdx, 50);
            }
            if (YesNo.YES.equals(column.getIsRequire())) {
                writer.writeCellValue(colIdx, 0, label);
                XlsxUtil.setFontColor(writer, colIdx, 0, Font.COLOR_RED, false);
            } else {
                writer.writeCellValue(colIdx, 0, label);
                XlsxUtil.setFontColor(writer, colIdx, 0, Font.COLOR_NORMAL, false);
            }
            XlsxUtil.setBorder(writer, colIdx++, 0);
        }
        writer.writeCellValue(colIdx, 0, I18nUtil.getMessage("excel.import.template.headerWarning"));

        writer.setCurrentRow(1);
        writer.writeRow(fieldNameRow);

        XlsxUtil.setFontColor(writer, colIdx, 0, Font.COLOR_RED, true);
        XlsxUtil.setFontColor(writer, colIdx, 1, Font.COLOR_RED, true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        String encodeFileName = I18nUtil.getLanguageValue(entityDefineFactory.getEntityName(className)) + I18nUtil.getMessage("excel.import.template") + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(encodeFileName, StandardCharsets.UTF_8));

        writer.flush(response.getOutputStream());
        writer.close();
        response.getOutputStream().flush();
    }

    @PostMapping("/{className}/excel/importPreview")
    @Openapi
    @Operation(summary = "Excel导入预览")
    public Result<List<Map<String, Object>>> importPreview(@PathVariable("className") String className, @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        ExcelReader excelReader = ExcelUtil.getReader(file.getInputStream(), 0);
        int rowCount = excelReader.getRowCount();
        if (rowCount < 3) {
            return Result.success(Collections.emptyList());
        }

        List<Column> columns = fieldDefineUtil.getColumns(className, false);
        columns = columns.stream().filter(s -> YesNo.YES.equals(s.getIsForm())).toList();

        boolean success = true;

        List<Map<String, Object>> list = excelReader.read(1, 2, excelReader.getRowCount() - 1);
        I18nUtil.encodeToI18nFormat(list, columns);
        List<Map<String, Object>> excelRowPreviews = new ArrayList<>();

        StringBuilder errMessage = new StringBuilder();
        for (Map<String, Object> entity : list) {
            String message = valid(entity, columns);
            if (StringUtils.isNotEmpty(message)) {
                entity.put("validateResult", message);
                errMessage.append(message);
                success = false;
            }
            excelRowPreviews.add(entity);
        }

        String err = validBatch(list, excelRowPreviews);
        if (StringUtils.isNotEmpty(err)) {
            success = false;
        }

        if (success) {
            return Result.success(excelRowPreviews);
        } else {
            if (err == null) {
                err = errMessage.toString();
            }
            return Result.of(Status.ARGS_VALIDATE_FAILED, err, null, excelRowPreviews);
        }
    }

    /**
     * 对上传的记录做一些唯一性、重复性校验
     *
     * @param entities
     * @param excelRowPreviews
     */
    protected String validBatch(List<Map<String, Object>> entities, List<Map<String, Object>> excelRowPreviews) {
        return null;
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
    @Openapi
    @Operation(summary = "存在更新记录，否则插入记录（批量）")
    public Result<List<Map<String, Object>>> saveOrUpdateBatch(@PathVariable("className") String className, @RequestBody String body) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("saveOrUpdateBatch: {}", body);
        }
        List<Map<String, Object>> entities = JacksonUtil.nativeRead(body, new TypeReference<List<Map<String, Object>>>() {
        });
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

    @PostMapping("/{className}/excel/export")
    @Openapi
    @Operation(summary = "Excel数据导出")
    public void exportExcel(@PathVariable("className") String className, @RequestBody List<String> fieldNames, HttpServletResponse response) throws Exception {
        List<Column> columns = fieldDefineUtil.getColumns(className, false);
        Map<String, Column> columnMap = CastUtil.listToMap(columns, Column::getName);

        List<String> displayNameRow = Lists.newArrayList();
        for (String fieldName : fieldNames) {
            displayNameRow.add(I18nUtil.getLanguageValue(columnMap.get(fieldName).getDisplayName()));
        }

        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.setCurrentRow(0);
        writer.writeRow(displayNameRow);
        writer.setCurrentRow(1);
        writer.writeRow(fieldNames);

        CellStyle dateStyle = createDateFormatStyle(writer, "yyyy-mm-dd");
        CellStyle dateTimeStyle = createDateFormatStyle(writer, "yyyy-mm-dd HH:mm:ss");

        List<Map<String, Object>> list = commonService.list(className);
        int curRow = 2;
        for (Map<String, Object> entity : list) {
            int col = 0;
            for (String fieldName : fieldNames) {
                Column column = columnMap.get(fieldName);
                Object value = entity.get(fieldName);

                if (ColumnType.LOCAL_DATE.equals(column.getType())) {
                    writer.setStyle(dateStyle, col, curRow);
                } else if (ColumnType.LOCAL_DATE_TIME.equals(column.getType())) {
                    writer.setStyle(dateTimeStyle, col, curRow);
                } else if (ColumnType.BYTE.equals(column.getType())
                        || ColumnType.SHORT.equals(column.getType())
                        || ColumnType.INT.equals(column.getType())
                        || ColumnType.LONG.equals(column.getType())
                        || ColumnType.FLOAT.equals(column.getType())
                        || ColumnType.DOUBLE.equals(column.getType())) {
                    writer.setStyle(createNumberFormatStyle(writer, column.getPrecision()), col, curRow);
                }
                if (YesNo.YES.equals(column.getIsI18n())) {
                    writer.writeCellValue(col, curRow, I18nUtil.getLanguageValue(String.valueOf(value)));
                } else {
                    writer.writeCellValue(col, curRow, value);
                }
                col++;
            }
            curRow++;
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        String encodeFileName = I18nUtil.getLanguageValue(I18nUtil.getLanguageValue(entityDefineFactory.getEntityName(className))) + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(encodeFileName, StandardCharsets.UTF_8));

        writer.flush(response.getOutputStream());
        writer.close();
        response.getOutputStream().flush();
    }

    /**
     * Excel日期时间格式
     *
     * @param writer
     * @param format
     * @return
     */
    protected static CellStyle createDateFormatStyle(ExcelWriter writer, String format) {
        Workbook workbook = writer.getWorkbook();
        CellStyle style = workbook.createCellStyle();
        DataFormat dataFormat = workbook.createDataFormat();
        style.setDataFormat(dataFormat.getFormat(format));
        return style;
    }

    /**
     * 浮点数显示格式
     *
     * @param writer
     * @param decimalPlaces
     * @return
     */
    protected static CellStyle createNumberFormatStyle(ExcelWriter writer, int decimalPlaces) {
        Workbook workbook = writer.getWorkbook();
        CellStyle style = workbook.createCellStyle();
        DataFormat dataFormat = workbook.createDataFormat();

        // 拼接格式：0.00 表示两位小数，0.0 一位，0 整数
        StringBuilder format = new StringBuilder("0");
        if (decimalPlaces > 0) {
            format.append(".");
            format.append("0".repeat(decimalPlaces));
        }
        style.setDataFormat(dataFormat.getFormat(format.toString()));
        return style;
    }
}
