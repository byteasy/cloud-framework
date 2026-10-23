package com.byteasy.common.util;

import com.byteasy.dto.FormatMap;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.ClassUtils;
import tools.jackson.core.*;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.*;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalTimeSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;
import tools.jackson.databind.jsontype.impl.DefaultTypeResolverBuilder;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.FilterProvider;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Log4j2
public class JacksonUtil {

    private static final JsonMapper OBJECT_MAPPER;
    private static final JsonMapper OBJECT_WITH_CLASS_MAPPER;

    static {
        // 1. 使用 JsonMapper.builder() 构建规范的 Jackson 3 实例
        JsonMapper.Builder builder = JsonMapper.builder()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN)
                .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
                .defaultTimeZone(TimeZone.getDefault())
                .enable(EnumFeature.WRITE_ENUMS_USING_TO_STRING)
                .enable(EnumFeature.READ_ENUMS_USING_TO_STRING)
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 注册模块
        builder.addModule(customJavaTimeModule());
        builder.addModule(formatMapModule());

        OBJECT_MAPPER = builder.build();

        JsonMapper.Builder classBuilder = OBJECT_MAPPER.rebuild();
        // 配置多态类型安全验证器：必须明确允许你的业务包名和基础集合类
        BasicPolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Object.class)            // 允许基类为 Object
                .allowIfSubType(Object[].class)           // 允许对象数组
                .allowIfSubType("com.byteasy.")           // 允许你公司/工程的所有包名
                .allowIfSubType("java.util.")             // 允许常用集合
                .build();

        classBuilder.polymorphicTypeValidator(ptv);

        // ✅ 关键修复：直接传入构建好的 ptv，而不是传未配置验证器的 OBJECT_MAPPER
        CustomTypeResolverBuilder typer = CustomTypeResolverBuilder.forEverything(ptv);
        classBuilder.setDefaultTyping(typer);
        OBJECT_WITH_CLASS_MAPPER = classBuilder.build();
    }

    public static SimpleModule customJavaTimeModule() {
        SimpleModule module = new SimpleModule("CustomJavaTimeModule");

        // 1. LocalDateTime 默认格式
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        // 2. LocalDate 默认格式
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        module.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        // 3. LocalTime 默认格式
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

        // 4. Duration 序列化器
        module.addSerializer(Duration.class, new DurationToSecondsSerializer());
        module.addDeserializer(Duration.class, new DurationFromSecondsDeserializer());

        return module;
    }

    public static SimpleModule formatMapModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(FormatMap.class, new FormatMapSerializer());
        module.addDeserializer(FormatMap.class, new FormatMapDeserializer());
        return module;
    }

    public static JsonMapper copy() {
        return OBJECT_MAPPER;
    }

    public static JsonMapper copyMapperWithClass() {
        return OBJECT_WITH_CLASS_MAPPER;
    }

    public static String toJson(Object obj) {
        return toJson(obj, false);
    }

    public static String toJson(Object obj, boolean withClass) {
        if (obj == null) return null;
        if (obj instanceof String || obj instanceof Number) {
            return obj.toString();
        }
        try {
            return withClass ? OBJECT_WITH_CLASS_MAPPER.writeValueAsString(obj)
                    : OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("JacksonUtil.toJson-json序列化出错: {}", obj, e);
            return null;
        }
    }

    public static String toJson(Object obj, FilterProvider filters) {
        return toJson(obj, filters, false);
    }

    public static String toJson(Object obj, FilterProvider filters, boolean withClass) {
        if (obj == null) return null;
        if (obj instanceof String || obj instanceof Number) {
            return obj.toString();
        }
        try {
            ObjectMapper mapper = withClass ? OBJECT_WITH_CLASS_MAPPER : OBJECT_MAPPER;
            return mapper.writer(filters).writeValueAsString(obj);
        } catch (Exception e) {
            log.error("JacksonUtil.toJson-json序列化出错: {}", obj, e);
            return null;
        }
    }

    public static <T> T parse(String json, Class<T> tClass) {
        if (json == null) return null;
        try {
            return OBJECT_MAPPER.readValue(json, tClass);
        } catch (Exception e) {
            log.error("JacksonUtil.parse-json解析出错: {}", json, e);
            return null;
        }
    }

    public static Object parseObj(String json) {
        if (json == null) return null;
        try {
            return OBJECT_WITH_CLASS_MAPPER.readValue(json, Object.class);
        } catch (Exception e) {
            log.error("JacksonUtil.parse-json解析出错: {}", json, e);
            return null;
        }
    }

    public static List<Object> parseObjList(String json) {
        if (json == null) return Collections.emptyList();
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, Object.class);
            return OBJECT_WITH_CLASS_MAPPER.readValue(json, javaType);
        } catch (Exception e) {
            log.error("JacksonUtil.parse-json解析出错: {}", json, e);
            return Collections.emptyList();
        }
    }

    public static Object[] parseObjArray(String json) {
        if (json == null) return new Object[0];
        List<Object> list = parseObjList(json);
        return list.toArray(new Object[0]);
    }

    public static <E> List<E> parseList(String json, Class<E> eClass) {
        return nativeRead(json, List.class, eClass);
    }

    public static <K, V> Map<K, V> parseMap(String json, Class<K> kClass, Class<V> vClass) {
        return nativeRead(json, Map.class, kClass, vClass);
    }

    public static <K, V> LinkedHashMap<K, V> parseLinkedHashMap(String json, Class<K> kClass, Class<V> vClass) {
        return nativeRead(json, LinkedHashMap.class, kClass, vClass);
    }

    public static <T> T nativeRead(String json, TypeReference<T> type) {
        if (json == null) return null;
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (Exception e) {
            log.error("JacksonUtil.nativeRead-json解析出错: {}", json, e);
            return null;
        }
    }

    public static <T> T nativeRead(String json, Class<?> javaClazz, Class<?>... genericClazz) {
        if (json == null) return null;
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(javaClazz, genericClazz);
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (Exception e) {
            log.error("JacksonUtil.nativeRead-json解析出错: ", e);
            return null;
        }
    }

    public static Object parseClazz(String targetStr, Class<?> clazz) {
        if (clazz == String.class) return targetStr;
        if (targetStr == null) return null;
        if (targetStr.startsWith("{")) return parse(targetStr, clazz);
        if (targetStr.startsWith("[")) return parseList(targetStr, clazz);

        if (clazz == Integer.class) return Integer.valueOf(targetStr);
        if (clazz == Long.class) return Long.valueOf(targetStr);
        if (clazz == BigDecimal.class) return new BigDecimal(targetStr);
        if (clazz == Boolean.class) return Boolean.valueOf(targetStr);
        if (clazz == Double.class) return Double.valueOf(targetStr);
        if (clazz == Float.class) return Float.valueOf(targetStr);
        if (clazz == Short.class) return Short.valueOf(targetStr);
        if (clazz == Byte.class) return Byte.valueOf(targetStr);
        return null;
    }

    public static Map<String, Object> obj2Map(Object object) {
        return OBJECT_MAPPER.convertValue(object, new TypeReference<>() {
        });
    }

    public static Map<String, String> obj2StringMap(Object object) {
        return OBJECT_MAPPER.convertValue(object, new TypeReference<>() {
        });
    }

    public static <T> T obj2Val(Object object) {
        return OBJECT_MAPPER.convertValue(object, new TypeReference<T>() {
        });
    }

    public static JsonNode readTree(String json) {
        if (json == null) return null;
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (Exception e) {
            log.error("JacksonUtil.readTree-json解析出错: {}", json, e);
            return null;
        }
    }

    public static JsonMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * Jackson 3 专属的自定义多态类型解析器
     */
    public static class CustomTypeResolverBuilder extends DefaultTypeResolverBuilder implements Serializable {
        private static final long serialVersionUID = 1L;

        public static CustomTypeResolverBuilder forEverything(ObjectMapper mapper) {
            return new CustomTypeResolverBuilder(DefaultTyping.NON_FINAL, mapper.deserializationConfig().getPolymorphicTypeValidator());
        }

        public static CustomTypeResolverBuilder forEverything(PolymorphicTypeValidator polymorphicTypeValidator) {
            return new CustomTypeResolverBuilder(DefaultTyping.NON_FINAL, polymorphicTypeValidator);
        }

        /**
         * 构造函数：直接调用父类的 3 参数构造器
         * 参数 1: Validator
         * 参数 2: DefaultTyping
         * 参数 3: As.PROPERTY
         * (父类会自动将 _idType 设为 Id.CLASS，_typeProperty 设为 "@class"，无需额外调用 init/inclusion)
         */
        public CustomTypeResolverBuilder(DefaultTyping typing, PolymorphicTypeValidator polymorphicTypeValidator) {
            super(polymorphicTypeValidator, typing, JsonTypeInfo.As.PROPERTY);
        }

        /**
         * 拷贝构造函数：供 withDefaultImpl 使用
         */
        protected CustomTypeResolverBuilder(CustomTypeResolverBuilder base, Class<?> defaultImpl) {
            super(base, defaultImpl);
        }

        /**
         * 父类通过 ClassUtil.verifyMustOverride 强制子类必须重写此方法
         */
        @Override
        public CustomTypeResolverBuilder withDefaultImpl(Class<?> defaultImpl) {
            if (this._defaultImpl == defaultImpl) {
                return this;
            }
            return new CustomTypeResolverBuilder(this, defaultImpl);
        }

        @Override
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

            // 排除原生 java 包中的 final 类型（如 String, Integer 等）
            if (javaType.isFinal() && !isKotlinType(javaType.getRawClass()) && javaType.getRawClass().getPackageName().startsWith("java")) {
                return false;
            }

            // 排除 Jackson 原生树节点（如 JsonNode, ObjectNode 等）
            return !TreeNode.class.isAssignableFrom(javaType.getRawClass());
        }

        protected JavaType resolveArrayOrWrapper(JavaType type) {
            while (type.isArrayType()) {
                type = type.getContentType();
                if (type.isReferenceType()) {
                    type = resolveArrayOrWrapper(type);
                }
            }
            return type;
        }

        private static boolean isKotlinType(Class<?> clazz) {
            return clazz.isAnnotationPresent(kotlin.Metadata.class);
        }
    }

    public static class DurationToSecondsSerializer extends ValueSerializer<Duration> {
        @Override
        public void serialize(Duration value, JsonGenerator gen, SerializationContext serializers) throws JacksonException {
            gen.writeNumber(value.getSeconds());
        }
    }

    // 反序列化：秒 → Duration
    public static class DurationFromSecondsDeserializer extends ValueDeserializer<Duration> {
        @Override
        public Duration deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
            return Duration.ofSeconds(Long.parseLong(p.getValueAsString()));
        }
    }
}