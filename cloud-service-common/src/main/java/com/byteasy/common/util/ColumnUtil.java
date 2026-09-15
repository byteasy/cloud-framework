package com.byteasy.common.util;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.common.component.FieldDefineLoadingCache;
import com.byteasy.entity.LogicDeleteEntity;
import com.byteasy.enums.*;
import com.byteasy.platform.entity.FieldDefine;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.swagger.v3.core.util.ReflectionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ColumnUtil {
    public static Map<String, FieldDefine> getColumns(Class<?> cls) {
        if (Enum.class.isAssignableFrom(cls)) {
            return ColumnUtil.getEnumColumns(cls);
        }
        Map<String, FieldDefine> map = new HashMap<>();

        List<Field> fields = ReflectionUtils.getDeclaredFields(cls);
        for (Field field : fields) {
            FieldDefine column = new FieldDefine();
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
                column.setGroupId(fieldMeta.groupId() == 0 ? null : fieldMeta.groupId());
                column.setIsPrimary(fieldMeta.isPrimary() ? YesNo.YES : YesNo.NO);
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
                column.setExtrasParams(fieldMeta.extrasParams());
            } else {
                column.setIsPrimary(YesNo.NO);
                column.setIsShow(YesNo.YES);
                column.setIsForm(YesNo.YES);
                column.setEncrypt(YesNo.NO);
                column.setDesensitize(YesNo.NO);
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
        Schema schema = ReflectionUtils.getAnnotation(cls, Schema.class);
        if (schema != null && StringUtils.isNotEmpty(schema.description())) {
            return schema.description();
        }
        return null;
    }


    public static Map<String, FieldDefine> getEnumColumns(Class<?> cls) {
        if (IBaseEnum.class.isAssignableFrom(cls)) {
            Class<? extends IBaseEnum> enumCls = (Class<? extends IBaseEnum>) cls;
            IBaseEnum[] enums = enumCls.getEnumConstants();
            Map<String, FieldDefine> result = Maps.newHashMapWithExpectedSize(enums.length);
            for (IBaseEnum anEnum : enums) {
                FieldDefine column = new FieldDefine();
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
            Map<String, FieldDefine> result = Maps.newHashMapWithExpectedSize(names.size());
            for (String name : names) {
                FieldDefine column = new FieldDefine();
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
        return field.getAnnotation(FieldMeta.class);
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

    public static Set<Object> getPrimaryKeys(FieldDefine primaryKey, List<Map<String, Object>> entities) {
        Set<Object> result = Sets.newHashSet();
        for (Map<String, Object> entity : entities) {
            result.add(convertToDbValue(entity.get(primaryKey.getName()), primaryKey));
        }
        return result;
    }

    public static Object convertToDbValue(Object value, FieldDefine column) {
        if (value == null) {
            return null;
        }
        String valStr = value instanceof String ? (String) value : String.valueOf(value);
        if (valStr.isEmpty()) {
            return null;
        }

        try {
            return switch (column.getType()) {
                case BYTE, SHORT, INT, LONG -> NumberUtil.parseLong(valStr);
                case FLOAT, DOUBLE -> NumberUtil.parseDouble(valStr);
                case BOOL -> BooleanUtil.toBooleanObject(valStr);
                case LOCAL_DATE_TIME ->
                        Timestamp.valueOf(LocalDateTime.parse(valStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                case LOCAL_DATE ->
                        Timestamp.valueOf(LocalDateTime.parse(valStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                case ENUM -> {
                    String className = column.getTypeClassName();
                    if (className == null) yield null;

                    try {
                        Class<?> clazz = Class.forName(className);

                        // 必须是枚举，且实现 IBaseEnum
                        if (!clazz.isEnum() || !IBaseEnum.class.isAssignableFrom(clazz)) {
                            yield null;
                        }

                        // 拿到所有枚举实例
                        Object[] enums = clazz.getEnumConstants();
                        for (Object e : enums) {
                            if (e instanceof IBaseEnum<?>) {
                                IBaseEnum<?> enumObj = (IBaseEnum<?>) e;
                                // 把枚举值转成字符串比较，匹配传入的 valStr
                                if (String.valueOf(enumObj.getEnumName()).equals(valStr)) {
                                    yield enumObj.getValue();
                                }
                            }
                        }
                        yield null;
                    } catch (Exception e) {
                        // 枚举从数据库中定义的1、2、3...等转为枚举字母单词
                        Object resultValue = null;
                        FieldDefineLoadingCache fieldDefineLoadingCache = SpringUtil.getBean(FieldDefineLoadingCache.class);
                        List<FieldDefine> enumDefineList = fieldDefineLoadingCache.load(className);
                        for (FieldDefine enumDef : enumDefineList) {
                            if (enumDef.getName().equals(valStr)) {
                                switch (enumDef.getType()) {
                                    case BYTE, SHORT, INT, LONG -> resultValue = Long.valueOf(enumDef.getFieldName());
                                    case BOOL -> resultValue = Boolean.valueOf(enumDef.getFieldName());
                                    case FLOAT, DOUBLE -> resultValue = Double.valueOf(enumDef.getFieldName());
                                    case STRING -> resultValue = enumDef.getFieldName();
                                }
                            }
                        }
                        yield resultValue;
                    }
                }
                default -> valStr;
            };
        } catch (Exception e) {
            return value;
        }
    }


    public static Object convertToEntityValue(Object value, FieldDefine column) {
        switch (column.getType()) {
            case ENUM -> {
                String className = column.getTypeClassName();
                if (className == null) return value;
                try {
                    Class<?> clazz = Class.forName(className);
                    // 必须是枚举，且实现 IBaseEnum
                    if (!clazz.isEnum() || !IBaseEnum.class.isAssignableFrom(clazz)) {
                        return value;
                    }
                    // 拿到所有枚举实例
                    Object[] enums = clazz.getEnumConstants();
                    for (Object e : enums) {
                        if (e instanceof IBaseEnum<?>) {
                            IBaseEnum<?> enumObj = (IBaseEnum<?>) e;
                            if (enumObj.getValue().equals(value)) {
                                return enumObj;
                            }
                        }
                    }
                    return value;
                } catch (Exception e) {
                    // 枚举从数据库中定义的1、2、3...等转为枚举字母单词
                    FieldDefineLoadingCache fieldDefineLoadingCache = SpringUtil.getBean(FieldDefineLoadingCache.class);
                    List<FieldDefine> enumDefineList = fieldDefineLoadingCache.load(className);
                    for (FieldDefine enumDef : enumDefineList) {
                        if (enumDef.getFieldName().equals(String.valueOf(value))) {
                            return enumDef.getName();
                        }
                    }
                    return value;
                }
            }
        }
        return value;
    }

    public static Set<String> getAuditFields() {
        Field[] fields = ReflectUtil.getFields(LogicDeleteEntity.class);
        Set<String> result = Sets.newHashSet();
        for (Field field : fields) {
            result.add(field.getName());
        }

        return result;
    }
}
