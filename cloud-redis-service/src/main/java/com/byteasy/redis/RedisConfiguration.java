package com.byteasy.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.core.StreamWriteFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.cfg.EnumFeature;

import java.util.TimeZone;

import static com.byteasy.common.util.JacksonUtil.customJavaTimeModule;
import static com.byteasy.common.util.JacksonUtil.formatMapModule;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // Spring Boot 4 推荐使用 GenericJackson3JsonRedisSerializer 配合 Jackson 3
        GenericJacksonJsonRedisSerializer serializer = GenericJacksonJsonRedisSerializer.create(builder -> {
            builder.customize(mapperBuilder -> {
                mapperBuilder.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        .enable(StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN)
                        .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
                        .defaultTimeZone(TimeZone.getDefault())
                        .enable(EnumFeature.WRITE_ENUMS_USING_TO_STRING)
                        .enable(EnumFeature.READ_ENUMS_USING_TO_STRING)
                        .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS);

                // 注册模块
                mapperBuilder.addModule(customJavaTimeModule());
                mapperBuilder.addModule(formatMapModule());
            });
        });

        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}