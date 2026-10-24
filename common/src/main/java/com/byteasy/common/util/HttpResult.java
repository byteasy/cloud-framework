package com.byteasy.common.util;

import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * HTTP 响应结果包装类
 *
 * @param <T> 响应体泛型类型
 */
public class HttpResult<T> {

    /**
     * HTTP 状态码（如 200, 404, 500 等）
     */
    private final int statusCode;

    /**
     * HTTP 响应头
     */
    private final HttpHeaders headers;

    /**
     * 反序列化后的响应体数据
     */
    private final T data;

    public HttpResult(int statusCode, HttpHeaders headers, T data) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.data = data;
    }

    public static <T> HttpResult<T> of(int statusCode, HttpHeaders headers, T data) {
        return new HttpResult<>(statusCode, headers, data);
    }

    public static <T> HttpResult<T> of(int statusCode, HttpHeaders headers, T data, String rawBody) {
        return new HttpResult<>(statusCode, headers, data);
    }

    /**
     * 判断 HTTP 状态码是否属于 2xx 成功区间
     */
    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public T getData() {
        return data;
    }

    /**
     * 获取指定响应头的第一条值
     */
    public Optional<String> getHeader(String name) {
        return headers != null ? headers.firstValue(name) : Optional.empty();
    }

    /**
     * 获取响应头 Map
     */
    public Map<String, List<String>> getHeaderMap() {
        return headers != null ? headers.map() : Map.of();
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "statusCode=" + statusCode +
                ", data=" + data +
                '}';
    }
}
