package com.byteasy.platform.service.impl;

import com.byteasy.platform.entity.Openapi;
import com.byteasy.platform.entity.OpenapiLog;
import com.byteasy.platform.service.IOpenapiLogService;
import lombok.extern.log4j.Log4j2;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class OpenapiReplayService {

    @Autowired
    private IOpenapiLogService openapiLogService;

    // 重放专用 OkHttpClient
    private static final OkHttpClient REPLAY_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .build();

    // 需由 OkHttp 自动维护的 Header，避免重复定义产生冲突
    private static final Set<String> EXCLUDE_HEADERS = Set.of(
            "content-length", "host", "connection", "accept-encoding"
    );

    /**
     * 重放指定日志对应的接口
     *
     * @param logId      接口执行日志ID
     * @param targetHost 目标服务地址（如 "http://127.0.0.1:8080"），为 null 则默认本地
     * @return 目标接口返回的真实响应体字符串（业务切面会自动生成一条新的日志记录）
     */
    public String replay(Long logId, String targetHost) throws IOException {
        OpenapiLog logEntity = openapiLogService.getById(logId);
        if (logEntity == null) {
            throw new IllegalArgumentException("未找到对应的接口调用日志记录, id: " + logId);
        }

        // 1. 拼接目标完整 URL
        String baseUrl = StringUtils.isNotBlank(targetHost) ? targetHost.replaceAll("/+$", "") : "http://127.0.0.1:8080";
        String fullUrl = baseUrl + logEntity.getUri();
        if (StringUtils.isNotBlank(logEntity.getQueryString())) {
            fullUrl += (fullUrl.contains("?") ? "&" : "?") + logEntity.getQueryString();
        }

        // 2. 还原 RequestBody
        Openapi.HttpMethod httpMethod = logEntity.getHttpMethod();
        String methodStr = httpMethod != null ? httpMethod.name() : "GET";
        RequestBody requestBody = buildRequestBody(methodStr, logEntity.getContentType(), logEntity.getHttpBody());

        // 3. 构建 OkHttp Request，还原 Headers
        Request.Builder requestBuilder = new Request.Builder()
                .url(fullUrl)
                .method(methodStr, requestBody);

        Map<String, String[]> headers = logEntity.getHttpHeader();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String[]> entry : headers.entrySet()) {
                String headerName = entry.getKey();
                if (headerName == null || EXCLUDE_HEADERS.contains(headerName.toLowerCase())) {
                    continue;
                }
                String[] values = entry.getValue();
                if (values != null) {
                    for (String val : values) {
                        if (val != null) {
                            requestBuilder.addHeader(headerName, val);
                        }
                    }
                }
            }
        }

        // 🔥 注入重放溯源标识：传递给被调接口，日志切面会记录下来，证明是由哪条日志重放来的
        requestBuilder.header("X-Openapi-Replay", "true");
        requestBuilder.header("X-Replay-Source-Id", String.valueOf(logId));

        // 4. 发起真实请求（被调用的业务接口会自动触发 OpenapiLogAspect，写入一条全新的执行日志！）
        try (Response response = REPLAY_CLIENT.newCall(requestBuilder.build()).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            log.info("【接口重放完成】sourceLogId={}, method={}, url={}, statusCode={}",
                    logId, methodStr, fullUrl, response.code());
            return responseBody;
        }
    }

    private RequestBody buildRequestBody(String httpMethod, String contentType, String httpBody) {
        boolean permitsBody = HttpMethod.permitsRequestBody(httpMethod);
        boolean requiresBody = HttpMethod.requiresRequestBody(httpMethod);

        if (!permitsBody) {
            return null;
        }

        MediaType mediaType = MediaType.parse(StringUtils.isNotBlank(contentType)
                ? contentType
                : "application/json; charset=utf-8");

        if (StringUtils.isNotBlank(httpBody)) {
            return RequestBody.create(httpBody, mediaType);
        }

        if (requiresBody) {
            return RequestBody.create("", mediaType);
        }

        return null;
    }
}
