package com.byteasy.common.configuration;

import com.byteasy.Constant;
import com.byteasy.common.interceptor.AuthenticationInterceptor;
import com.byteasy.common.interceptor.I18nLocaleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    private final LocaleResolver localeResolver;

    public InterceptorConfiguration(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor()).addPathPatterns(Constant.SAPI + "/**");
        registry.addInterceptor(new I18nLocaleInterceptor(localeResolver)).addPathPatterns("/**");
    }

}
