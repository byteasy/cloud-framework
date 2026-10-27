package com.byteasy.common.configuration;

import com.byteasy.common.component.EntityChangeInterceptor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class CommonConfiguration {

    @Bean
    public EntityChangeInterceptor entityChangeInterceptor() {
        return new EntityChangeInterceptor();
    }
}
