package com.byteasy.platform.oauth2.entity;


import com.byteasy.common.util.JacksonUtil;
import org.junit.jupiter.api.Test;

import java.time.Duration;

public class OAuth2TokenSettingsTest {

    @Test
    public void test() {
        OAuth2TokenSettings settings = new OAuth2TokenSettings();
        settings.setDeviceCodeTimeToLive(Duration.ofDays(1));
        System.out.println(JacksonUtil.toJson(settings));
    }

}