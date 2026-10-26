package com.byteasy.script.function;

import cn.hutool.core.codec.Base64;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import com.byteasy.Constant;
import com.byteasy.common.component.OpenApiLoadingCache;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.common.token.UserInfoBuilder;
import com.byteasy.util.HttpUtil;
import com.byteasy.util.JacksonUtil2;
import com.byteasy.exception.BizException;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.script.JsFunction;
import com.byteasy.script.service.IJsScriptApiService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.byteasy.util.RandomUtil.random;

@Component
@Log4j2
public class ExecuteOpenapiFunction implements JsFunction {
    @Autowired
    private IJsScriptApiService jsScriptApiService;
    @Autowired
    private OpenApiLoadingCache openApiLoadingCache;

    @Override
    public String group() {
        return "basic";
    }

    @Override
    public String name() {
        return "executeOpenapi";
    }

    @Override
    public String description() {
        return """
                Execute openapi function.
                函数原型:
                Result runScript(String apiCode, Map<String, Object> params, Map<String, String> headers, String body) throws ExecutionException;
                """;
    }

    @Override
    public Object run(Object[] args) throws Exception {
        if (args == null || args.length == 0) return null;
        String apiCode = args[0].toString();
        Openapi openapi = openApiLoadingCache.load(apiCode);
        if (openapi == null) {
            throw new BizException("未找到Openapi定义: " + apiCode);
        }
        if (Openapi.Type.SCRIPT.equals(openapi.getType())) {
            return jsScriptApiService.runScript(apiCode, (Map<String, Object>) args[1], (Map<String, String>) args[2], (String) args[3]);
        } else {
            try {
                DiscoveryClient discoveryClient = SpringUtil.getBean(DiscoveryClient.class);
                List<ServiceInstance> instances = discoveryClient.getInstances(openapi.getServiceCode());
                Map<String, String> header = new HashMap<>();
                UserInfo token = TokenUtil.getUserinfo();
                if (token != null) {
                    header.put(Constant.USER_INFO, Base64.encode(JacksonUtil2.toJson(token)));
                } else {
                    header.put(Constant.USER_INFO, UserInfoBuilder.builderByMicroService(SpringUtil.getApplicationName(), SpringUtil.getApplicationName()));
                }
                ServiceInstance randomService = random(instances);
                String url = randomService.getUri().toString() + openapi.getUri();
                try {
                    HttpUtil.RequestBuilder requestBuilder = null;
                    if (Openapi.HttpMethod.GET.equals(openapi.getHttpMethod())) {
                        requestBuilder = HttpUtil.get(url).headers(header);
                    } else if (Openapi.HttpMethod.POST.equals(openapi.getHttpMethod())) {
                        requestBuilder = HttpUtil.post(url).headers(header).textBody((String) args[2]);
                    } else if (Openapi.HttpMethod.PUT.equals(openapi.getHttpMethod())) {
                        requestBuilder = HttpUtil.put(url).headers(header).textBody((String) args[2]);
                    } else if (Openapi.HttpMethod.DELETE.equals(openapi.getHttpMethod())) {
                        requestBuilder = HttpUtil.delete(url).headers(header).textBody((String) args[2]);
                    }
                    String result = requestBuilder.executeForString();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status", 0) == 200) {
                        return jsonObject.get("data");
                    } else {
                        log.error("apiCode: {}, error: {}", apiCode, result);
                        return null;
                    }
                } catch (Exception e) {
                    log.error("executeOpenapi remote call err", e);
                    throw new BizException(e.getMessage());
                }
            } catch (Exception e1) {
                log.error("executeOpenapi run err", e1);
                throw new BizException(e1.getMessage());
            }
        }
    }
}
