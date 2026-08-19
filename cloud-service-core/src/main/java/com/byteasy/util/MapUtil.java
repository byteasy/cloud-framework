package com.byteasy.util;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class MapUtil {

    public static Object getSingleValue(Map<String, ?> map, String key) {
        if (!map.containsKey(key)) {
            return null;
        }
        Object value = map.get(key);
        if (value.getClass().isArray()) {
            return ((String[]) value)[0];
        }
        if (value instanceof List<?>) {
            return ((List<?>) value).getFirst();
        }
        return value;
    }

    public static Map<String, Object> convertToMap(Map<String, String[]> params) {
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(params.size());
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            if (entry.getValue().length == 1) {
                // 单值
                map.put(entry.getKey(), entry.getValue()[0]);
            } else if (entry.getValue().length > 1) {
                // 数组
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }
}
