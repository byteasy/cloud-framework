package com.byteasy.platform.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.byteasy.Result;
import com.byteasy.util.HttpResult;
import com.byteasy.util.HttpUtil;
import com.byteasy.util.JacksonUtil;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.platform.entity.OpenapiLog;
import com.byteasy.platform.mapper.OpenapiLogMapper;
import com.byteasy.platform.service.IOpenapiLogService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
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

        HttpUtil.RequestBuilder builder = null;
        switch (httpMethod) {
            case GET -> builder = HttpUtil.get(fullUrl);
            case PUT -> builder = HttpUtil.put(fullUrl);
            case DELETE -> builder = HttpUtil.delete(fullUrl);
            case POST -> builder = HttpUtil.post(fullUrl);
        }
        if (StringUtils.isNotBlank(logEntity.getHttpBody())) {
            builder.textBody(logEntity.getHttpBody());
        }
        builder.header(HttpHeaders.CONTENT_TYPE, logEntity.getContentType());


        Map<String, String> headers = logEntity.getHttpHeader();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String headerName = entry.getKey();
                if (headerName == null || EXCLUDE_HEADERS.contains(headerName.toLowerCase())) {
                    continue;
                }
                String values = entry.getValue();
                if (values != null) {
                    builder.header(headerName, values);
                }
            }
        }

        builder.header("X-Openapi-Replay", "true");
        builder.header("X-Replay-Source-Id", String.valueOf(logId));

        // 4. 发起真实请求（被调用的业务接口会自动触发 OpenapiLogAspect，写入一条全新的执行日志！）
        HttpResult<String> response = builder.execute();
        String responseBody = response.getData();
        log.info("【接口重放完成】sourceLogId={}, method={}, url={}, statusCode={}, responseBody={}", logId, methodStr, fullUrl, response.getStatusCode(), responseBody);
        Result result = JacksonUtil.parse(responseBody, Result.class);
        if (result.isSuccess()) {
            return Result.success();
        } else {
            return Result.error(result.getDescription());
        }
    }
}
