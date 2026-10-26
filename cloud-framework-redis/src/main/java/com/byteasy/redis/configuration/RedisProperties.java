package com.byteasy.redis.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cloud.redis")
public class RedisProperties {
    private boolean enabled = true;
}