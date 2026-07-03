package com.byteasy.enums;

public enum Gender implements IBaseEnum<Integer> {
    MAN(0, "男性"),
    WOMAN(1, "女性");
    public final int value;
    public final String desc;

    Gender(int value, String desc) {
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
        throw new IllegalArgumentException("无效的删除状态值: " + value);
    }

    @Override
    public String getName() {
        return desc;
    }
}
