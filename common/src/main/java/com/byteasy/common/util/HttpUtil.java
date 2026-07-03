package com.byteasy.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpResponse;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * HTTP请求工具类
 *
 * @Author 131232
 * @Date 2025/3/7 13:20
 */
@Deprecated
public class HttpUtil {

    /**
     * get方法
     *
     * @param requestUrl 请求路径
     * @param headers    请求头
     * @return 响应
     */
    public static String sendGet(String requestUrl, Map<String, String> headers) {
        HttpResponse response = cn.hutool.http.HttpUtil.createGet(requestUrl).addHeaders(headers).execute();
        return response.body();
    }

    /**
     * post方法
     *
     * @param body       请求体
     * @param requestUrl 请求路径
     * @param headers    请求头
     * @return 响应
     */
    public static String sendPost(String body, String requestUrl, Map<String, String> headers) {
        HttpResponse response = cn.hutool.http.HttpUtil.createPost(requestUrl).addHeaders(headers).body(body).execute();
        return response.body();
    }


    /**
     * 发送Post请求
     *
     * @param url
     * @param headers
     * @param body
     * @param <T>
     * @return
     */
    public static <T> String sendPost(String url, Map<String, String> headers, T body) {
        return sendPost(url, headers, body, false);
    }

    /**
     * 发送Post请求
     *
     * @param body
     * @param url
     * @param headers
     * @param <T>
     * @return
     */
    public static <T> String sendPost(String url, Map<String, String> headers, T body, boolean form) {
        if (form) {
            return cn.hutool.http.HttpUtil.createPost(url).addHeaders(headers).body(cn.hutool.http.HttpUtil.toParams(BeanUtil.beanToMap(body))).execute().body();
        } else {
            return cn.hutool.http.HttpUtil.createPost(url).addHeaders(headers).body(JacksonUtil.toJson(body)).execute().body();
        }
    }

    /**
     * 请求头数据组装
     *
     * @return map
     */
    public static Map<String, String> getBaseHeaders() {
        Map<String, String> headers = Maps.newHashMap();
        headers.put("Accept-Encoding", "gzip,deflate");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("Connection", "Keep-Alive");
        headers.put("User-Agent", "Apache-HttpClient/4.5.5 (Java/21)");
        return headers;
    }
}
