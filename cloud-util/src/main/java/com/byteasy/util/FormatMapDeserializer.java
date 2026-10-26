package com.byteasy.util;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;

/**
 * Jackson 3.0 适配版本 FormatMap 反序列化器
 */
public class FormatMapDeserializer extends ValueDeserializer<FormatMap> {

    // 格式化器常量（DateTimeFormatter 本身是线程安全的，可复用）
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public FormatMap deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        FormatMap map = new FormatMap();

        // 🔥 Jackson 3 核心变更：由 ctxt.readTree(p) 替代 p.getCodec().readTree(p)
        JsonNode node = ctxt.readTree(p);
        if (node == null || !node.isObject()) {
            return map;
        }

        Iterator<String> fieldNames = node.propertyNames().iterator();

        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode fieldNode = node.get(fieldName);

            if (fieldNode.isNull()) {
                map.put(fieldName, null);
                continue;
            }

            try {
                // 1. 文本类型智能推断与解析
                if (fieldNode.isTextual()) {
                    String text = fieldNode.asText();

                    // 优先尝试解析 LocalDateTime (yyyy-MM-dd HH:mm:ss)
                    try {
                        map.put(fieldName, LocalDateTime.parse(text, DATETIME_FORMATTER));
                        continue;
                    } catch (Exception ignored) {
                    }

                    // 尝试解析 LocalDate (yyyy-MM-dd)
                    try {
                        map.put(fieldName, LocalDate.parse(text, DATE_FORMATTER));
                        continue;
                    } catch (Exception ignored) {
                    }

                    // 尝试解析 java.util.Date
                    try {
                        // 注意：SimpleDateFormat 非线程安全，每次解析需实例化或使用 ThreadLocal
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(text);
                        map.put(fieldName, date);
                        continue;
                    } catch (Exception ignored) {
                    }

                    // 普通字符串
                    map.put(fieldName, text);
                    continue;
                }

                // 2. 数值类型
                if (fieldNode.isNumber()) {
                    // 若是浮点/大数，保留高精度 BigDecimal
                    if (fieldNode.isFloatingPointNumber() || fieldNode.isBigDecimal()) {
                        map.put(fieldName, fieldNode.decimalValue());
                    } else if (fieldNode.isLong()) {
                        map.put(fieldName, fieldNode.asLong());
                    } else {
                        map.put(fieldName, fieldNode.asInt());
                    }
                    continue;
                }

                // 3. 布尔类型
                if (fieldNode.isBoolean()) {
                    map.put(fieldName, fieldNode.asBoolean());
                    continue;
                }

                // 4. 复杂嵌套结构（对象或数组）转换为基础 Java 类型 (Map / List / 原生值)
                map.put(fieldName, ctxt.readTreeAsValue(fieldNode, Object.class));

            } catch (Exception e) {
                // 容错降级：如果转换失败，放入底层 node 或 asText
                map.put(fieldName, fieldNode.asText());
            }
        }

        return map;
    }
}