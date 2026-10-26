package com.byteasy.util;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class HttpUtil {

    private static volatile HttpClient httpClient;
    private static volatile ObjectMapper objectMapper;

    static {
        // 全局连接池参数
        System.setProperty("jdk.httpclient.connectionPoolSize", "200");
        System.setProperty("jdk.httpclient.keepalive.timeout", "60");

        // 默认初始化 JDK 21 原生 HttpClient
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(5))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .executor(Executors.newVirtualThreadPerTaskExecutor()) // Java 21 虚拟线程
                .build();

        objectMapper = JacksonUtil.getObjectMapper();
    }

    public static HttpClient getHttpClient() {
        return httpClient;
    }

    // ==================== 快捷入口 ====================

    public static RequestBuilder get(String url) {
        return new RequestBuilder("GET", url);
    }

    public static RequestBuilder post(String url) {
        return new RequestBuilder("POST", url);
    }

    public static RequestBuilder put(String url) {
        return new RequestBuilder("PUT", url);
    }

    public static RequestBuilder delete(String url) {
        return new RequestBuilder("DELETE", url);
    }

    public static RequestBuilder patch(String url) {
        return new RequestBuilder("PATCH", url);
    }

    // ==================== 链式构建器 ====================

    public static class RequestBuilder {
        private final String method;
        private final String baseUrl;
        private final Map<String, List<String>> queryParams = new LinkedHashMap<>();
        private final Map<String, String> headers = new LinkedHashMap<>();
        private Duration timeout = Duration.ofSeconds(15);
        private HttpRequest.BodyPublisher bodyPublisher;

        public RequestBuilder(String method, String url) {
            this.method = method.toUpperCase();
            this.baseUrl = url;
        }

        // ----------- Query 参数 -----------

        public RequestBuilder query(String key, Object value) {
            if (key != null && value != null) {
                this.queryParams.computeIfAbsent(key, k -> new ArrayList<>()).add(value.toString());
            }
            return this;
        }

        public RequestBuilder queryParams(Map<String, ?> params) {
            if (params != null) {
                params.forEach((k, v) -> {
                    if (v instanceof Collection<?> col) {
                        col.forEach(item -> query(k, item));
                    } else {
                        query(k, v);
                    }
                });
            }
            return this;
        }

        // ----------- Header 请求头 -----------

        public RequestBuilder header(String key, String value) {
            if (key != null && value != null) {
                this.headers.put(key, value);
            }
            return this;
        }

        public RequestBuilder headers(Map<String, String> headers) {
            if (headers != null) {
                this.headers.putAll(headers);
            }
            return this;
        }

        public RequestBuilder bearerAuth(String token) {
            return header("Authorization", "Bearer " + token);
        }

        public RequestBuilder timeout(Duration timeout) {
            if (timeout != null) this.timeout = timeout;
            return this;
        }

        // ----------- 请求 Body -----------

        /**
         * 传 JSON 对象（自动使用 Jackson 序列化）
         */
        public RequestBuilder jsonBody(Object body) {
            try {
                String json = (body instanceof String s) ? s : objectMapper.writeValueAsString(body);
                header("Content-Type", "application/json; charset=UTF-8");
                this.bodyPublisher = HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8);
                return this;
            } catch (Exception e) {
                throw new RuntimeException("序列化 JSON 失败", e);
            }
        }

        /**
         * 传 Form 表单 (application/x-www-form-urlencoded)
         */
        public RequestBuilder formBody(Map<String, ?> formMap) {
            header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            if (formMap == null || formMap.isEmpty()) {
                this.bodyPublisher = HttpRequest.BodyPublishers.noBody();
                return this;
            }
            String formStr = formMap.entrySet().stream()
                    .filter(e -> e.getKey() != null && e.getValue() != null)
                    .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" +
                            URLEncoder.encode(e.getValue().toString(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));
            this.bodyPublisher = HttpRequest.BodyPublishers.ofString(formStr, StandardCharsets.UTF_8);
            return this;
        }

        /**
         * 纯文本 Body
         */
        public RequestBuilder textBody(String text) {
            header("Content-Type", "text/plain; charset=UTF-8");
            this.bodyPublisher = (text == null) ? HttpRequest.BodyPublishers.noBody()
                    : HttpRequest.BodyPublishers.ofString(text, StandardCharsets.UTF_8);
            return this;
        }

        /**
         * 二进制 Byte 数组
         */
        public RequestBuilder byteBody(byte[] bytes, String contentType) {
            if (contentType != null) header("Content-Type", contentType);
            this.bodyPublisher = (bytes == null) ? HttpRequest.BodyPublishers.noBody()
                    : HttpRequest.BodyPublishers.ofByteArray(bytes);
            return this;
        }

        /**
         * 文件 Body
         */
        public RequestBuilder fileBody(Path path, String contentType) {
            if (contentType != null) header("Content-Type", contentType);
            try {
                this.bodyPublisher = HttpRequest.BodyPublishers.ofFile(path);
                return this;
            } catch (Exception e) {
                throw new RuntimeException("读取文件失败: " + path, e);
            }
        }

        // ----------- 执行请求 -----------

        /**
         * 构建 HttpRequest 对象
         */
        public HttpRequest buildHttpRequest() {
            String fullUrl = buildFullUrl();
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(fullUrl))
                    .timeout(this.timeout);

            // 设置 Header
            this.headers.forEach(builder::header);

            // 设置 Method & Body
            HttpRequest.BodyPublisher publisher = (this.bodyPublisher != null)
                    ? this.bodyPublisher
                    : HttpRequest.BodyPublishers.noBody();

            switch (this.method) {
                case "GET" -> builder.GET();
                case "DELETE" -> {
                    // JDK HttpClient DELETE 默认不带 body，若有 body 则用 method("DELETE", ...)
                    if (this.bodyPublisher != null) {
                        builder.method("DELETE", publisher);
                    } else {
                        builder.DELETE();
                    }
                }
                case "POST" -> builder.POST(publisher);
                case "PUT" -> builder.PUT(publisher);
                default -> builder.method(this.method, publisher); // PATCH 等
            }

            return builder.build();
        }

        /**
         * 同步发送并返回原生字符串结果
         */
        public HttpResult<String> execute() {
            try {
                HttpResponse<String> response = httpClient.send(buildHttpRequest(), HttpResponse.BodyHandlers.ofString());
                return new HttpResult<>(response.statusCode(), response.headers(), response.body());
            } catch (IOException | InterruptedException e) {
                if (e instanceof InterruptedException) Thread.currentThread().interrupt();
                throw new RuntimeException("HTTP 请求异常: " + baseUrl, e);
            }
        }

        /**
         * 直接获取响应字符串（只关心 body 内容时使用）
         */
        public String executeForString() {
            HttpResult<String> result = execute();
            return result.getData();
        }

        /**
         * 同步发送并自动反序列化为 Java 对象（支持泛型 Class）
         */
        public <T> HttpResult<T> execute(Class<T> clazz) {
            try {
                // 使用 ofInputStream 边读边解，大响应时避免多重内存拷贝
                HttpResponse<InputStream> response = httpClient.send(buildHttpRequest(), HttpResponse.BodyHandlers.ofInputStream());
                try (InputStream is = response.body()) {
                    T data = null;
                    if (response.statusCode() != 204 && is.available() >= 0) {
                        data = objectMapper.readValue(is, clazz);
                    }
                    return new HttpResult<>(response.statusCode(), response.headers(), data);
                }
            } catch (Exception e) {
                throw new RuntimeException("HTTP 请求或解析失败: " + baseUrl, e);
            }
        }

        /**
         * 同步发送并自动反序列化为复杂泛型对象（如 Result<List<User>>）
         */
        public <T> HttpResult<T> execute(TypeReference<T> typeRef) {
            try {
                HttpResponse<InputStream> response = httpClient.send(buildHttpRequest(), HttpResponse.BodyHandlers.ofInputStream());
                try (InputStream is = response.body()) {
                    T data = objectMapper.readValue(is, typeRef);
                    return new HttpResult<>(response.statusCode(), response.headers(), data);
                }
            } catch (Exception e) {
                throw new RuntimeException("HTTP 复杂泛型解析失败: " + baseUrl, e);
            }
        }

        /**
         * 异步执行
         */
        public CompletableFuture<HttpResult<String>> executeAsync() {
            return httpClient.sendAsync(buildHttpRequest(), HttpResponse.BodyHandlers.ofString())
                    .thenApply(res -> new HttpResult<>(res.statusCode(), res.headers(), res.body()));
        }

        // ----------- URL 拼接辅助 -----------

        private String buildFullUrl() {
            if (queryParams.isEmpty()) {
                return baseUrl;
            }
            String queryString = queryParams.entrySet().stream()
                    .flatMap(e -> e.getValue().stream()
                            .map(v -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" +
                                    URLEncoder.encode(v, StandardCharsets.UTF_8)))
                    .collect(Collectors.joining("&"));

            return baseUrl.contains("?") ? baseUrl + "&" + queryString : baseUrl + "?" + queryString;
        }
    }
}