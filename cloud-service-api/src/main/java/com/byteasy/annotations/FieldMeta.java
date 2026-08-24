package com.byteasy.annotations;

import com.byteasy.enums.Align;
import com.byteasy.enums.ValidatorPattern;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldMeta {
    /**
     * 显示名称
     */
    String name() default "";

    /**
     * 说明
     */
    String description() default "";

    /**
     * 数据库字段名称：默认将对象的属性名从驼峰转为下划线
     */
    String fieldName() default "";

    /**
     * 显示顺序
     *
     */
    int showOrder() default 1;

    /**
     * 显示宽度
     */
    int width() default 0;

    /**
     * 分组编号
     */
    long groupId() default 0;

    /**
     * 是否主键
     */
    boolean isPrimary() default false;

    /**
     * 是否显示
     */
    boolean isShow() default true;

    /**
     * 是否排序
     */
    boolean isSort() default false;

    /**
     * 是否固定
     */
    boolean isFixed() default false;

    /**
     * 是否省略
     */
    boolean isOverflow() default false;

    /**
     * 是否搜索
     */
    boolean isSearch() default false;

    /**
     * 是否支持多选
     * 在一对一的情况下(EXTERNAL_KEY)，Select组件和Table组件支持单选、多选
     */
    boolean multiple() default false;

    /**
     * 是否必填
     */
    boolean isRequire() default false;

    /**
     * 是否表单字段
     */
    boolean isForm() default true;

    /**
     * 是否多语言字段
     */
    boolean isI18n() default false;

    /**
     * 对齐方式
     */
    Align align() default Align.LEFT;

    /**
     * 最大长度
     */
    int maxLength() default 120;

    /**
     * 最小长度
     */
    int minLength() default 0;

    /**
     * 精度
     */
    int precision() default 0;

    /**
     * 最大数字
     */
    int max() default Integer.MAX_VALUE;

    /**
     * 最小数字
     */
    int min() default 0;

    /**
     * 是否加密存储
     */
    boolean encrypt() default false;

    /**
     * 是否脱敏展示
     */
    boolean desensitize() default false;

    /**
     * 字段校验规则
     */
    ValidatorPattern pattern() default ValidatorPattern.NONE;

    /**
     * 外键或者明细表
     * 当实体类有注解TableField时，属于外键关联，表中无此字段，则属于明细表
     */
    Class<?> externalClass() default void.class;

    /**
     * 外键关联字段
     */
    String externalClassPrimaryField() default "id";

    /**
     * 明细表关联的本表字段
     */
    String innerPrimaryField() default "id";

    /**
     * 外键展示字段
     */
    String externalClassDisplayField() default "name";

    /**
     * 拓展参数
     */
    String extrasParams() default "";
}
