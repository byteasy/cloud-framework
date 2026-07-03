package com.byteasy.enums;

public enum UiComponent implements IBaseEnum<Integer> {
    RADIO(0, "Radio"),
    SELECT(1, "Select"),
    TABLE(2, "Table"),
    ;

    public final int value;
    public final String desc;

    UiComponent(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static UiComponent fromValue(int value) {
        for (UiComponent status : UiComponent.values()) {
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

}
