package com.byteasy.platform.util;

import com.byteasy.exception.BizException;
import com.byteasy.platform.entity.User;
import com.byteasy.platform.entity.UserStatus;

import java.time.LocalDateTime;

public class UserUtil {

    public static void validate(User user) {
        if (user == null) {
            throw new BizException("账号不存在");
        }
        if (!UserStatus.NORMAL.equals(user.getStatus())) {
            throw new BizException("账号已: " + user.getStatus().getName());
        }
        if (user.getAccountExpiredAt() != null && user.getAccountExpiredAt().isAfter(LocalDateTime.now())) {
            throw new BizException("账号已过期");
        }
        if (user.getCredentialsExpiredAt() != null && user.getCredentialsExpiredAt().isAfter(LocalDateTime.now())) {
            throw new BizException("密码已过期");
        }
    }
}
