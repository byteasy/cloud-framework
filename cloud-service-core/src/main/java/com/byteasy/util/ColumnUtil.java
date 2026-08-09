package com.byteasy.util;

import cn.hutool.core.util.EnumUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldDisplay;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.common.util.GenericTypeUtil;
import com.byteasy.common.util.NameConvertUtil;
import com.byteasy.dto.Column;
import com.byteasy.enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Maps;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.proxy.Proxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ColumnUtil {
    public static Map<String, Column> getColumns(Class<?> cls) {
        if (Enum.class.isAssignableFrom(cls)) {
            return ColumnUtil.getEnumColumns(cls);
        }
        Map<String, Column> map = new HashMap<>();

        List<Field> fields = ReflectionUtils.getDeclaredFields(cls);
        for (Field field : fields) {
            Column column = new Column();
            column.setClassName(cls.getName());
            Schema schema = field.getAnnotation(Schema.class);
            FieldMeta fieldMeta = getFieldMeta(field);
            TableField tableField = field.getAnnotation(TableField.class);
            column.setName(field.getName());
            if (tableField != null) {
                column.setFieldName(tableField.value());
            }
            String name = schema != null ? schema.description() : (fieldMeta != null ? fieldMeta.name() : field.getName());
            if (StringUtils.isEmpty(name)) {
                name = field.getName();
            }
            if (name.startsWith("{")) {
                column.setDisplayName(name);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.set("zh_CN", name);
                column.setDisplayName(jsonObject.toString());
            }
            if (column.getName().equals("id")) {
                Class typeClass = GenericTypeUtil.getEntityIdType(cls);
                if (typeClass != null) {
                    column.setType(ColumnType.fromClass(typeClass));
                    column.setTypeClassName(typeClass.getName());
                } else { // 有部分ID不是通过泛型定义的
                    column.setType(ColumnType.fromClass(field.getType()));
                    column.setTypeClassName(field.getType().getName());
                }
            } else {
                column.setType(ColumnType.fromClass(field.getType()));
                column.setTypeClassName(field.getType().getName());
            }
            if (fieldMeta != null) {
                if (StringUtils.isNotEmpty(fieldMeta.innerPrimaryField())) {
                    column.setInnerPrimaryField(fieldMeta.innerPrimaryField());
                }
                column.setDescription(fieldMeta.description());
                // 显示顺序
                column.setShowOrder(fieldMeta.showOrder());
                // 显示宽度（0 则用默认值 120）
                column.setWidth(fieldMeta.width());
                // 是否显示（boolean 转 YesNo 枚举）
                column.setIsShow(fieldMeta.isShow() ? YesNo.YES : YesNo.NO);
                // 是否排序
                column.setIsSort(fieldMeta.isSort() ? YesNo.YES : YesNo.NO);
                // 是否省略
                column.setIsOverflow(fieldMeta.isOverflow() ? YesNo.YES : YesNo.NO);
                column.setIsI18n(fieldMeta.isI18n() ? YesNo.YES : YesNo.NO);
                // 是否搜索
                column.setIsSearch(fieldMeta.isSearch() ? YesNo.YES : YesNo.NO);
                column.setIsForm(fieldMeta.isForm() ? YesNo.YES : YesNo.NO);
                column.setIsRequire(fieldMeta.isRequire() ? YesNo.YES : YesNo.NO);
                column.setIsFixed(fieldMeta.isFixed() ? YesNo.YES : YesNo.NO);
                // 对齐方式（直接赋值枚举）
                column.setAlign(fieldMeta.align());
                column.setMaxLength(fieldMeta.maxLength());
                column.setMinLength(fieldMeta.minLength());
                column.setMax(fieldMeta.max());
                column.setMin(fieldMeta.min());
                column.setPrecision(fieldMeta.precision());
                column.setMultiple(fieldMeta.multiple() ? YesNo.YES : YesNo.NO);
                column.setPattern(fieldMeta.pattern() == ValidatorPattern.NONE ? null : fieldMeta.pattern());
                // 是否外键
                column.setExternalClass(fieldMeta.externalClass().equals(void.class) ? null : fieldMeta.externalClass().getName());
                column.setExternalClassPrimaryField(fieldMeta.externalClassPrimaryField());
                column.setExternalClassDisplayField(fieldMeta.externalClassDisplayField());
                column.setUiComponent(fieldMeta.uiComponent());
                column.setEncrypt(fieldMeta.encrypt() ? YesNo.YES : YesNo.NO);
                column.setDesensitize(fieldMeta.desensitize() ? YesNo.YES : YesNo.NO);
                if (column.getExternalClass() != null) {
                    if (tableField != null && tableField.exist()) {
                        // 外键关联，此字段存储的是其他表的主键或者编码字段
                        column.setRelationType(RelationType.EXTERNAL_KEY);
                    } else {
                        // 此字段表中无对应字段，字段类型为字符串，用于显示明细表对象的展示名称
                        column.setRelationType(RelationType.DETAIL_TABLE);
                    }
                }
            } else {
                column.setIsShow(YesNo.YES);
                column.setIsForm(YesNo.YES);
            }
            JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
            if (jsonFormat != null) {
                column.setSerialPattern(jsonFormat.pattern());
                column.setTimezone(jsonFormat.timezone());
            }

            /**
             * 如果数据库字段名为空，根据驼峰规则转化为数据库字段名
             */
            if (StringUtils.isEmpty(column.getFieldName()) && (tableField == null || tableField.exist())) {
                column.setFieldName(NameConvertUtil.camelToUnderline(column.getName()));
            }
            map.put(field.getName(), column);
        }
        return map;
    }

    public static String getSchemaName(Class<?> cls) {
        FieldMeta fieldMeta = ReflectionUtils.getAnnotation(cls, FieldMeta.class);
        if (fieldMeta != null && StringUtils.isNotEmpty(fieldMeta.name())) {
            return fieldMeta.name();
        }
        FieldDisplay fieldDisplay = ReflectionUtils.getAnnotation(cls, FieldDisplay.class);
        if (fieldDisplay != null && StringUtils.isNotEmpty(fieldDisplay.name())) {
            return fieldDisplay.name();
        }
        Schema schema = ReflectionUtils.getAnnotation(cls, Schema.class);
        if (schema != null && StringUtils.isNotEmpty(schema.name())) {
            return schema.name();
        }
        return null;
    }

    public static String getTableName(Class<?> cls) {
        TableName tableName = ReflectionUtils.getAnnotation(cls, TableName.class);
        if (tableName != null) {
            return tableName.value();
        }
        return NameConvertUtil.camelToUnderline(cls.getSimpleName());
    }

    public static String getSchemaDescription(Class<?> cls) {
        FieldMeta fieldMeta = ReflectionUtils.getAnnotation(cls, FieldMeta.class);
        if (fieldMeta != null && StringUtils.isNotEmpty(fieldMeta.description())) {
            return fieldMeta.description();
        }
        FieldDisplay fieldDisplay = ReflectionUtils.getAnnotation(cls, FieldDisplay.class);
        if (fieldDisplay != null && StringUtils.isNotEmpty(fieldDisplay.description())) {
            return fieldDisplay.description();
        }
        Schema schema = ReflectionUtils.getAnnotation(cls, Schema.class);
        if (schema != null && StringUtils.isNotEmpty(schema.description())) {
            return schema.description();
        }
        return null;
    }


    public static Map<String, Column> getEnumColumns(Class<?> cls) {
        if (IBaseEnum.class.isAssignableFrom(cls)) {
            Class<? extends IBaseEnum> enumCls = (Class<? extends IBaseEnum>) cls;
            IBaseEnum[] enums = enumCls.getEnumConstants();
            Map<String, Column> result = Maps.newHashMapWithExpectedSize(enums.length);
            for (IBaseEnum anEnum : enums) {
                Column column = new Column();
                column.setClassName(cls.getName());
                column.setName(anEnum.getEnumName());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("zh_CN", anEnum.getName());
                column.setDisplayName(jsonObject.toString());
                column.setType(ColumnType.ENUM);
                column.setTypeClassName(anEnum.getCss());
                result.put(anEnum.getEnumName(), column);
            }
            return result;
        } else {
            List<String> names = EnumUtil.getNames((Class<? extends Enum<?>>) cls);
            Map<String, Column> result = Maps.newHashMapWithExpectedSize(names.size());
            for (String name : names) {
                Column column = new Column();
                column.setClassName(cls.getName());
                column.setName(name);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("zh_CN", name);
                column.setDisplayName(jsonObject.toString());
                column.setType(ColumnType.ENUM);
                column.setTypeClassName(null);
                result.put(name, column);
            }
            return result;
        }
    }

    public static FieldMeta getFieldMeta(Field field) {
        FieldMeta meta = field.getAnnotation(FieldMeta.class);
        if (meta != null) return meta;

        FieldDisplay display = field.getAnnotation(FieldDisplay.class);
        if (display == null) return null;

        // 动态代理自动映射同名字段（超级优雅）
        return (FieldMeta) Proxy.newProxyInstance(
                FieldMeta.class.getClassLoader(),
                new Class[]{FieldMeta.class},
                (proxy, method, args) -> {
                    Method displayMethod = FieldDisplay.class.getMethod(method.getName());
                    return displayMethod.invoke(display);
                }
        );
    }

    public static Set<String> getFieldNames(Class<?> cls) {
        List<Field> fields = ReflectionUtils.getDeclaredFields(cls);
        return fields.stream().map(Field::getName).collect(Collectors.toSet());
    }

    public static Object toValue(JSONObject jsonObject, ColumnType type, String fieldName) {
        switch (type) {
            case ColumnType.INT -> {
                return jsonObject.getInt(fieldName);
            }
            case ColumnType.LONG -> {
                return jsonObject.getLong(fieldName);
            }
            case ColumnType.FLOAT -> {
                return jsonObject.getFloat(fieldName);
            }
            case ColumnType.DOUBLE -> {
                return jsonObject.getDouble(fieldName);
            }
            case ColumnType.BYTE -> {
                return jsonObject.getByte(fieldName);
            }
            case ColumnType.SHORT -> {
                return jsonObject.getShort(fieldName);
            }
            case ColumnType.BOOL -> {
                return jsonObject.getBool(fieldName);
            }
            case ColumnType.STRING -> {
                return jsonObject.getStr(fieldName);
            }
        }
        return jsonObject.get(fieldName);
    }
}
