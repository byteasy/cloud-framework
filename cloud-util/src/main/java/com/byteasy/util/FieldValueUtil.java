package com.byteasy.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FieldValueUtil {

    public static Object valueOf(String value, Class<?> field) {
        if (field.equals(Boolean.class)) {
            return Boolean.valueOf(value);
        } else if (field.equals(String.class)) {
            return value;
        } else if (field.equals(boolean.class)) {
            return Boolean.parseBoolean(value);
        } else if (field.equals(Byte.class)) {
            return Byte.valueOf(value);
        } else if (field.equals(byte.class)) {
            return Byte.parseByte(value);
        } else if (field.equals(Short.class)) {
            return Short.valueOf(value);
        } else if (field.equals(short.class)) {
            return Short.parseShort(value);
        } else if (field.equals(Integer.class)) {
            return Integer.valueOf(value);
        } else if (field.equals(int.class)) {
            return Integer.parseInt(value);
        } else if (field.equals(Long.class)) {
            return Long.valueOf(value);
        } else if (field.equals(long.class)) {
            return Long.parseLong(value);
        } else if (field.equals(Float.class)) {
            return Float.valueOf(value);
        } else if (field.equals(float.class)) {
            return Float.parseFloat(value);
        } else if (field.equals(Double.class)) {
            return Double.valueOf(value);
        } else if (field.equals(double.class)) {
            return Double.parseDouble(value);
        } else if (field.equals(BigDecimal.class)) {
            return new BigDecimal(value);
        } else if (field.equals(BigInteger.class)) {
            return new BigInteger(value);
        } else if (field.equals(Date.class)) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else if (field.equals(LocalDate.class)) {
            return LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else if (field.equals(LocalDateTime.class)) {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else if (field.isEnum()) {
            Object[] objs = field.getEnumConstants();
            if (objs != null) {
                for (Object obj : objs) {
                    if (obj.toString().equalsIgnoreCase(value)) {
                        return obj;
                    }
                }
            }
        }
        return null;
    }

    public static Object[] toArray(String[] value, Class<?> type) {
        Object[] results = new Object[value.length];
        for (int i = 0; i < value.length; i++) {
            results[i] = valueOf(value[i], type);
        }
        return results;
    }
}
