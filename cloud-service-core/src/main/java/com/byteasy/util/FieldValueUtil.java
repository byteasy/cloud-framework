package com.byteasy.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReflectUtil;

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
}
