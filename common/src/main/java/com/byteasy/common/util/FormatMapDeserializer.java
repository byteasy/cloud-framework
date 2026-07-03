package com.byteasy.common.util;

import com.byteasy.dto.FormatMap;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class FormatMapDeserializer extends JsonDeserializer<FormatMap> {

    @Override
    public FormatMap deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        FormatMap map = new FormatMap();
        JsonNode node = p.getCodec().readTree(p);
        Iterator<String> fieldNames = node.fieldNames();

        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode fieldNode = node.get(fieldName);

            if (fieldNode.isNull()) {
                map.put(fieldName, null);
                continue;
            }

            try {
                // 根据类型自动反序列化
                if (fieldNode.isTextual()) {
                    String text = fieldNode.asText();

                    // 尝试解析 LocalDateTime
                    try {
                        map.put(fieldName, LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        continue;
                    } catch (Exception ignored) {
                    }

                    // 尝试解析 LocalDate
                    try {
                        map.put(fieldName, LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        continue;
                    } catch (Exception ignored) {
                    }

                    // 尝试解析 Date
                    try {
                        map.put(fieldName, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(text));
                        continue;
                    } catch (Exception ignored) {
                    }

                    // 普通字符串
                    map.put(fieldName, text);
                } else if (fieldNode.isNumber()) {
                    // 数字类型统一用 BigDecimal 保证精度
                    map.put(fieldName, new BigDecimal(fieldNode.asText()));
                } else if (fieldNode.isBoolean()) {
                    map.put(fieldName, fieldNode.asBoolean());
                } else {
                    map.put(fieldName, fieldNode.asText());
                }
            } catch (Exception e) {
                map.put(fieldName, fieldNode.asText());
            }
        }
        return map;
    }
}
