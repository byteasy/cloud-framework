package com.byteasy.enums;

/**
 * 常用表单校验模式
 */
public enum ValidatorPattern implements IBaseEnum<Integer> {
    NONE(-1, "无"),
    MOBILE(0, "手机号"),
    EMAIL(1, "邮箱"),
    ACCOUNT(2, "登录账号"),
    PASSWORD(3, "密码"),
    URL(4, "URL"),
    ID_CARD(5, "身份证"),
    ;

    public final int value;
    public final String desc;

    ValidatorPattern(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static ValidatorPattern fromValue(int value) {
        for (ValidatorPattern status : ValidatorPattern.values()) {
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
