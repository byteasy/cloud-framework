package com.byteasy.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

import java.io.Serializable;

public interface IBaseEnum<T extends Serializable> extends IEnum<T> {

    String PRIMARY = "primary";
    String SUCCESS = "success";
    String INFO = "info";
    String WARNING = "warning";
    String DANGER = "danger";

    /**
     * 枚举前端显示的值
     *
     * @return
     */
    String getName();

    /**
     * 前端显示的样式，'primary', 'success', 'info', 'warning', 'danger'
     *
     * @return
     */
    default String getCss() {
        return PRIMARY;
    }

    default String getEnumName() {
        return ((Enum) this).name();
    }
}
