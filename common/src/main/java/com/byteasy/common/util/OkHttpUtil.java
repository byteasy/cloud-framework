package com.byteasy.common.util;

import cn.hutool.core.io.FileUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class OkHttpUtil {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");
    private static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private static final MediaType OCTET_STREAM = MediaType.parse("application/octet-stream");

    // 默认客户端
    private static volatile OkHttpClient defaultClient;
    // 自定义客户端缓存
    private static final Map<String, OkHttpClient> clientMap = new ConcurrentHashMap<>();

    static {
        initDefaultClient();
    }

    /**
     * 初始化默认客户端
     */
    private static void initDefaultClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new Log4j2HttpLoggingInterceptor());
        loggingInterceptor.setLevel(Level.BASIC);
        defaultClient = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).retryOnConnectionFailure(true).connectionPool(new ConnectionPool(20, 5, TimeUnit.SECONDS)).addInterceptor(loggingInterceptor).build();
    }

    /**
     * 获取默认客户端
     */
    public static OkHttpClient getDefaultClient() {
        return defaultClient;
    }

    /**
     * 创建自定义配置的客户端
     */
    public static OkHttpClient createClient(OkHttpClient.Builder builder) {
        return builder.build();
    }

    /**
     * 添加自定义客户端到缓存
     */
    public static void addClient(String clientName, OkHttpClient client) {
        clientMap.put(clientName, client);
    }

    /**
     * 从缓存获取自定义客户端
     */
    public static OkHttpClient getClient(String clientName) {
        return clientMap.get(clientName);
    }

    /**
     * 同步GET请求
     */
    public static String get(String url) throws IOException {
        return get(url, null, null);
    }

    public static String get(String url, Map<String, String> headers) throws IOException {
        return get(url, headers, null);
    }

    public static String get(String url, Map<String, String> headers, OkHttpClient client) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        addHeaders(builder, headers);
        Request request = builder.build();
        return execute(request, client);
    }

    public static String get(String url, Map<String, String> headers, Map<String, String> queryParams, OkHttpClient client) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        // 添加查询参数
        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        Request.Builder builder = new Request.Builder().url(urlBuilder.build());
        addHeaders(builder, headers);
        Request request = builder.build();

        return execute(request, client);
    }

    public static String get(String url, Map<String, String> headers, List<Pair<String, String>> queryParams, OkHttpClient client) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        // 添加查询参数
        if (queryParams != null) {
            for (Pair<String, String> entry : queryParams) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }

        Request.Builder builder = new Request.Builder().url(urlBuilder.build());
        addHeaders(builder, headers);
        Request request = builder.build();

        return execute(request, client);
    }

    public static <T> T get(String url, Map<String, String> headers, OkHttpClient client, TypeReference<T> type) throws IOException {
        String response = get(url, headers, client);
        return JacksonUtil.nativeRead(response, type);
    }

    public static <T> T getWithT(String url, Map<String, String> headers, OkHttpClient client, Class<T> cls) throws IOException {
        String response = get(url, headers, client);
        return JacksonUtil.parse(response, cls);
    }

    public static <T> T get(String url, Map<String, String> headers, Map<String, String> queryParams, OkHttpClient client, TypeReference<T> type) throws IOException {
        String response = get(url, headers, queryParams, client);
        return JacksonUtil.nativeRead(response, type);
    }

    public static <T> T get(String url, Map<String, String> headers, List<Pair<String, String>> queryParams, OkHttpClient client, TypeReference<T> type) throws IOException {
        String response = get(url, headers, queryParams, client);
        return JacksonUtil.nativeRead(response, type);
    }

    /**
     * 同步POST请求(表单)
     */
    public static String postForm(String url, Map<String, String> params) throws IOException {
        return postForm(url, params, null, null);
    }

    public static String postForm(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        return postForm(url, params, headers, null);
    }

    public static String postForm(String url, Map<String, String> params, Map<String, String> headers, OkHttpClient client) throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (StringUtils.isNotEmpty(entry.getValue())) {
                    formBuilder.add(entry.getKey(), entry.getValue());
                }
            }
        }

        Request.Builder builder = new Request.Builder().url(url).post(formBuilder.build());

        addHeaders(builder, headers);
        Request request = builder.build();

        return execute(request, client);
    }

    /**
     * 同步POST请求(JSON)
     */
    public static String postJson(String url, String json) throws IOException {
        return postJson(url, json, null, null);
    }

    public static String postJson(String url, String json, Map<String, String> headers) throws IOException {
        return postJson(url, json, headers, null);
    }

    public static String postJson(String url, String json, Map<String, String> headers, OkHttpClient client) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request.Builder builder = new Request.Builder().url(url).post(body);

        addHeaders(builder, headers);
        Request request = builder.build();

        return execute(request, client);
    }

    /**
     * 复杂对象反序列化
     *
     * @param url
     * @param json
     * @param headers
     * @param client
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T postJsonWithResult(String url, String json, Map<String, String> headers, OkHttpClient client, TypeReference<T> type) throws IOException {
        String response = postJson(url, json, headers, client);
        return JacksonUtil.nativeRead(response, type);
    }

    public static <T> T postJsonWithT(String url, String json, Map<String, String> headers, OkHttpClient client, Class<T> cls) throws IOException {
        String response = postJson(url, json, headers, client);
        return JacksonUtil.parse(response, cls);
    }

    public static String postXml(String url, String xml) throws IOException {
        return postXml(url, xml, null, null);
    }

    public static String postXml(String url, String xml, Map<String, String> headers) throws IOException {
        return postXml(url, xml, headers, null);
    }

    public static String postXml(String url, String xml, Map<String, String> headers, OkHttpClient client) throws IOException {
        RequestBody body = RequestBody.create(xml, XML);
        Request.Builder builder = new Request.Builder().url(url).post(body);

        addHeaders(builder, headers);
        Request request = builder.build();

        return execute(request, client);
    }

    /**
     * 同步POST请求(文件上传)
     */
    public static String upload(String url, String fileParamName, File file) throws IOException {
        return upload(url, fileParamName, file, null, null, null);
    }

    public static String upload(String url, String fileParamName, File file, Map<String, String> params, Map<String, String> headers) throws IOException {
        return upload(url, fileParamName, file, params, headers, null);
    }

    public static String upload(String url, String fileParamName, File file, Map<String, String> params, Map<String, String> headers, OkHttpClient client) throws IOException {
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(fileParamName, file.getName(), RequestBody.create(file, OCTET_STREAM));

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (StringUtils.isNotEmpty(entry.getValue())) {
                    multipartBuilder.addFormDataPart(entry.getKey(), entry.getValue());
                }
            }
        }

        Request.Builder builder = new Request.Builder().url(url).post(multipartBuilder.build());

        addHeaders(builder, headers);
        Request request = builder.build();

        return execute(request, client);
    }

    /**
     * 多文件同时上传
     *
     * @param url
     * @param files
     * @param params
     * @param headers
     * @param client
     * @return
     * @throws IOException
     */
    public static String uploadMultiFiles(String url, List<Pair<String, File>> files, Map<String, String> params, Map<String, String> headers, OkHttpClient client) throws IOException {
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (Pair<String, File> item : files) {
            multipartBuilder.addFormDataPart(item.getLeft(), item.getRight().getName(), RequestBody.create(item.getRight(), OCTET_STREAM));
        }

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (StringUtils.isNotEmpty(entry.getValue())) {
                    multipartBuilder.addFormDataPart(entry.getKey(), entry.getValue());
                }
            }
        }

        Request.Builder builder = new Request.Builder().url(url).post(multipartBuilder.build());

        addHeaders(builder, headers);
        Request request = builder.build();

        return execute(request, client);
    }

    /**
     * 文件下载
     */
    public static void download(String url, String savePath) throws IOException {
        download(url, savePath, null, null);
    }

    public static void download(String url, String savePath, Map<String, String> headers) throws IOException {
        download(url, savePath, headers, null);
    }

    public static void download(String url, String savePath, Map<String, String> headers, OkHttpClient client) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        addHeaders(builder, headers);
        Request request = builder.build();

        try (Response response = getClientToUse(client).newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("下载失败，状态码: " + response.code());
            }

            ResponseBody body = response.body();
            if (body != null) {
                FileUtil.writeBytes(Objects.requireNonNull(body.bytes()), new File(savePath));
            }
        }
    }

    public static byte[] downloadToBytes(String url) throws IOException {
        return downloadToBytes(url, null);
    }

    public static byte[] downloadToBytes(String url, Map<String, String> headers) throws IOException {
        return downloadToBytes(url, headers, null);
    }

    public static byte[] downloadToBytes(String url, Map<String, String> headers, OkHttpClient client) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        addHeaders(builder, headers);
        Request request = builder.build();

        try (Response response = getClientToUse(client).newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("下载失败，状态码: " + response.code());
            }

            ResponseBody body = response.body();
            return body.bytes();
        }
    }

    /**
     * 异步GET请求
     */
    public static void asyncGet(String url, Callback callback) {
        asyncGet(url, null, null, callback);
    }

    public static void asyncGet(String url, Map<String, String> headers, Callback callback) {
        asyncGet(url, headers, null, callback);
    }

    public static void asyncGet(String url, Map<String, String> headers, OkHttpClient client, Callback callback) {
        Request.Builder builder = new Request.Builder().url(url);
        addHeaders(builder, headers);
        Request request = builder.build();

        getClientToUse(client).newCall(request).enqueue(callback);
    }

    /**
     * 异步POST请求(JSON)
     */
    public static void asyncPostJson(String url, String json, Callback callback) {
        asyncPostJson(url, json, null, null, callback);
    }

    public static void asyncPostJson(String url, String json, Map<String, String> headers, Callback callback) {
        asyncPostJson(url, json, headers, null, callback);
    }

    public static void asyncPostJson(String url, String json, Map<String, String> headers, OkHttpClient client, Callback callback) {
        RequestBody body = RequestBody.create(json, JSON);
        Request.Builder builder = new Request.Builder().url(url).post(body);

        addHeaders(builder, headers);
        Request request = builder.build();

        getClientToUse(client).newCall(request).enqueue(callback);
    }

    /**
     * 执行请求并返回响应字符串
     */
    private static String execute(Request request, OkHttpClient client) throws IOException {
        try (Response response = getClientToUse(client).newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("请求失败，状态码: " + response.code());
            }
            ResponseBody body = response.body();
            return body != null ? body.string() : null;
        }
    }

    /**
     * 获取要使用的客户端
     */
    private static OkHttpClient getClientToUse(OkHttpClient client) {
        return client != null ? client : defaultClient;
    }

    /**
     * 添加请求头
     */
    private static void addHeaders(Request.Builder builder, Map<String, String> headers) {
        if (headers != null) {
            headers.forEach(builder::addHeader);
        }
    }

    /**
     * 自定义回调适配器
     */
    public static abstract class SimpleCallback implements Callback {

        @Override
        public abstract void onResponse(@NotNull Call call, @NotNull Response response) throws IOException;

        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            e.printStackTrace();
        }
    }
}
