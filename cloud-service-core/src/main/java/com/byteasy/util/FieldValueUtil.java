package com.byteasy.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;
import com.byteasy.entity.Entity;

import java.util.Map;

public class FieldValueUtil {

    public static <T> T getFieldValue(Object object, String fieldName, Class<T> cls) {
        if (object instanceof Map<?, ?>) {
            return MapUtil.get((Map) object, fieldName, cls);
        } else {
            Object value = ReflectUtil.getFieldValue(object, fieldName);
            return (T) value;
        }
    }

    public static Object getFieldValue(Object object, String fieldName) {
        if (object instanceof Map<?, ?>) {
            return ((Map<?, ?>) object).get(fieldName);
        } else {
            return ReflectUtil.getFieldValue(object, fieldName);
        }
    }

    public static void setFieldValue(Object object, String fieldName, Object value) {
        if (object instanceof Map) {
            ((Map) object).put(fieldName, value);
        } else if (object instanceof Entity<?>) {
            ((Entity) object).setExtendField(fieldName, value);
        } else {
            ReflectUtil.setFieldValue(object, fieldName, value);
        }
    }
}
