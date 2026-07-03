package com.byteasy.common.token;

import cn.hutool.core.codec.Base64;
import com.byteasy.common.util.JacksonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserInfoTest {

    public static UserInfo create() {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("15315086265@163.com");
        userInfo.setName("测试");
        userInfo.setLocale("zh");
        userInfo.setUserId("fa1a2cec-9686-4b05-978f-54e3746660c6");
        userInfo.setEmailVerified(true);
        userInfo.setMobile("15315086265");
        userInfo.setUsername("test");
        return userInfo;
    }

    @Test
    public void generateUserInfoHeader() {
        UserInfo userInfo = create();
        System.out.println(JacksonUtil.toJson(userInfo, true));
        Assertions.assertTrue(JacksonUtil.toJson(userInfo, true).contains("@class"));
        String json = JacksonUtil.toJson(userInfo, false);
        System.out.println(json);
        System.out.println(Base64.encode(json));
    }
}