package com.byteasy.enums;

/**
 * 是否类字段
 */
public enum YesNo implements IBaseEnum<Integer> {
    NO(0, "否"),
    YES(1, "是");

    public final int value;
    public final String desc;

    YesNo(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static YesNo fromValue(int value) {
        for (YesNo status : YesNo.values()) {
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
