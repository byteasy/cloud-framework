package com.byteasy.platform;

import com.byteasy.common.util.JacksonUtil;
import com.byteasy.platform.api.*;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import tools.jackson.databind.json.JsonMapper;

@Configuration(proxyBeanMethods = false)
public class RestClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public RestClient plateformServiceRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("lb://" + Constant.SERVICE_NAME)
                // ✅ Spring 7 官方推荐写法，替代已废弃的 messageConverters()
                .configureMessageConverters(clientConverters -> {
                    // 注册统一的 Jackson 3 转换器
                    clientConverters.configureMessageConverters(c -> new JacksonJsonHttpMessageConverter((JsonMapper) JacksonUtil.getObjectMapper()));
                })
                // 全局请求拦截器
                .requestInterceptor((request, body, execution) -> {
                    request.getHeaders().add("X-Client-Version", "4.0.0");
                    return execution.execute(request, body);
                })
                .build();
    }

    @Bean
    public DictionaryClient dictionaryClient(RestClient plateformServiceRestClient) {
        RestClientAdapter adapter = RestClientAdapter.create(plateformServiceRestClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(DictionaryClient.class);
    }

    @Bean
    public EntityDefineClient entityDefineClient(RestClient plateformServiceRestClient) {
        RestClientAdapter adapter = RestClientAdapter.create(plateformServiceRestClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(EntityDefineClient.class);
    }

    @Bean
    public FieldDefineClient fieldDefineClient(RestClient plateformServiceRestClient) {
        RestClientAdapter adapter = RestClientAdapter.create(plateformServiceRestClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(FieldDefineClient.class);
    }

    @Bean
    public OpenapiClient openapiClient(RestClient plateformServiceRestClient) {
        RestClientAdapter adapter = RestClientAdapter.create(plateformServiceRestClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(OpenapiClient.class);
    }

    @Bean
    public RoleOpenapiClient roleOpenapiClient(RestClient plateformServiceRestClient) {
        RestClientAdapter adapter = RestClientAdapter.create(plateformServiceRestClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(RoleOpenapiClient.class);
    }

    @Bean
    public ScriptFunctionClient scriptFunctionClient(RestClient plateformServiceRestClient) {
        RestClientAdapter adapter = RestClientAdapter.create(plateformServiceRestClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ScriptFunctionClient.class);
    }

    @Bean
    public UserClient userClient(RestClient plateformServiceRestClient) {
        RestClientAdapter adapter = RestClientAdapter.create(plateformServiceRestClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(UserClient.class);
    }

    @Bean
    public ServiceInstanceClient serviceInstanceClient(RestClient plateformServiceRestClient) {
        RestClientAdapter adapter = RestClientAdapter.create(plateformServiceRestClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ServiceInstanceClient.class);
    }

    @Bean
    public OpenapiLogClient openapiLogClient(RestClient plateformServiceRestClient) {
        RestClientAdapter adapter = RestClientAdapter.create(plateformServiceRestClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(OpenapiLogClient.class);
    }

    @Bean
    public JsScriptClient jsScriptClient(RestClient plateformServiceRestClient) {
        RestClientAdapter adapter = RestClientAdapter.create(plateformServiceRestClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(JsScriptClient.class);
    }
}
