package com.byteasy.platform.entity;

import com.byteasy.enums.Gender;
import com.byteasy.enums.IBaseEnum;

public enum UserStatus implements IBaseEnum<Integer> {
    DISABLE(0, "禁用"),
    NORMAL(1, "正常"),
    LOCKED(2, "锁定"),
    LOGOUT(3, "注销"),
    ;
    public final int value;
    public final String desc;

    UserStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static Gender fromValue(int value) {
        for (Gender status : Gender.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的用户状态: " + value);
    }

    @Override
    public String getName() {
        return desc;
    }

    @Override
    public String getCss() {
        if (value == 0) {
            return DANGER;
        } else if (value == 2 || value == 3) {
            return WARNING;
        } else {
            return IBaseEnum.super.getCss();
        }
    }


}
