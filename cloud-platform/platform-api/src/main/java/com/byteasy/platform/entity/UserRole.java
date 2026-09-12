package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.entity.Relation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 平台角色授权表
 * </p>
 *
 * @author devops
 * @since 2026-04-02
 */
@Getter
@Setter
@TableName("platform_user_role")
@Schema(name = "UserRole", description = "平台角色授权表")
public class UserRole extends Relation {

    @Schema(description = "平台资源关联的用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "平台资源关联的角色ID")
    @TableField("role_id")
    private Long roleId;
}
