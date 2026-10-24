package com.byteasy.common.token;

public final class UserInfoHolder {

    private static final ThreadLocal<UserInfo> userInfoLocal = new ThreadLocal<>();

    public static void setUserInfo(UserInfo userInfo) {
        userInfoLocal.set(userInfo);
    }

    public static UserInfo getUserInfo() {
        return userInfoLocal.get();
    }

    public static void clear() {
        userInfoLocal.remove();
    }
}
