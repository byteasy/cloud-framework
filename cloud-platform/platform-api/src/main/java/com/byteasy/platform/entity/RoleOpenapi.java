package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.entity.ImmutablyEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色接口授权表
 * </p>
 *
 * @author devops
 * @since 2026-09-16
 */
@Getter
@Setter
@TableName("platform_role_openapi")
@FieldMeta(name = "RoleOpenapi", description = "角色接口授权表")
public class RoleOpenapi extends ImmutablyEntity<Long> {

    @FieldMeta(name = "平台资源关联的角色ID", showOrder = 100)
    @TableField("role_id")
    private Long roleId;

    @FieldMeta(name = "平台资源ID", showOrder = 101)
    @TableField("openapi_id")
    private Long openapiId;
}
