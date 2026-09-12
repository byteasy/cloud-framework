package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.entity.AuditEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 平台资源分类表
 * </p>
 *
 * @author devops
 * @since 2026-03-17
 */
@Getter
@Setter
@TableName("platform_resource_category")
@Schema(name = "ResourceCategory", description = "{\"zh_CN\":\"菜单分类\",\"en\":\"Menu Category\"}")
public class ResourceCategory extends AuditEntity<Long> {

    @Schema(description = "资源分类")
    @TableField("parent_id")
    @FieldMeta(showOrder = 100)
    private Long parentId;

    @Schema(description = "编码")
    @TableField("code")
    @FieldMeta(showOrder = 101, isRequire = true, isShow = false)
    private String code;

    @Schema(description = "名称")
    @TableField("name")
    @FieldMeta(showOrder = 102, isRequire = true, isI18n = true)
    private String name;

    @Schema(description = "显示顺序")
    @TableField("show_order")
    @FieldMeta(showOrder = 103, isRequire = true, isShow = false)
    private Integer showOrder;

    @Schema(description = "备注")
    @TableField("remark")
    @FieldMeta(showOrder = 104, isShow = false, isI18n = true)
    private String remark;
}
