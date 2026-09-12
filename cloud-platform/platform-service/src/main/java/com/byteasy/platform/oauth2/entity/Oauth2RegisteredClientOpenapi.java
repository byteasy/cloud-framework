package com.byteasy.platform.oauth2.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.entity.Relation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
@Getter
@Setter
@TableName("oauth2_registered_client_openapi")
@Schema(name = "Oauth2RegisteredClientOpenapi", description = "")
public class Oauth2RegisteredClientOpenapi extends Relation {
    @TableField(value = "oauth2_registered_client_id")
    private String oauth2RegisteredClientId;

    @TableField(value = "openapi_id")
    private Long openapiId;
}
