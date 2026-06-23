package com.byteasy.enums;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum ColumnType implements IBaseEnum<Integer> {
    BOOL(14, "布尔"),
    BYTE(0, "字节"),
    SHORT(1, "短整型"),
    INT(2, "整型"),
    LONG(3, "长整型"),
    FLOAT(4, "单精度浮点数"),
    DOUBLE(5, "双精度浮点数"),
    STRING(6, "字符串"),
    LOCAL_DATE(7, "日期"),
    LOCAL_DATE_TIME(8, "时间"),
    ENUM(9, "自定义枚举"),
    ARRAY(10, "数组"),
    JSON(11, "Json对象"),
    JSON_ARRAY(12, "Json对象数组"),
    SPLITTER(13, "分割对象"),
    ;
    public final int value;
    public final String desc;

    ColumnType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static ColumnType fromValue(int value) {
        for (ColumnType status : ColumnType.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的删除状态值: " + value);
    }

    @Override
    public String getName() {
        return desc;
    }

    public static ColumnType fromClass(Class<?> cls) {
        if (cls.isEnum()) {
            return ColumnType.ENUM;
        } else if (cls.isArray() || List.class.isAssignableFrom(cls) || Set.class.isAssignableFrom(cls)) {
            return ColumnType.ARRAY;
        } else if (Boolean.class.equals(cls) || boolean.class.equals(cls)) {
            return ColumnType.BOOL;
        } else if (Byte.class.equals(cls) || byte.class.equals(cls)) {
            return ColumnType.BYTE;
        } else if (Short.class.equals(cls) || short.class.equals(cls)) {
            return ColumnType.SHORT;
        } else if (Integer.class.equals(cls) || int.class.equals(cls)) {
            return ColumnType.INT;
        } else if (Duration.class.equals(cls)) {
            return ColumnType.INT;
        } else if (Long.class.equals(cls) || long.class.equals(cls)) {
            return ColumnType.LONG;
        } else if (Float.class.equals(cls) || float.class.equals(cls)) {
            return ColumnType.FLOAT;
        } else if (Double.class.equals(cls) || double.class.equals(cls)) {
            return ColumnType.DOUBLE;
        } else if (String.class.equals(cls)) {
            return ColumnType.STRING;
        } else if (LocalDate.class.equals(cls)) {
            return ColumnType.LOCAL_DATE;
        } else if (LocalDateTime.class.equals(cls)) {
            return ColumnType.LOCAL_DATE_TIME;
        } else if (Map.class.isAssignableFrom(cls) || isCustomClassByClassLoader(cls)) {
            return ColumnType.JSON;
        }
        return null;
    }

    private static boolean isCustomClassByClassLoader(Class<?> cls) {
        if (cls.isPrimitive() || cls.isArray() || cls.isEnum() || cls.isInterface()) {
            return false;
        }
        return cls.getClassLoader() != null;
    }
}
