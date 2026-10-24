package com.byteasy.platform;

import com.byteasy.common.configuration.RestClientHeaderInterceptor;
import com.byteasy.platform.api.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration(proxyBeanMethods = false)
public class RestClientConfig {

    @Bean
    public RestClient plateformServiceRestClient(RestClient.Builder restClientBuilder, RestClientHeaderInterceptor restClientHeaderInterceptor) {
        return restClientBuilder
                .baseUrl("lb://" + Constant.SERVICE_NAME)
                .requestInterceptor(restClientHeaderInterceptor)
                .build();
    }

    @Bean
    public HttpServiceProxyFactory platformServiceProxyFactory(RestClient plateformServiceRestClient) {
        RestClientAdapter adapter = RestClientAdapter.create(plateformServiceRestClient);
        return HttpServiceProxyFactory.builderFor(adapter).build();
    }

    @Bean
    public DictionaryClient dictionaryClient(HttpServiceProxyFactory platformServiceProxyFactory) {
        return platformServiceProxyFactory.createClient(DictionaryClient.class);
    }

    @Bean
    public EntityDefineClient entityDefineClient(HttpServiceProxyFactory platformServiceProxyFactory) {
        return platformServiceProxyFactory.createClient(EntityDefineClient.class);
    }

    @Bean
    public FieldDefineClient fieldDefineClient(HttpServiceProxyFactory platformServiceProxyFactory) {
        return platformServiceProxyFactory.createClient(FieldDefineClient.class);
    }

    @Bean
    public OpenapiClient openapiClient(HttpServiceProxyFactory platformServiceProxyFactory) {
        return platformServiceProxyFactory.createClient(OpenapiClient.class);
    }

    @Bean
    public RoleOpenapiClient roleOpenapiClient(HttpServiceProxyFactory platformServiceProxyFactory) {
        return platformServiceProxyFactory.createClient(RoleOpenapiClient.class);
    }

    @Bean
    public ScriptFunctionClient scriptFunctionClient(HttpServiceProxyFactory platformServiceProxyFactory) {
        return platformServiceProxyFactory.createClient(ScriptFunctionClient.class);
    }

    @Bean
    public UserClient userClient(HttpServiceProxyFactory platformServiceProxyFactory) {
        return platformServiceProxyFactory.createClient(UserClient.class);
    }

    @Bean
    public ServiceInstanceClient serviceInstanceClient(HttpServiceProxyFactory platformServiceProxyFactory) {
        return platformServiceProxyFactory.createClient(ServiceInstanceClient.class);
    }

    @Bean
    public OpenapiLogClient openapiLogClient(HttpServiceProxyFactory platformServiceProxyFactory) {
        return platformServiceProxyFactory.createClient(OpenapiLogClient.class);
    }

    @Bean
    public JsScriptClient jsScriptClient(HttpServiceProxyFactory platformServiceProxyFactory) {
        return platformServiceProxyFactory.createClient(JsScriptClient.class);
    }
}
