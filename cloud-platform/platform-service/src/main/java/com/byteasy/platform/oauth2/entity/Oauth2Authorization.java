package com.byteasy.platform.oauth2.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.byteasy.annotations.FieldMeta;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author devops
 * @since 2026-02-26
 */
@Getter
@Setter
@TableName("oauth2_authorization")
@Schema(name = "Oauth2Authorization", description = "")
public class Oauth2Authorization implements Serializable {

    @FieldMeta(showOrder = 101, isOverflow = true)
    private String id;

    @FieldMeta(showOrder = 102, isOverflow = true)
    private String registeredClientId;

    @FieldMeta(showOrder = 103, isSearch = true)
    private String principalName;

    @FieldMeta(showOrder = 104)
    private String authorizationGrantType;

    @FieldMeta(showOrder = 105)
    private String authorizedScopes;

    @FieldMeta(showOrder = 106, isOverflow = true)
    private String attributes;

    @FieldMeta(showOrder = 107, isShow = false)
    private String state;

    @FieldMeta(showOrder = 108, isShow = false)
    private String authorizationCodeValue;

    @FieldMeta(showOrder = 109)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime authorizationCodeIssuedAt;

    @FieldMeta(showOrder = 110)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime authorizationCodeExpiresAt;

    @FieldMeta(showOrder = 111, isOverflow = true)
    private String authorizationCodeMetadata;

    @FieldMeta(showOrder = 112, isOverflow = true)
    private String accessTokenValue;

    @FieldMeta(showOrder = 113)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime accessTokenIssuedAt;

    @FieldMeta(showOrder = 114)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime accessTokenExpiresAt;

    @FieldMeta(showOrder = 115, isOverflow = true)
    private String accessTokenMetadata;

    @FieldMeta(showOrder = 116)
    private String accessTokenType;

    @FieldMeta(showOrder = 117)
    private String accessTokenScopes;

    @FieldMeta(showOrder = 118, isOverflow = true)
    private String oidcIdTokenValue;

    @FieldMeta(showOrder = 119)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime oidcIdTokenIssuedAt;

    @FieldMeta(showOrder = 120)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime oidcIdTokenExpiresAt;

    @FieldMeta(showOrder = 121, isOverflow = true)
    private String oidcIdTokenMetadata;

    @FieldMeta(showOrder = 122, isOverflow = true)
    private String refreshTokenValue;

    @FieldMeta(showOrder = 123)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime refreshTokenIssuedAt;

    @FieldMeta(showOrder = 124)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime refreshTokenExpiresAt;

    @FieldMeta(showOrder = 125, isOverflow = true)
    private String refreshTokenMetadata;

    @FieldMeta(showOrder = 126)
    private String userCodeValue;

    @FieldMeta(showOrder = 127)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime userCodeIssuedAt;

    @FieldMeta(showOrder = 128)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime userCodeExpiresAt;

    @FieldMeta(showOrder = 129, isOverflow = true)
    private String userCodeMetadata;

    @FieldMeta(showOrder = 130)
    private String deviceCodeValue;

    @FieldMeta(showOrder = 131)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deviceCodeIssuedAt;

    @FieldMeta(showOrder = 132)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deviceCodeExpiresAt;

    @FieldMeta(showOrder = 130, isOverflow = true)
    private String deviceCodeMetadata;
}
