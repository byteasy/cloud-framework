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
 * 平台角色定义表
 * </p>
 *
 * @author devops
 * @since 2026-04-02
 */
@Getter
@Setter
@TableName("platform_role")
@Schema(name = "Role", description = "{\"zh_CN\":\"系统角色\",\"en\":\"System Role\"}")
public class Role extends AuditEntity<Long> {

    @Schema(description = "编码")
    @TableField("code")
    @FieldMeta(showOrder = 1, isSearch = true, isRequire = true)
    private String code;

    @Schema(description = "名称")
    @TableField("name")
    @FieldMeta(showOrder = 2, isSearch = true, isI18n = true, isRequire = true)
    private String name;

    @Schema(description = "状态")
    @TableField("status")
    @FieldMeta(showOrder = 3, isSearch = true, isRequire = true)
    private Enable status;

    @Schema(description = "备注")
    @TableField("remark")
    @FieldMeta(showOrder = 4, isI18n = true)
    private String remark;
}
