package com.byteasy.common.util;

import com.byteasy.dto.FormatMap;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

public class FormatMapSerializer extends JsonSerializer<FormatMap> {

    // 日期格式器（线程安全）
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void serialize(FormatMap map, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        Map<String, String> rules = map.getFormatRules();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String pattern = rules.get(key);

            gen.writeFieldName(key);

            if (value == null) {
                gen.writeNull();
                continue;
            }

            try {
                // ====================== 日期/时间类型 ======================
                if (value instanceof LocalDateTime) {
                    LocalDateTime time = (LocalDateTime) value;
                    if (pattern != null) {
                        gen.writeString(time.format(DateTimeFormatter.ofPattern(pattern)));
                    } else {
                        gen.writeString(time.format(DATETIME_FORMATTER));
                    }
                } else if (value instanceof LocalDate) {
                    LocalDate date = (LocalDate) value;
                    if (pattern != null) {
                        gen.writeString(date.format(DateTimeFormatter.ofPattern(pattern)));
                    } else {
                        gen.writeString(date.format(DATE_FORMATTER));
                    }
                } else if (value instanceof java.sql.Timestamp) {
                    Date date = new Date(((java.sql.Timestamp) value).getTime());
                    if (pattern != null) {
                        gen.writeString(new SimpleDateFormat(pattern).format(date));
                    } else {
                        gen.writeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
                    }
                } else if (value instanceof Date) {
                    Date date = (Date) value;
                    if (pattern != null) {
                        gen.writeString(new SimpleDateFormat(pattern).format(date));
                    } else {
                        gen.writeString(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
                    }
                }

                // ====================== 数字类型 ======================
                else if (value instanceof Number) {
                    if (value instanceof BigDecimal) {
                        // 防止科学计数法
                        BigDecimal decimal = (BigDecimal) value;
                        if (pattern != null) {
                            gen.writeString(new DecimalFormat(pattern).format(decimal));
                        } else {
                            gen.writeNumber(decimal);
                        }
                    } else {
                        if (pattern != null) {
                            gen.writeString(new DecimalFormat(pattern).format(value));
                        } else {
                            gen.writeNumber(new BigDecimal(value.toString()));
                        }
                    }
                }

                // ====================== 字符串 ======================
                else if (value instanceof String) {
                    gen.writeString((String) value);
                }

                // ====================== 其他类型 ======================
                else {
                    gen.writeObject(value);
                }

            } catch (Exception e) {
                // 序列化失败时输出原值，不抛错
                gen.writeObject(value);
            }
        }

        gen.writeEndObject();
    }
}
