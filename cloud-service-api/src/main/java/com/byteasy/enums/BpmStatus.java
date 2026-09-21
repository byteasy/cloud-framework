package com.byteasy.enums;

/**
 * 流程审批状态枚举
 */
public enum BpmStatus implements IBaseEnum<Integer> {

    DRAFT(0, "草稿"),
    PROCESSING(1, "审批中"),
    APPROVED(2, "审批通过"),
    REJECTED(3, "审批拒绝"),
    REVOKED(4, "已撤销"),
    TERMINATED(5, "已终止"),
    SUSPENDED(6, "已挂起");

    public final int value;
    public final String desc;

    BpmStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.desc;
    }

    public static BpmStatus fromValue(int value) {
        for (BpmStatus status : BpmStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的BPM状态值: " + value);
    }

    /**
     * 前端 Tag/Badge 状态样式映射
     */
    @Override
    public String getCss() {
        switch (this) {
            case DRAFT:
                return INFO;        // 灰色/次要
            case PROCESSING:
                return PRIMARY;     // 蓝色/主色（进行中）
            case APPROVED:
                return SUCCESS;     // 绿色（成功）
            case REJECTED:
            case TERMINATED:
                return DANGER;      // 红色（失败/终止）
            case REVOKED:
            case SUSPENDED:
                return WARNING;     // 黄色/橙色（撤回/警告/挂起）
            default:
                return IBaseEnum.super.getCss();
        }
    }

    /**
     * 判断当前流程是否已经处于终态（不可再审批）
     */
    public boolean isEnd() {
        return this == APPROVED || this == REJECTED || this == REVOKED || this == TERMINATED;
    }
}
