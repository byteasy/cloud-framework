package com.byteasy.platform.oauth2.entity;

import com.byteasy.annotations.FieldMeta;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@FieldMeta(name = "{\"zh_CN\":\"客户端设置\",\"en\":\"OAuth2 Client Settings\"}")
public class OAuth2ClientSettings {
    /**
     * 是否要求客户端在授权码流程中提供 Proof Key 挑战和验证器（默认false）
     */
    @FieldMeta(name = "Proof Key挑战和验证", showOrder = 100)
    private boolean requireProofKey = false;

    /**
     * 客户端请求访问时是否需要授权同意（默认false）
     */
    @FieldMeta(name = "是否需要授权同意", showOrder = 101)
    private boolean requireAuthorizationConsent = false;

    /**
     * 客户端 JSON Web Key Set 的URL
     */
    @FieldMeta(name = "客户端WebKeySet URL", showOrder = 102)
    private String jwkSetUrl;

    /**
     * 客户端在令牌端点认证时签名JWT使用的JWS算法
     * 适用于 private_key_jwt / client_secret_jwt 认证方式
     * JwsAlgorithm
     */
    @FieldMeta(name = "认证签名算法", showOrder = 103)
    private String tokenEndpointAuthenticationSigningAlgorithm;

    /**
     * 使用 tls_client_auth 认证方式时，客户端X509证书的预期主题DN
     */
    @FieldMeta(name = "X509证书DN", showOrder = 104)
    private String x509CertificateSubjectDN;
}
