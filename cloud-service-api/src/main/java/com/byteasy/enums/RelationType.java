package com.byteasy.enums;

public enum RelationType implements IBaseEnum<Integer> {
    EXTERNAL_KEY(0, "External Primary Key"),
    DETAIL_TABLE(1, "Detail Table"),
    ;

    public final int value;
    public final String desc;

    RelationType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static RelationType fromValue(int value) {
        for (RelationType status : RelationType.values()) {
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
