package com.byteasy.common.token;

import cn.hutool.core.codec.Base64;
import com.byteasy.common.util.JacksonUtil;

public final class UserInfoBuilder {

    public static String builderByMicroService(String name, String account) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        userInfo.setFamilyName(name.substring(0, 1));
        userInfo.setGivenName(name.substring(1));
        userInfo.setUsername(account);
        userInfo.setUserId(account);
        userInfo.setStartedByUser(false);
        return Base64.encode(JacksonUtil.toJson(userInfo));
    }
}
