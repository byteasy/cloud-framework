package com.byteasy.enums;

/**
 * 状态类字段
 */
public enum Enable implements IBaseEnum<Integer> {
    DISABLE(0, "禁用"),
    ENABLE(1, "正常");

    public final int value;
    public final String desc;

    Enable(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static Enable fromValue(int value) {
        for (Enable status : Enable.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的状态值: " + value);
    }

    @Override
    public String getName() {
        return desc;
    }

    @Override
    public String getCss() {
        if (value == 0) {
            return DANGER;
        } else {
            return IBaseEnum.super.getCss();
        }
    }
}
