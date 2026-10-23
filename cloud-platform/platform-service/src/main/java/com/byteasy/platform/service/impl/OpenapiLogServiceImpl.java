package com.byteasy.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteasy.Result;
import com.byteasy.common.util.JacksonUtilOld;
import com.byteasy.common.util.OkHttpUtil;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.platform.entity.OpenapiLog;
import com.byteasy.platform.mapper.OpenapiLogMapper;
import com.byteasy.platform.service.IOpenapiLogService;
import lombok.extern.log4j.Log4j2;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.HttpMethod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.byteasy.util.RandomUtil.random;

/**
 * <p>
 * 接口执行日志记录表 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-09-17
 */
@Service
@Log4j2
public class OpenapiLogServiceImpl extends ServiceImpl<OpenapiLogMapper, OpenapiLog> implements IOpenapiLogService {
    @Autowired
    private DiscoveryClient discoveryClient;

    private static final Set<String> EXCLUDE_HEADERS = Set.of("content-length", "host", "connection", "accept-encoding");

    /**
     * 重放指定日志对应的接口
     *
     * @param logId 接口执行日志ID
     * @return 目标接口返回的真实响应体字符串（业务切面会自动生成一条新的日志记录）
     */
    public Result replay(Long logId) throws IOException {
        OpenapiLog logEntity = getById(logId);
        if (logEntity == null) {
            throw new IllegalArgumentException("未找到对应的接口调用日志记录, id: " + logId);
        }
        List<ServiceInstance> instances = discoveryClient.getInstances(logEntity.getServiceCode());

        if (instances == null || instances.isEmpty()) {
            throw new IllegalStateException("未找到服务 [" + logEntity.getServiceCode() + "] 的任何可用实例，无法重放！");
        }

        ServiceInstance randomService = random(instances);

        // 1. 拼接目标完整 URL
        String fullUrl = randomService.getUri().toString() + logEntity.getUri();
        if (StringUtils.isNotBlank(logEntity.getQueryString())) {
            fullUrl += (fullUrl.contains("?") ? "&" : "?") + logEntity.getQueryString();
        }

        // 2. 还原 RequestBody
        Openapi.HttpMethod httpMethod = logEntity.getHttpMethod();
        String methodStr = httpMethod != null ? httpMethod.name() : "GET";
        RequestBody requestBody = buildRequestBody(methodStr, logEntity.getContentType(), logEntity.getHttpBody());

        // 3. 构建 OkHttp Request，还原 Headers
        Request.Builder requestBuilder = new Request.Builder().url(fullUrl).method(methodStr, requestBody);

        Map<String, String> headers = logEntity.getHttpHeader();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String headerName = entry.getKey();
                if (headerName == null || EXCLUDE_HEADERS.contains(headerName.toLowerCase())) {
                    continue;
                }
                String values = entry.getValue();
                if (values != null) {
                    requestBuilder.addHeader(headerName, values);
                }
            }
        }

        // 🔥 注入重放溯源标识：传递给被调接口，日志切面会记录下来，证明是由哪条日志重放来的
        requestBuilder.header("X-Openapi-Replay", "true");
        requestBuilder.header("X-Replay-Source-Id", String.valueOf(logId));

        // 4. 发起真实请求（被调用的业务接口会自动触发 OpenapiLogAspect，写入一条全新的执行日志！）
        try (Response response = OkHttpUtil.getDefaultClient().newCall(requestBuilder.build()).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            log.info("【接口重放完成】sourceLogId={}, method={}, url={}, statusCode={}, responseBody={}", logId, methodStr, fullUrl, response.code(), responseBody);
            Result result = JacksonUtilOld.parse(responseBody, Result.class);
            if (result.isSuccess()) {
                return Result.success();
            } else {
                return Result.error(result.getDescription());
            }
        }
    }

    private RequestBody buildRequestBody(String httpMethod, String contentType, String httpBody) {
        boolean permitsBody = HttpMethod.permitsRequestBody(httpMethod);
        boolean requiresBody = HttpMethod.requiresRequestBody(httpMethod);

        if (!permitsBody) {
            return null;
        }

        MediaType mediaType = MediaType.parse(StringUtils.isNotBlank(contentType) ? contentType : "application/json; charset=utf-8");

        if (StringUtils.isNotBlank(httpBody)) {
            return RequestBody.create(httpBody, mediaType);
        }

        if (requiresBody) {
            return RequestBody.create("", mediaType);
        }

        return null;
    }
}
