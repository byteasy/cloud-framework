package com.byteasy.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.Status;
import com.byteasy.annotations.Authentication;
import com.byteasy.annotations.Log;
import com.byteasy.common.util.CastUtil;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.common.util.RequestUtil;
import com.byteasy.component.EntityDefineUtil;
import com.byteasy.component.EntityServiceFactory;
import com.byteasy.component.FieldDefineUtil;
import com.byteasy.component.I18nUtil;
import com.byteasy.dto.Column;
import com.byteasy.enums.ColumnType;
import com.byteasy.enums.RelationType;
import com.byteasy.enums.YesNo;
import com.byteasy.exception.BizException;
import com.byteasy.util.XlsxUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Log4j2
public abstract class BaseController<SERVICE extends IService<ENTITY>, ENTITY, ID extends Serializable> extends AbstractController<SERVICE, ENTITY, ID> {

    @Autowired
    protected FieldDefineUtil fieldDefineUtil;
    @Autowired
    private EntityDefineUtil entityDefineUtil;
    @Autowired
    private EntityServiceFactory entityServiceFactory;

    @GetMapping("")
    @Authentication
    @Operation(summary = "分页查询")
    public Result<Page<? extends ENTITY>> page() throws Exception {
        Page<? extends ENTITY> page = baseService.page(RequestUtil.getPage(), RequestUtil.getQueryWrapper(baseService.getEntityClass()));

        /**
         * 将外键关联的对象塞入拓展字段中
         */
        List<Column> columns = fieldDefineUtil.getColumns(getEntityClass().getName(), false);
        Set<Column> externalKeys = columns.stream().filter(f -> f.getExternalClass() != null && Boolean.TRUE.equals(f.getExternalClassAutoFill())).collect(Collectors.toSet());
        if (!externalKeys.isEmpty()) {
            entityServiceFactory.updateExternalEntity(externalKeys, page.getRecords());
        }
        return Result.success(page);
    }

    @GetMapping("/list/match")
    @Authentication
    @Operation(summary = "匹配查询")
    public Result<List<? extends ENTITY>> list() throws Exception {
        List<? extends ENTITY> list = baseService.list(RequestUtil.getQueryWrapper(baseService.getEntityClass()));

        /**
         * 将外键关联的对象塞入拓展字段中
         */
        List<Column> columns = fieldDefineUtil.getColumns(getEntityClass().getName(), false);
        Set<Column> externalKeys = columns.stream().filter(f -> f.getExternalClass() != null && Boolean.TRUE.equals(f.getExternalClassAutoFill())).collect(Collectors.toSet());
        if (!externalKeys.isEmpty()) {
            entityServiceFactory.updateExternalEntity(externalKeys, list);
        }
        return Result.success(list);
    }

    @GetMapping("/{id}")
    @Authentication
    @Operation(summary = "根据主键查找")
    @Parameters({@Parameter(name = "id", description = "主键")})
    public Result<? extends ENTITY> getById(@PathVariable("id") ID id) throws Exception {
        ENTITY entity = baseService.getById(id);
        List<Column> columns = fieldDefineUtil.getColumns(getEntityClass().getName(), false);
        Set<Column> externalKeys = columns.stream().filter(f -> f.getExternalClass() != null && Boolean.TRUE.equals(f.getExternalClassAutoFill())).collect(Collectors.toSet());
        if (!externalKeys.isEmpty()) {
            entityServiceFactory.updateExternalEntity(externalKeys, List.of(entity));
        }
        return Result.success(entity);
    }

    @PostMapping("")
    @Authentication
    @Operation(summary = "存在更新记录，否则插入一条记录")
    @Log
    public Result<? extends ENTITY> saveOrUpdate(@RequestBody ENTITY entity) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("saveOrUpdate: {}", JacksonUtil.toJson(entity));
        }
        validEntity(entity);
        if (!baseService.saveOrUpdate(entity)) {
            throw new BizException(I18nUtil.getMessage("saveRecordFailed"));
        }
        return Result.success(entity);
    }

    @PostMapping("/extra")
    @Authentication
    @Operation(summary = "存在更新记录，否则插入一条记录")
    @Log
    public Result<? extends ENTITY> saveOrUpdate(@RequestBody String body) throws Exception {
        ENTITY entity = JacksonUtil.nativeRead(body, baseService.getEntityClass());
        if (log.isInfoEnabled()) {
            log.info("saveOrUpdate: {}", body);
        }
        validEntity(entity);
        if (!baseService.saveOrUpdate(entity)) {
            throw new BizException(I18nUtil.getMessage("saveRecordFailed"));
        }
        return Result.success(entity);
    }

    @PostMapping("/batch")
    @Authentication
    @Operation(summary = "存在更新记录，否则插入记录（批量）")
    @Log
    public Result<List<ENTITY>> saveOrUpdateBatch(@RequestBody List<ENTITY> entities) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("saveOrUpdateBatch: {}", JacksonUtil.toJson(entities));
        }
        List<Column> columns = fieldDefineUtil.getColumns(baseService.getEntityClass().getName(), false);
        for (ENTITY entity : entities) {
            validEntity(entity, columns);
        }
        if (!baseService.saveOrUpdateBatch(entities)) {
            throw new BizException(I18nUtil.getMessage("saveRecordFailed"));
        }
        return Result.success(entities);
    }

    @PutMapping("")
    @Authentication
    @Operation(summary = "根据主键更新记录")
    @Log
    public Result<? extends ENTITY> updateById(@RequestBody ENTITY entity) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("updateById: {}", JacksonUtil.toJson(entity));
        }
        validEntity(entity);
        if (!baseService.updateById(entity)) {
            throw new BizException(I18nUtil.getMessage("saveRecordFailed"));
        }
        return Result.success(entity);
    }

    @PutMapping("/batch")
    @Authentication
    @Operation(summary = "根据主键批量更新记录")
    @Log
    public Result<List<ENTITY>> updateBatchById(@RequestBody List<ENTITY> entities) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("updateBatchById: {}", JacksonUtil.toJson(entities));
        }
        List<Column> columns = fieldDefineUtil.getColumns(baseService.getEntityClass().getName(), false);
        for (ENTITY entity : entities) {
            validEntity(entity, columns);
        }
        if (!baseService.updateBatchById(entities)) {
            throw new BizException(I18nUtil.getMessage("saveRecordFailed"));
        }
        return Result.success(entities);
    }

    @DeleteMapping("/{id}")
    @Authentication
    @Operation(summary = "根据主键删除记录")
    @Log
    public Result<Boolean> removeById(@PathVariable("id") ID id) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("removeById: {}", id);
        }
        return Result.success(baseService.removeById(id));
    }

    @DeleteMapping("/batch")
    @Authentication
    @Operation(summary = "根据主键批量删除记录")
    @Log
    public Result<Boolean> removeByIds(@RequestBody List<ENTITY> entities) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("removeByIds: {}", JacksonUtil.toJson(entities));
        }
        return Result.success(baseService.removeByIds(entities));
    }

    @DeleteMapping("")
    @Authentication
    @Operation(summary = "根据条件批量删除记录（慎重）")
    @Log
    public Result<Boolean> removeByWrapper(@RequestBody ENTITY entity) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("removeByWrapper: {}", JacksonUtil.toJson(entity));
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String contentType = request.getHeader("content-type");
        if (StringUtils.isEmpty(contentType)) {
            return Result.success(baseService.remove(RequestUtil.getQueryWrapper(baseService.getEntityClass())));
        } else {
            if (contentType.contains("x-www-form-urlencoded")) {
                return Result.success(baseService.remove(RequestUtil.getQueryWrapper(baseService.getEntityClass())));
            } else {
                // application/json
                return Result.success(baseService.remove(RequestUtil.getQueryWrapper(baseService.getEntityClass())));
            }
        }
    }

    @GetMapping("/excel/template")
    @Authentication
    @Operation(summary = "下载Excel导入模板")
    public void downloadExcelTemplate(HttpServletResponse response) throws Exception {
        List<Column> columns = fieldDefineUtil.getColumns(baseService.getEntityClass().getName(), false);

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
        String encodeFileName = I18nUtil.getLanguageValue(entityDefineUtil.getEntityName(baseService.getEntityClass().getName())) + I18nUtil.getMessage("excel.import.template") + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(encodeFileName, StandardCharsets.UTF_8));

        writer.flush(response.getOutputStream());
        writer.close();
        response.getOutputStream().flush();
    }

    @PostMapping("/excel/importPreview")
    @Authentication
    @Operation(summary = "Excel导入预览")
    public Result<List<Map<String, Object>>> uploadExcelPreview(@RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        ExcelReader excelReader = ExcelUtil.getReader(file.getInputStream(), 0);
        int rowCount = excelReader.getRowCount();
        if (rowCount < 3) {
            return Result.success(Collections.emptyList());
        }

        List<Column> columns = fieldDefineUtil.getColumns(baseService.getEntityClass().getName(), false);
        columns = columns.stream().filter(s -> YesNo.YES.equals(s.getIsForm())).toList();

        boolean success = true;
        List<ENTITY> list = excelReader.read(1, 2, baseService.getEntityClass());
        I18nUtil.encodeToI18nFormat(list, columns);
        List<Map<String, Object>> excelRowPreviews = new ArrayList<>();

        StringBuilder errMessage = new StringBuilder();
        for (ENTITY entity : list) {
            String message = valid(entity, columns);
            Map<String, Object> row = BeanUtil.beanToMap(entity);
            if (StringUtils.isNotEmpty(message)) {
                row.put("validateResult", message);
                errMessage.append(message);
                success = false;
            }
            excelRowPreviews.add(row);
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

    protected void appendErrorMessage(Map<String, Object> row, String msg) {
        Object error = row.get(Constant.VALID_MSG_KEY);
        if (error != null) {
            row.put(Constant.VALID_MSG_KEY, error + msg + ";");
        } else {
            row.put(Constant.VALID_MSG_KEY, msg + ";");
        }
    }

    protected void validEntity(ENTITY entity) throws Exception {
        List<Column> columns = fieldDefineUtil.getColumns(baseService.getEntityClass().getName(), false);
        String msg = valid(entity, columns);
        if (StringUtils.isNotEmpty(msg)) {
            throw new BizException(msg);
        }
    }

    protected void validEntity(ENTITY entity, List<Column> columns) throws Exception {
        String msg = valid(entity, columns);
        if (StringUtils.isNotEmpty(msg)) {
            throw new BizException(msg);
        }
    }

    /**
     * 对上传的记录做一些唯一性、重复性校验
     *
     * @param entities
     * @param excelRowPreviews
     */
    protected String validBatch(List<ENTITY> entities, List<Map<String, Object>> excelRowPreviews) {
        return null;
    }

    protected String valid(ENTITY entity, List<Column> columns) {
        StringBuilder builder = new StringBuilder();
        for (Column column : columns) {
            Object value = ReflectUtil.getFieldValue(entity, column.getName());
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
                    Number no = (Number) value;
                    if (no.longValue() > column.getMax()) {
                        builder.append(I18nUtil.getLanguageValue(column.getDisplayName())).append(":").append(I18nUtil.getMessage("validator.error.field.max")).append(column.getMax()).append(";");
                    }
                    if (no.longValue() < column.getMin()) {
                        builder.append(I18nUtil.getLanguageValue(column.getDisplayName())).append(":").append(I18nUtil.getMessage("validator.error.field.min")).append(column.getMin()).append(";");
                    }
                }
            }
        }
        return builder.toString();
    }


    @PostMapping("/excel/export")
    @Authentication
    @Operation(summary = "Excel数据导出")
    public void exportExcel(@RequestBody List<String> fieldNames, HttpServletResponse response) throws Exception {
        List<Column> columns = fieldDefineUtil.getColumns(baseService.getEntityClass().getName(), false);
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

        QueryWrapper<ENTITY> wrapper = RequestUtil.getQueryWrapper(baseService.getEntityClass());
        List<ENTITY> list = baseService.list(wrapper);
        int curRow = 2;
        for (ENTITY entity : list) {
            int col = 0;
            for (String fieldName : fieldNames) {
                Column column = columnMap.get(fieldName);
                Object value = ReflectUtil.getFieldValue(entity, fieldName);

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
        String encodeFileName = I18nUtil.getLanguageValue(I18nUtil.getLanguageValue(entityDefineUtil.getEntityName(baseService.getEntityClass().getName()))) + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".xlsx";
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
