package com.byteasy.enums;

public enum Delete implements IBaseEnum<Integer> {
    NORMAL(0, "正常"),
    DELETE(1, "已删除");
    public final int value;
    public final String desc;

    Delete(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static Delete fromValue(int value) {
        for (Delete status : Delete.values()) {
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

    @Override
    public String getCss() {
        if (value == 0) {
            return IBaseEnum.super.getCss();
        } else {
            return DANGER;
        }
    }


}
