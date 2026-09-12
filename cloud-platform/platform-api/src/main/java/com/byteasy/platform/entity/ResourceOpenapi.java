package com.byteasy.platform.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.entity.Relation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * OAuth2客户端与平台资源的授权关联表
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
@Getter
@Setter
@TableName("platform_resource_openapi")
@Schema(name = "ResourceOpenapi", description = "OAuth2客户端与平台资源的授权关联表")
public class ResourceOpenapi extends Relation {

    @Schema(description = "OAuth2客户端ID，引用oauth2_registered_client表")
    @TableField("resource_id")
    private Long resourceId;

    @Schema(description = "平台资源ID，引用openapi_id")
    @TableField("openapi_id")
    private Long openapiId;
}
