package com.byteasy.common.util;

import cn.hutool.core.util.NumberUtil;
import com.byteasy.dto.FormatMap;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.KotlinDetector;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 主要是因为baomidou的框架没有升级
 */
@Log4j2
@Deprecated
public class JacksonUtilOld {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectMapper OBJECT_WITH_CLASS_MAPPER;

    static {
        // 对于空的对象转json的时候不抛出错误
        OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // 禁用遇到未知属性抛出异常
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 序列化BigDecimal时不使用科学计数法输出
        OBJECT_MAPPER.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        // null的属性不序列化
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.setTimeZone(TimeZone.getDefault());
        OBJECT_MAPPER.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        OBJECT_MAPPER.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        // 定义LocalDateTime 的序列化和反序列化默认
        OBJECT_MAPPER.setTimeZone(TimeZone.getDefault());
        // 日期和时间格式化
        OBJECT_MAPPER.registerModule(customJavaTimeModule());
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 自定义MAP序列化与反序列化
        OBJECT_MAPPER.registerModule(formatMapModule());

        OBJECT_WITH_CLASS_MAPPER = OBJECT_MAPPER.copy();
        /**
         * 序列化时带上class属性信息
         */
        StdTypeResolverBuilder typer = TypeResolverBuilder.forEverything(OBJECT_WITH_CLASS_MAPPER).init(JsonTypeInfo.Id.CLASS, null).inclusion(JsonTypeInfo.As.PROPERTY);
        OBJECT_WITH_CLASS_MAPPER.setDefaultTyping(typer);
    }

    public static JavaTimeModule customJavaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // 1. LocalDateTime 默认格式
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        // 2. 补上 LocalDate 默认格式 (同时原生支持实体类上的 @JsonFormat 覆盖)
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        // 3. 补上 LocalTime 默认格式
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

        // 4. Duration
        javaTimeModule.addSerializer(Duration.class, new DurationToSecondsSerializer());
        javaTimeModule.addDeserializer(Duration.class, new DurationFromSecondsDeserializer());

        return javaTimeModule;
    }

    public static SimpleModule formatMapModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(FormatMap.class, new FormatMapSerializerOld());
        module.addDeserializer(FormatMap.class, new FormatMapDeserializerOld()); // 新增
        return module;
    }

    public static ObjectMapper copy() {
        return OBJECT_MAPPER.copy();
    }

    public static ObjectMapper copyMapperWithClass() {
        return OBJECT_WITH_CLASS_MAPPER.copy();
    }

    /**
     * 序列化对象（转json）
     */
    public static String toJson(Object obj) {
        return toJson(obj, false);
    }

    public static String toJson(Object obj, boolean withClass) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return obj.toString();
        }
        if (obj instanceof Number) {
            return obj.toString();
        }
        try {
            if (withClass) {
                return OBJECT_WITH_CLASS_MAPPER.writeValueAsString(obj);
            } else {
                return OBJECT_MAPPER.writeValueAsString(obj);
            }
        } catch (JsonProcessingException e) {
            log.error("JacksonUtil.toJson-json序列化出错: {}", obj, e);
            return null;
        }
    }

    /**
     * 序列化对象（转json）
     */
    public static String toJson(Object obj, FilterProvider filters) {
        return toJson(obj, filters, false);
    }

    /**
     * 序列化对象（转json）
     */
    public static String toJson(Object obj, FilterProvider filters, boolean withClass) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return obj.toString();
        }
        if (obj instanceof Number) {
            return obj.toString();
        }
        try {
            if (withClass) {
                return OBJECT_WITH_CLASS_MAPPER.writer(filters).writeValueAsString(obj);
            } else {
                return OBJECT_MAPPER.writer(filters).writeValueAsString(obj);
            }
        } catch (JsonProcessingException e) {
            log.error("JacksonUtil.toJson-json序列化出错: {}", obj, e);
            return null;
        }
    }

    /**
     * 反序列化（json转为Bean）
     */
    public static <T> T parse(String json, Class<T> tClass) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, tClass);
        } catch (IOException e) {
            log.error("JacksonUtil.parse-json解析出错: {}", json, e);
            return null;
        }
    }

    /**
     * 反序列带有class类型属性的json
     *
     * @param json
     * @return
     */
    public static Object parseObj(String json) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_WITH_CLASS_MAPPER.readValue(json, Object.class);
        } catch (IOException e) {
            log.error("JacksonUtil.parse-json解析出错: {}", json, e);
            return null;
        }
    }

    /**
     * 反序列带有class类型属性的json
     *
     * @param json
     * @return
     */
    public static List<Object> parseObjList(String json) {
        if (json == null) {
            return Collections.EMPTY_LIST;
        }
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, Object.class);

            return OBJECT_WITH_CLASS_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            log.error("JacksonUtil.parse-json解析出错: {}", json, e);
            return null;
        }
    }

    /**
     * 反序列带有class类型属性的json
     *
     * @param json
     * @return
     */
    public static Object[] parseObjArray(String json) {
        if (json == null) {
            return new Object[]{};
        }
        return parseObjList(json).stream().toArray(Object[]::new);
    }

    /**
     * 反序列化（json转List）
     */
    public static <E> List<E> parseList(String json, Class<E> eClass) {
        return nativeRead(json, List.class, eClass);
    }

    /**
     * 反序列化（json转Map）
     */
    public static <K, V> Map<K, V> parseMap(String json, Class<K> kClass, Class<V> vClass) {
        return nativeRead(json, Map.class, kClass, vClass);
    }

    /**
     * 反序列化（json转LinkedHashMap）
     */
    public static <K, V> LinkedHashMap<K, V> parseLinkedHashMap(String json, Class<K> kClass, Class<V> vClass) {
        return nativeRead(json, LinkedHashMap.class, kClass, vClass);
    }

    /**
     * json转复杂对象
     */
    public static <T> T nativeRead(String json, TypeReference<T> type) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            log.error("JacksonUtil.nativeRead-json解析出错: {}", json, e);
            return null;
        }
    }

    /**
     * @param json         json字符串
     * @param javaClazz    需要转换的java对象类型， 例如：List.class, ArrayList.class, Map.class, HashMap.class, LinkedHashMap.class
     * @param genericClazz 泛型类型
     * @return T
     * @author wen
     * @since 2021/1/20
     */
    public static <T> T nativeRead(String json, Class<?> javaClazz, Class<?>... genericClazz) {
        if (json == null) {
            return null;
        }
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(javaClazz, genericClazz);
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (Exception e) {
            log.error("JacksonUtil.nativeRead-json解析出错: ", e);
            return null;
        }
    }

    /**
     * 字符串转为指定对象
     */
    public static Object parseClazz(String targetStr, Class<?> clazz) {
        if (clazz == String.class) {
            return targetStr;
        }
        if (targetStr == null) {
            return null;
        }
        if (targetStr.startsWith("{")) {
            return parse(targetStr, clazz);
        }
        if (targetStr.startsWith("[")) {
            return parseList(targetStr, clazz);
        }
        if (clazz == Integer.class) {
            return Integer.valueOf(targetStr);
        }
        if (clazz == Long.class) {
            return Long.valueOf(targetStr);
        }
        if (clazz == BigDecimal.class) {
            return new BigDecimal(targetStr);
        }
        if (clazz == Boolean.class) {
            return Boolean.valueOf(targetStr);
        }
        if (clazz == Double.class) {
            return Double.valueOf(targetStr);
        }
        if (clazz == Float.class) {
            return Float.valueOf(targetStr);
        }
        if (clazz == Short.class) {
            return Short.valueOf(targetStr);
        }
        if (clazz == Byte.class) {
            return Byte.valueOf(targetStr);
        }
        return null;
    }

    /**
     * 对象转map
     */
    public static Map<String, Object> obj2Map(Object object) {
        return OBJECT_MAPPER.convertValue(object, new TypeReference<>() {
        });
    }

    /**
     * 对象转Map
     *
     * @param object
     * @return
     */
    public static Map<String, String> obj2StringMap(Object object) {
        return OBJECT_MAPPER.convertValue(object, new TypeReference<>() {
        });
    }

    /**
     * 对象转其他
     */
    public static <T> T obj2Val(Object object) {
        return OBJECT_MAPPER.convertValue(object, new TypeReference<T>() {
        });
    }

    public static JsonNode readTree(String json) {
        if (json == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (IOException e) {
            log.error("JacksonUtil.readTree-json解析出错: {}", json, e);
            return null;
        }
    }

    private static class TypeResolverBuilder extends ObjectMapper.DefaultTypeResolverBuilder {

        static TypeResolverBuilder forEverything(ObjectMapper mapper) {
            return new TypeResolverBuilder(DefaultTyping.EVERYTHING, mapper.getPolymorphicTypeValidator());
        }

        public TypeResolverBuilder(DefaultTyping typing, PolymorphicTypeValidator polymorphicTypeValidator) {
            super(typing, polymorphicTypeValidator);
        }

        @Override
        public ObjectMapper.DefaultTypeResolverBuilder withDefaultImpl(Class<?> defaultImpl) {
            return this;
        }

        /**
         * Method called to check if the default type handler should be used for given type. Note: "natural types" (String, Boolean, Integer, Double) will never use typing; that is both due to them being
         * concrete and final, and since actual serializers and deserializers will also ignore any attempts to enforce typing.
         */
        public boolean useForType(JavaType javaType) {

            if (javaType.isJavaLangObject()) {
                return true;
            }

            if (javaType.isArrayType() || javaType.isContainerType()) {
                return false;
            }

            javaType = resolveArrayOrWrapper(javaType);

            if (javaType.isEnumType() || ClassUtils.isPrimitiveOrWrapper(javaType.getRawClass())) {
                return false;
            }

            if (javaType.isFinal() && !KotlinDetector.isKotlinType(javaType.getRawClass()) && javaType.getRawClass().getPackageName().startsWith("java")) {
                return false;
            }

            // [databind#88] Should not apply to JSON tree models:
            return !TreeNode.class.isAssignableFrom(javaType.getRawClass());
        }

        private JavaType resolveArrayOrWrapper(JavaType type) {

            while (type.isArrayType()) {
                type = type.getContentType();
                if (type.isReferenceType()) {
                    type = resolveArrayOrWrapper(type);
                }
            }

            while (type.isReferenceType()) {
                type = type.getReferencedType();
                if (type.isArrayType()) {
                    type = resolveArrayOrWrapper(type);
                }
            }

            return type;
        }
    }

    // 序列化：Duration → 秒
    public static class DurationToSecondsSerializer extends JsonSerializer<Duration> {
        @Override
        public void serialize(Duration value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeNumber(value.getSeconds());
        }
    }

    // 反序列化：秒 → Duration
    public static class DurationFromSecondsDeserializer extends JsonDeserializer<Duration> {
        @Override
        public Duration deserialize(com.fasterxml.jackson.core.JsonParser p, DeserializationContext ctxt) throws IOException {
            return Duration.ofSeconds(NumberUtil.parseLong(p.getValueAsString()));
        }
    }
}
