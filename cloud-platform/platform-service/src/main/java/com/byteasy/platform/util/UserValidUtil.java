package com.byteasy.platform.util;

import com.byteasy.exception.BizException;
import com.byteasy.platform.entity.User;
import com.byteasy.platform.entity.UserStatus;

public class UserValidUtil {
    public static void checkUser(User user) {
        if (!UserStatus.NORMAL.equals(user.getStatus())) {
            throw new BizException("用户账户状态异常: " + user.getStatus().getName());
        }
    }
}
