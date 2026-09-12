package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.entity.AuditEntity;
import com.byteasy.enums.Enable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 字典定义表
 * </p>
 *
 * @author devops
 * @since 2026-03-24
 */
@Getter
@Setter
@TableName("platform_dictionary")
@Schema(name = "Dictionary", description = "字典定义表")
public class Dictionary extends AuditEntity<Long> {

    @Schema(description = "资源分组")
    @TableField("group_id")
    @FieldMeta(showOrder = 101, isShow = false)
    private Long groupId;

    @Schema(description = "编码")
    @TableField("code")
    @FieldMeta(isSearch = true, isRequire = true, showOrder = 102)
    private String code;

    @Schema(description = "名称")
    @TableField("name")
    @FieldMeta(isSearch = true, isRequire = true, showOrder = 103)
    private String name;

    @Schema(description = "状态")
    @TableField("status")
    @FieldMeta(isSearch = true, isRequire = true, showOrder = 104)
    private Enable status;

    @Schema(description = "备注")
    @TableField("remark")
    @FieldMeta(showOrder = 105)
    private String remark;
}
