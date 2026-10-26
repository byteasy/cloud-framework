package com.byteasy.util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.*;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HttpUtilTest {

    private static HttpServer server;
    private static int port;
    private static String baseUrl;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // ==================== 测试 DTO 模型 ====================

    public static class TestUser {
        private Long id;
        private String name;
        private Integer age;

        public TestUser() {
        }

        public TestUser(Long id, String name, Integer age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestUser testUser = (TestUser) o;
            return Objects.equals(id, testUser.id) && Objects.equals(name, testUser.name) && Objects.equals(age, testUser.age);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, age);
        }
    }

    public static class ApiResponse<T> {
        private int code;
        private String message;
        private T data;

        public ApiResponse() {
        }

        public ApiResponse(int code, String message, T data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }

    // ==================== 本地轻量级 HTTP 服务端生命周期 ====================

    @BeforeAll
    static void startMockServer() throws IOException {
        // 随机选择可用本地端口启动，避免端口冲突
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        port = server.getAddress().getPort();
        baseUrl = "http://127.0.0.1:" + port;

        // 注册测试路由
        server.createContext("/test/get", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
            exchange.getResponseHeaders().set("X-Server-Header", "Byteasy-OK");
            String response = "Query: " + query + ", Auth: " + authHeader;
            sendResponse(exchange, 200, response);
        });

        server.createContext("/test/post-json", exchange -> {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            TestUser user = MAPPER.readValue(body, TestUser.class);
            user.setName(user.getName() + "_Processed");
            sendResponse(exchange, 200, MAPPER.writeValueAsString(user));
        });

        server.createContext("/test/generic-response", exchange -> {
            TestUser user = new TestUser(1001L, "Alice", 28);
            ApiResponse<TestUser> apiResponse = new ApiResponse<>(0, "success", user);
            sendResponse(exchange, 200, MAPPER.writeValueAsString(apiResponse));
        });

        server.createContext("/test/form", exchange -> {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            sendResponse(exchange, 200, "ReceivedForm:" + body);
        });

        server.createContext("/test/put", exchange -> {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            sendResponse(exchange, 200, "PUT_OK:" + body);
        });

        server.createContext("/test/delete", exchange -> {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            sendResponse(exchange, 200, "DELETE_OK:" + body);
        });

        server.createContext("/test/patch", exchange -> {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            sendResponse(exchange, 200, "PATCH_OK:" + body);
        });

        server.createContext("/test/status/404", exchange -> sendResponse(exchange, 404, "Not Found"));
        server.createContext("/test/status/500", exchange -> sendResponse(exchange, 500, "Internal Server Error"));

        server.setExecutor(null); // 使用默认单线程调度
        server.start();
    }

    @AfterAll
    static void stopMockServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        byte[] bytes = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    // ==================== 1. URL 拼接与参数构建测试 ====================

    @Test
    @Order(1)
    @DisplayName("测试 URL Query 参数拼接与中文 URL 编码")
    void testQueryParamEncoding() {
        HttpRequest request = HttpUtil.get("https://example.com/api")
                .query("keyword", "中文 测试&+")
                .query("page", 1)
                .buildHttpRequest();

        String uri = request.uri().toString();
        assertTrue(uri.contains("page=1"));
        assertTrue(uri.contains("keyword=%E4%B8%AD%E6%96%87+%E6%B5%8B%E8%AF%95%26%2B"));
    }

    @Test
    @Order(2)
    @DisplayName("测试 Map 以及 List 集合类 Query 参数展开")
    void testCollectionQueryParams() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("role", "admin");
        params.put("ids", List.of(101, 102, 103));

        HttpRequest request = HttpUtil.get("https://example.com/api?existing=true")
                .queryParams(params)
                .buildHttpRequest();

        String uri = request.uri().toString();
        assertTrue(uri.startsWith("https://example.com/api?existing=true&"));
        assertTrue(uri.contains("role=admin"));
        assertTrue(uri.contains("ids=101&ids=102&ids=103"));
    }

    // ==================== 2. Headers 与 认证测试 ====================

    @Test
    @Order(3)
    @DisplayName("测试 Header 设置与 Bearer 认证注入")
    void testHeadersAndBearerAuth() {
        HttpRequest request = HttpUtil.get("https://example.com/api")
                .header("X-Custom-Id", "123456")
                .headers(Map.of("Env", "test"))
                .bearerAuth("my-secret-token")
                .timeout(Duration.ofSeconds(10))
                .buildHttpRequest();

        assertEquals("123456", request.headers().firstValue("X-Custom-Id").orElse(null));
        assertEquals("test", request.headers().firstValue("Env").orElse(null));
        assertEquals("Bearer my-secret-token", request.headers().firstValue("Authorization").orElse(null));
        assertEquals(Duration.ofSeconds(10), request.timeout().orElse(null));
    }

    // ==================== 3. 真实网络交互：GET 请求 ====================

    @Test
    @Order(4)
    @DisplayName("测试真实 GET 请求与 HttpResult 状态判断")
    void testGetExecution() {
        HttpResult<String> result = HttpUtil.get(baseUrl + "/test/get")
                .query("name", "byteasy")
                .bearerAuth("jwt-token-999")
                .execute();

        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
        assertTrue(result.getData().contains("Query: name=byteasy"));
        assertTrue(result.getData().contains("Auth: Bearer jwt-token-999"));
        assertEquals("Byteasy-OK", result.getHeader("X-Server-Header").orElse(null));
        assertTrue(result.getHeaderMap().containsKey("X-server-header") || result.getHeaderMap().containsKey("X-Server-Header"));
    }

    @Test
    @Order(5)
    @DisplayName("测试 executeForString 快捷方法")
    void testExecuteForString() {
        String body = HttpUtil.get(baseUrl + "/test/get")
                .query("k", "v")
                .executeForString();

        assertNotNull(body);
        assertTrue(body.contains("Query: k=v"));
    }

    // ==================== 4. POST JSON 与对象反序列化 ====================

    @Test
    @Order(6)
    @DisplayName("测试 POST JSON 请求并反序列化为实体 Class<T>")
    void testPostJsonWithClassDeserialization() {
        TestUser sendUser = new TestUser(101L, "Tom", 20);

        HttpResult<TestUser> result = HttpUtil.post(baseUrl + "/test/post-json")
                .jsonBody(sendUser)
                .execute(TestUser.class);

        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
        assertNotNull(result.getData());
        assertEquals(101L, result.getData().getId());
        assertEquals("Tom_Processed", result.getData().getName());
        assertEquals(20, result.getData().getAge());
    }

    @Test
    @Order(7)
    @DisplayName("测试 POST 请求并反序列化为复杂泛型 TypeReference<ApiResponse<T>>")
    void testPostWithGenericTypeReference() {
        HttpResult<ApiResponse<TestUser>> result = HttpUtil.post(baseUrl + "/test/generic-response")
                .jsonBody(Map.of())
                .execute(new TypeReference<ApiResponse<TestUser>>() {
                });

        assertTrue(result.isSuccess());
        ApiResponse<TestUser> response = result.getData();
        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertEquals("success", response.getMessage());

        TestUser user = response.getData();
        assertNotNull(user);
        assertEquals(1001L, user.getId());
        assertEquals("Alice", user.getName());
        assertEquals(28, user.getAge());
    }

    // ==================== 5. 表单提交 (application/x-www-form-urlencoded) ====================

    @Test
    @Order(8)
    @DisplayName("测试 Form 表单 URL 编码传参")
    void testFormBody() {
        Map<String, Object> form = new LinkedHashMap<>();
        form.put("username", "admin&root");
        form.put("password", "123 456");

        HttpResult<String> result = HttpUtil.post(baseUrl + "/test/form")
                .formBody(form)
                .execute();

        assertTrue(result.isSuccess());
        assertTrue(result.getData().contains("username=admin%26root"));
        assertTrue(result.getData().contains("password=123+456"));
    }

    // ==================== 6. PUT / DELETE / PATCH 请求 ====================

    @Test
    @Order(9)
    @DisplayName("测试 PUT 请求带 JSON Body")
    void testPutRequest() {
        HttpResult<String> result = HttpUtil.put(baseUrl + "/test/put")
                .textBody("Sample Put Data")
                .execute();

        assertTrue(result.isSuccess());
        assertEquals("PUT_OK:Sample Put Data", result.getData());
    }

    @Test
    @Order(10)
    @DisplayName("测试 DELETE 请求（无 Body 与 带 Body）")
    void testDeleteRequest() {
        // 1. 无 Body 的 DELETE
        HttpResult<String> emptyDelete = HttpUtil.delete(baseUrl + "/test/delete").execute();
        assertTrue(emptyDelete.isSuccess());
        assertEquals("DELETE_OK:", emptyDelete.getData());

        // 2. 带 JSON Body 的 DELETE
        HttpResult<String> bodyDelete = HttpUtil.delete(baseUrl + "/test/delete")
                .jsonBody(Map.of("id", 100))
                .execute();
        assertTrue(bodyDelete.isSuccess());
        assertTrue(bodyDelete.getData().contains("\"id\":100"));
    }

    @Test
    @Order(11)
    @DisplayName("测试 PATCH 请求")
    void testPatchRequest() {
        HttpResult<String> result = HttpUtil.patch(baseUrl + "/test/patch")
                .textBody("Patch Content")
                .execute();

        assertTrue(result.isSuccess());
        assertEquals("PATCH_OK:Patch Content", result.getData());
    }

    // ==================== 7. 二进制 Byte 数组与临时文件 Body ====================

    @Test
    @Order(12)
    @DisplayName("测试 byteBody 与 fileBody 传参")
    void testByteAndFileBody() throws IOException {
        // 1. 测试 byteBody
        byte[] bytes = "Byte Content".getBytes(StandardCharsets.UTF_8);
        HttpResult<String> byteResult = HttpUtil.post(baseUrl + "/test/put")
                .byteBody(bytes, "application/octet-stream")
                .execute();
        assertTrue(byteResult.getData().contains("Byte Content"));

        // 2. 测试 fileBody
        Path tempFile = Files.createTempFile("http-util-test", ".txt");
        try {
            Files.writeString(tempFile, "File Content Test");
            HttpResult<String> fileResult = HttpUtil.post(baseUrl + "/test/put")
                    .fileBody(tempFile, "text/plain")
                    .execute();
            assertTrue(fileResult.getData().contains("File Content Test"));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    // ==================== 8. 异步执行 ====================

    @Test
    @Order(13)
    @DisplayName("测试 executeAsync 异步执行 CompletableFuture")
    void testAsyncExecution() throws Exception {
        CompletableFuture<HttpResult<String>> future = HttpUtil.get(baseUrl + "/test/get")
                .query("async", "true")
                .executeAsync();

        assertNotNull(future);
        HttpResult<String> result = future.get(); // 阻塞等待异步完成
        assertTrue(result.isSuccess());
        assertTrue(result.getData().contains("Query: async=true"));
    }

    // ==================== 9. 状态码与 HttpResult 边界测试 ====================

    @Test
    @Order(14)
    @DisplayName("测试 404 与 500 错误状态响应")
    void testErrorStatuses() {
        HttpResult<String> res404 = HttpUtil.get(baseUrl + "/test/status/404").execute();
        assertFalse(res404.isSuccess());
        assertEquals(404, res404.getStatusCode());
        assertEquals("Not Found", res404.getData());

        HttpResult<String> res500 = HttpUtil.get(baseUrl + "/test/status/500").execute();
        assertFalse(res500.isSuccess());
        assertEquals(500, res500.getStatusCode());
        assertEquals("Internal Server Error", res500.getData());
    }

    @Test
    @Order(15)
    @DisplayName("测试 HttpResult 静态工厂与 Optional Header 方法")
    void testHttpResultHelpers() {
        HttpResult<String> result = HttpResult.of(200, null, "data");
        assertTrue(result.isSuccess());
        assertEquals("data", result.getData());
        assertFalse(result.getHeader("Non-Existing").isPresent());
        assertTrue(result.getHeaderMap().isEmpty());
    }
}
