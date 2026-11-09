package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.entity.AuditEntity;
import com.byteasy.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * <p>
 * 字段定义表
 * </p>
 *
 * @author devops
 * @since 2026-03-13
 */
@Getter
@Setter
@TableName("platform_field_define")
@FieldMeta(name = "FieldDefine", description = "字段定义")
public class FieldDefine extends AuditEntity<Long> {
    @Serial
    private static final long serialVersionUID = 5784589258639194740L;

    @TableField(value = "class_name", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "类", isSearch = true, showOrder = 102, width = 240, isRequire = true)
    private String className;

    @TableField(value = "name", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "属性名", isSearch = true, showOrder = 103, isRequire = true)
    private String name;

    @TableField(value = "field_name", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "字段名", isSearch = true, showOrder = 104, isRequire = true)
    private String fieldName;

    @TableField(value = "display_name", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "显示名", showOrder = 105, maxLength = 80, isI18n = true)
    private String displayName;

    @TableField(value = "description", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "说明", showOrder = 106, isI18n = true)
    private String description;

    @TableField(value = "type", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "字段类型", showOrder = 107)
    private ColumnType type;

    @TableField(value = "type_class_name", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "字段类名", showOrder = 108)
    private String typeClassName;

    @TableField(value = "group_id", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "字段分组", isRequire = false, showOrder = 109, isShow = false)
    private Long groupId;

    @TableField(value = "is_primary", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "是否主键", showOrder = 110)
    private YesNo isPrimary;

    @TableField(value = "is_show", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "是否显示", showOrder = 111)
    private YesNo isShow;

    @TableField(value = "is_overflow", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "是否省略", showOrder = 112)
    private YesNo isOverflow;

    @TableField(value = "is_i18n", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "是否多语言", showOrder = 113)
    private YesNo isI18n;

    @TableField(value = "show_order", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "显示顺序", showOrder = 114)
    private Integer showOrder;

    @TableField(value = "width", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "显示宽度", showOrder = 115)
    private Integer width;

    @TableField(value = "is_require", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "是否必填", showOrder = 116)
    private YesNo isRequire;

    @TableField(value = "is_form", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "是否表单", showOrder = 117)
    private YesNo isForm;

    @TableField(value = "is_sort", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "是否排序", showOrder = 118)
    private YesNo isSort;

    @TableField(value = "is_search", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "是否搜索", showOrder = 119)
    private YesNo isSearch;

    @TableField(value = "is_fixed", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "是否固定", showOrder = 120, description = "在Table内是否固定")
    private YesNo isFixed;

    @TableField(value = "encrypt", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "是否加密", showOrder = 121)
    private YesNo encrypt;

    @TableField(value = "desensitize", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "是否脱敏", showOrder = 122)
    private YesNo desensitize;

    @TableField(value = "align", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "对齐方式", showOrder = 124, description = "在Table内的对齐方式")
    private Align align;

    @TableField(value = "max_length", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "最大长度", showOrder = 125)
    private Integer maxLength;

    @TableField(value = "min_length", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "最小长度", showOrder = 126)
    private Integer minLength;

    @TableField(value = "max", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "最大值", showOrder = 127)
    private Integer max;

    @TableField(value = "min", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "最小值", showOrder = 128)
    private Integer min;

    @TableField(value = "precision", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "精度", showOrder = 129)
    private Integer precision;

    @TableField(value = "textarea_rows", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "多行文本", showOrder = 130)
    private Integer textareaRows;

    @TableField(value = "multiple", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "是否多选", showOrder = 130, description = "当表格组件被用于对象筛选时起作用，控制是单选还是多选")
    private YesNo multiple;

    @TableField(value = "pattern", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "校验规则", showOrder = 131, description = "自定义校验规则")
    private ValidatorPattern pattern;

    @TableField(value = "relation_type", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "关联类型", showOrder = 132, description = "目前支持外键关联和明细表关联，1对1或者1对多")
    private RelationType relationType;

    @TableField(value = "external_class", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "关联对象类名", showOrder = 133)
    private String externalClass;

    @TableField(value = "external_class_primary_field", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "关联对象字段", showOrder = 134)
    private String externalClassPrimaryField;

    @TableField(value = "inner_primary_field", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "明细表关联字段", showOrder = 135, description = "关联类型为明细表时数据库无真实字段，需要指明本对象的关联字段名")
    private String innerPrimaryField;

    @TableField(value = "external_class_display_field", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "关联对象展示字段", showOrder = 136, description = "用于展示关联对象的字段名称")
    private String externalClassDisplayField;

    @TableField(value = "external_class_auto_fill", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "关联对象展示字段", showOrder = 136, description = "用于展示关联对象的字段名称")
    private Boolean externalClassAutoFill;

    @TableField(value = "field_component", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "表单自定义组件", description = "绑定到字段上的自定义前端组件,查询表单和编辑表单共用", showOrder = 138, maxLength = 512)
    private String fieldComponent;

    @TableField(value = "column_component", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "表格列自定义组件", description = "Table Column自定义组件", showOrder = 139, maxLength = 512)
    private String columnComponent;

    @TableField(value = "extras_params", updateStrategy = FieldStrategy.ALWAYS)
    @FieldMeta(name = "拓展参数", showOrder = 140, maxLength = 512)
    private String extrasParams;

    @TableField(value = "serial_pattern", updateStrategy = FieldStrategy.ALWAYS)
    @Schema(description = "序列化格式")
    private String serialPattern;

    @TableField(value = "timezone", updateStrategy = FieldStrategy.ALWAYS)
    @Schema(description = "时区")
    private String timezone;
}
