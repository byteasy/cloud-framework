package com.byteasy.event;

import com.byteasy.enums.IBaseEnum;

public enum EventType implements IBaseEnum<Integer> {
    SINGLETON(0, "单机模式"),
    ALL(1, "集群运行中所有服务实例"),
    ONE_INSTANCE_PER_SERVICE_GROUP(2, "每个微服务的单个实例"),
    ;
    public final int value;
    public final String desc;

    EventType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static EventType fromValue(int value) {
        for (EventType status : EventType.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的枚举值: " + value);
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
