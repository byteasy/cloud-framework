package com.byteasy.common.util;

import cn.hutool.core.codec.Base64;

public final class AuthorizationUtil {

    /**
     * 构造Basic认证
     *
     * @param user
     * @param password
     * @return
     */
    public static String basicHeader(String user, String password) {
        return "Basic " + Base64.encode(user + ":" + password);
    }
}
