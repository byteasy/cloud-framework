package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.entity.Relation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 平台资源角色授权表
 * </p>
 *
 * @author devops
 * @since 2026-04-02
 */
@Getter
@Setter
@TableName("platform_role_resource")
@Schema(name = "RoleResource", description = "平台资源角色授权表")
public class RoleResource extends Relation {

    @Schema(description = "平台资源关联的角色ID")
    @TableField("role_id")
    private Long roleId;

    @Schema(description = "平台资源ID")
    @TableField("resource_id")
    private Long resourceId;
}
