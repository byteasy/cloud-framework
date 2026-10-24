package com.byteasy.platform;

import com.byteasy.common.configuration.RestClientHeaderInterceptor;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.platform.api.*;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration(proxyBeanMethods = false)
public class RestClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public RestClient plateformServiceRestClient(RestClient.Builder builder, RestClientHeaderInterceptor restClientHeaderInterceptor) {
        return builder
                .baseUrl("lb://" + Constant.SERVICE_NAME)
                .configureMessageConverters(clientConverters -> {
                    clientConverters.addCustomConverter(new JacksonJsonHttpMessageConverter(JacksonUtil.getObjectMapper()));
                })
                .requestInterceptor(restClientHeaderInterceptor)
                .build();
    }

    @Bean
    public HttpServiceProxyFactory platformServiceProxyFactory(RestClient platformServiceRestClient) {
        RestClientAdapter adapter = RestClientAdapter.create(platformServiceRestClient);
        return HttpServiceProxyFactory.builderFor(adapter).build();
    }

    @Bean
    public DictionaryClient dictionaryClient(HttpServiceProxyFactory factory) {
        return factory.createClient(DictionaryClient.class);
    }

    @Bean
    public EntityDefineClient entityDefineClient(HttpServiceProxyFactory factory) {
        return factory.createClient(EntityDefineClient.class);
    }

    @Bean
    public FieldDefineClient fieldDefineClient(HttpServiceProxyFactory factory) {
        return factory.createClient(FieldDefineClient.class);
    }

    @Bean
    public OpenapiClient openapiClient(HttpServiceProxyFactory factory) {
        return factory.createClient(OpenapiClient.class);
    }

    @Bean
    public RoleOpenapiClient roleOpenapiClient(HttpServiceProxyFactory factory) {
        return factory.createClient(RoleOpenapiClient.class);
    }

    @Bean
    public ScriptFunctionClient scriptFunctionClient(HttpServiceProxyFactory factory) {
        return factory.createClient(ScriptFunctionClient.class);
    }

    @Bean
    public UserClient userClient(HttpServiceProxyFactory factory) {
        return factory.createClient(UserClient.class);
    }

    @Bean
    public ServiceInstanceClient serviceInstanceClient(HttpServiceProxyFactory factory) {
        return factory.createClient(ServiceInstanceClient.class);
    }

    @Bean
    public OpenapiLogClient openapiLogClient(HttpServiceProxyFactory factory) {
        return factory.createClient(OpenapiLogClient.class);
    }

    @Bean
    public JsScriptClient jsScriptClient(HttpServiceProxyFactory factory) {
        return factory.createClient(JsScriptClient.class);
    }
}
