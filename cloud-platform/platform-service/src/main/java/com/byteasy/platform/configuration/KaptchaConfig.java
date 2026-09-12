package com.byteasy.platform.configuration;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        // 设置边框
        properties.put("kaptcha.border", "no");
        // 设置边框颜色
        properties.put("kaptcha.border.color", "105,179,90");
        // 设置字体颜色
        properties.put("kaptcha.textproducer.font.color", "blue");
        // 设置图片宽度
        properties.put("kaptcha.image.width", "120");
        // 设置图片高度
        properties.put("kaptcha.image.height", "40");
        // 设置字体大小
        properties.put("kaptcha.textproducer.font.size", "30");
        // 设置session key
        properties.put("kaptcha.session.key", "code");
        // 设置验证码长度
        properties.put("kaptcha.textproducer.char.length", "4");
        // 设置字体
        properties.put("kaptcha.textproducer.font.names", "Arial,Courier");
        // 设置干扰线颜色
        properties.put("kaptcha.noise.color", "black");
        // 设置干扰实现类
        properties.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");
        // 设置图片样式
        properties.put("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}
