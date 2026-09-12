package com.byteasy.platform.entity;

import com.byteasy.enums.Gender;
import com.byteasy.enums.IBaseEnum;

public enum UserType implements IBaseEnum<Integer> {
    NORMAL(0, "普通账户"),
    ADMIN(1, "管理员"),
    SYSTEM(2, "系统"),
    EXTERNAL(3, "外部系统用户"),
    ;
    public final int value;
    public final String desc;

    UserType(int value, String desc) {
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
}
