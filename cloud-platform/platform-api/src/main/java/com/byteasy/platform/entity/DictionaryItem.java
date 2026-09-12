package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.entity.AuditEntity;
import com.byteasy.enums.Enable;
import com.byteasy.enums.YesNo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 字典项目定义表
 * </p>
 *
 * @author devops
 * @since 2026-03-24
 */
@Getter
@Setter
@TableName("platform_dictionary_item")
@Schema(name = "DictionaryItem", description = "字典项目定义表")
public class DictionaryItem extends AuditEntity<Long> {

    @Schema(description = "字典")
    @TableField("dictionary_id")
    @FieldMeta(isShow = false, isRequire = true, showOrder = 101)
    private Long dictionaryId;

    @Schema(description = "编码")
    @TableField("code")
    @FieldMeta(isSearch = true, isRequire = true, showOrder = 102)
    private String code;

    @Schema(description = "名称")
    @TableField("label")
    @FieldMeta(isSearch = true, isRequire = true, showOrder = 103)
    private String label;

    @Schema(description = "值")
    @TableField("value")
    @FieldMeta(isRequire = true, showOrder = 104)
    private String value;

    @Schema(description = "是否默认")
    @TableField("is_default")
    @FieldMeta(showOrder = 105)
    private YesNo isDefault;

    @Schema(description = "显示样式")
    @TableField("css")
    @FieldMeta(showOrder = 106)
    private String css;

    @Schema(description = "显示顺序")
    @TableField("show_order")
    @FieldMeta(showOrder = 107)
    private Integer showOrder;

    @Schema(description = "状态")
    @TableField("status")
    @FieldMeta(isRequire = true, showOrder = 108)
    private Enable status;

    @Schema(description = "备注")
    @TableField("remark")
    @FieldMeta(showOrder = 109)
    private String remark;
}
