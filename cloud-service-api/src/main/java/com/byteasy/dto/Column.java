package com.byteasy.dto;

import com.byteasy.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Column {
    @Schema(description = "类")
    private String className;
    @Schema(description = "属性名")
    private String name;
    @Schema(description = "字段名")
    private String fieldName;
    @Schema(description = "显示名")
    private String displayName;
    @Schema(description = "说明")
    private String description;
    @Schema(description = "字段类型")
    private ColumnType type;
    @Schema(description = "字段类名")
    private String typeClassName;
    @Schema(description = "显示")
    private YesNo isShow;
    @Schema(description = "排序")
    private YesNo isSort;
    @Schema(description = "省略")
    private YesNo isOverflow;
    @Schema(description = "开启多语言")
    private YesNo isI18n;
    @Schema(description = "搜索")
    private YesNo isSearch;
    @Schema(description = "表头固定")
    private YesNo isFixed;
    @Schema(description = "必填")
    private YesNo isRequire;
    @Schema(description = "表单字段")
    private YesNo isForm;
    @Schema(description = "是否加密")
    private YesNo encrypt;
    @Schema(description = "是否脱敏")
    private YesNo desensitize;
    @Schema(description = "对齐")
    private Align align;
    @Schema(description = "显示宽度")
    private Integer width;
    @Schema(description = "显示顺序")
    private Integer showOrder = 1;
    @Schema(description = "最大长度")
    private Integer maxLength = 50;
    @Schema(description = "最小长度")
    private Integer minLength = 0;
    @Schema(description = "最大值")
    private Integer max = Integer.MAX_VALUE;
    @Schema(description = "最小值")
    private Integer min = 0;
    @Schema(description = "精度")
    private Integer precision = 0;
    @Schema(description = "多选")
    private YesNo multiple = YesNo.NO;
    @Schema(description = "校验规则")
    private ValidatorPattern pattern;
    @Schema(description = "关联类型")
    private RelationType relationType;
    @Schema(description = "关联对象类名")
    private String externalClass;
    @Schema(description = "关联对象字段")
    private String externalClassPrimaryField;
    @Schema(description = "明细表关联字段")
    private String innerPrimaryField;
    @Schema(description = "关联对象展示字段")
    private String externalClassDisplayField;
    @Schema(description = "关联对象前端组件")
    private UiComponent uiComponent;
    @Schema(description = "表单自定义组件")
    private String fieldComponent;
    @Schema(description = "序列化格式")
    private String serialPattern;
    @Schema(description = "时区")
    private String timezone;
    @Schema(description = "表格列自定义组件")
    private String columnComponent;
}
