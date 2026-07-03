package com.byteasy.enums;

public enum Align implements IBaseEnum<Integer> {
    LEFT(0, "左对齐"),
    MIDDLE(1, "居中对齐"),
    RIGHT(2, "右对齐"),
    ;
    public final int value;
    public final String desc;

    Align(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static Align fromValue(int value) {
        for (Align status : Align.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的枚举: " + value);
    }

    @Override
    public String getName() {
        return desc;
    }
}
