package com.byteasy.platform.oauth2.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.byteasy.entity.AuditEntity;
import com.byteasy.typehandler.PgJsonbTypeHandler;
import com.byteasy.typehandler.PgStringSetHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * OAuth2 已注册客户端信息表
 *
 * @TableName oauth2_registered_client
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "oauth2_registered_client", autoResultMap = true)
@Data
@Schema(name = "Oauth2RegisteredClient", description = "{\"zh_CN\":\"应用认证\",\"en\":\"OAuth2 application\"}")
public class Oauth2RegisteredClient extends AuditEntity<String> {
    /**
     * 客户端唯一标识符
     */
    @Schema(description = "客户端编码")
    @TableField(value = "client_id")
    @FieldMeta(showOrder = 101, isSearch = true)
    private String clientId;

    /**
     * 客户端ID颁发时间
     */
    @Schema(description = "颁发时间")
    @TableField(value = "client_id_issued_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @FieldMeta(showOrder = 102, isForm = false)
    private LocalDateTime clientIdIssuedAt;

    /**
     * 客户端密钥（加密存储）
     */
    @Schema(description = "客户端密钥")
    @TableField(value = "client_secret")
    @FieldMeta(showOrder = 103, isShow = false, maxLength = 200)
    private String clientSecret;

    /**
     * 客户端密钥过期时间
     */
    @Schema(description = "客户端密钥过期时间")
    @TableField(value = "client_secret_expires_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @FieldMeta(showOrder = 104, isShow = false)
    private LocalDateTime clientSecretExpiresAt;

    /**
     * 客户端名称（用于展示）
     */
    @Schema(description = "客户端名称")
    @TableField(value = "client_name")
    @FieldMeta(showOrder = 105, isSearch = true)
    private String clientName;

    /**
     * 客户端认证方式列表（如：client_secret_basic, client_secret_post 等）
     */
    @Schema(description = "客户端认证方式")
    @TableField(value = "client_authentication_methods", typeHandler = PgStringSetHandler.class)
    @FieldMeta(showOrder = 106, isShow = false)
    private Set<String> clientAuthenticationMethods;

    /**
     * 授权许可类型列表（如：authorization_code, refresh_token, client_credentials 等）
     */
    @Schema(description = "授权许可类型")
    @TableField(value = "authorization_grant_types", typeHandler = PgStringSetHandler.class)
    @FieldMeta(showOrder = 107, isShow = false)
    private Set<String> authorizationGrantTypes;

    /**
     * 重定向URI列表（授权回调地址）
     */
    @Schema(description = "重定向URI")
    @TableField(value = "redirect_uris", typeHandler = PgStringSetHandler.class)
    @FieldMeta(showOrder = 108, isShow = false)
    private Set<String> redirectUris;

    /**
     * 登出后重定向URI列表
     */
    @Schema(description = "登出后重定向URI")
    @TableField(value = "post_logout_redirect_uris", typeHandler = PgStringSetHandler.class)
    @FieldMeta(showOrder = 109, isShow = false)
    private Set<String> postLogoutRedirectUris;

    /**
     * 授权作用域列表（如：read, write, openid 等）
     */
    @Schema(description = "授权作用域")
    @TableField(value = "scopes", typeHandler = PgStringSetHandler.class)
    @FieldMeta(showOrder = 110, isShow = false)
    private Set<String> scopes;

    /**
     * 客户端设置（JSONB格式，如 require-proof-key, require-authorization-consent 等）
     */
    @Schema(description = "客户端设置")
    @TableField(value = "client_settings", typeHandler = PgJsonbTypeHandler.class)
    @FieldMeta(showOrder = 111, isShow = false)
    private OAuth2ClientSettings clientSettings;

    /**
     * 令牌设置（JSONB格式，如 access-token-time-to-live, refresh-token-time-to-live 等）
     */
    @Schema(description = "令牌设置")
    @TableField(value = "token_settings", typeHandler = PgJsonbTypeHandler.class)
    @FieldMeta(showOrder = 112, isShow = false)
    private OAuth2TokenSettings tokenSettings;

    /**
     * 是否启用（逻辑删除替代方案）
     */
    @Schema(description = "是否启用")
    @TableField(value = "is_active")
    @FieldMeta(showOrder = 113)
    private Boolean isActive;

    /**
     * 客户端描述信息
     */
    @Schema(description = "说明")
    @TableField(value = "description")
    @FieldMeta(showOrder = 114)
    private String description;

}