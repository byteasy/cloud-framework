package com.byteasy.util;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertyUtil implements EnvironmentAware {

    private static Environment environment;

    @Override
    public void setEnvironment(Environment env) {
        PropertyUtil.environment = env;
    }

    /**
     * 获取配置字符串
     */
    public static String get(String key) {
        return environment.getProperty(key);
    }

    /**
     * 获取配置（带默认值）
     */
    public static String get(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    /**
     * 获取配置并转换类型
     */
    public static <T> T get(String key, Class<T> targetType, T defaultValue) {
        T value = environment.getProperty(key, targetType);
        return value != null ? value : defaultValue;
    }

    /**
     * 直接解析占位符字符串，例如 PropertyUtil.resolve("${custom.prefix:abc}_test")
     */
    public static String resolve(String placeholderStr) {
        return environment.resolvePlaceholders(placeholderStr);
    }
}
