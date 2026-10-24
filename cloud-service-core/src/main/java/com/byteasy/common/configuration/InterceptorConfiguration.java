package com.byteasy.common.configuration;

import com.byteasy.common.interceptor.I18nLocaleInterceptor;
import com.byteasy.common.util.JacksonUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class InterceptorConfiguration implements WebMvcConfigurer {

    private final LocaleResolver localeResolver;

    public InterceptorConfiguration(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new I18nLocaleInterceptor(localeResolver)).addPathPatterns("/**");
    }

    @Override
    public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {
        builder.registerDefaults().withJsonConverter(new JacksonJsonHttpMessageConverter(JacksonUtil.getObjectMapper()));
    }
}
