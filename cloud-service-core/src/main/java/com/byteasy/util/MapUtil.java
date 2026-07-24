package com.byteasy.util;

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
}
