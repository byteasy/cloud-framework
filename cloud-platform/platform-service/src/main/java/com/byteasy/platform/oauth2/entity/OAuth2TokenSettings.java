package com.byteasy.platform.oauth2.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;

import java.time.Duration;

@Getter
@Setter
@EqualsAndHashCode
public class OAuth2TokenSettings {
    /**
     * 授权码的过期时间（默认5分钟）
     */
    private Duration authorizationCodeTimeToLive = Duration.ofMinutes(5);

    /**
     * 访问令牌的过期时间（默认5分钟）
     */
    private Duration accessTokenTimeToLive = Duration.ofMinutes(5);

    /**
     * 访问令牌的格式（默认 SELF_CONTAINED）
     */
    private String accessTokenFormat = OAuth2TokenFormat.SELF_CONTAINED.getValue();

    /**
     * 设备码的过期时间（默认5分钟）
     * @since 1.1
     */
    private Duration deviceCodeTimeToLive = Duration.ofMinutes(5);

    /**
     * 是否重用刷新令牌（默认true）
     */
    private boolean reuseRefreshTokens = true;

    /**
     * 刷新令牌的过期时间（默认60分钟）
     */
    private Duration refreshTokenTimeToLive = Duration.ofMinutes(60);

    /**
     * 签名ID Token使用的JWS算法（默认RS256）
     */
    private SignatureAlgorithm idTokenSignatureAlgorithm = SignatureAlgorithm.RS256;

    /**
     * 访问令牌是否必须绑定客户端X509证书（默认false）
     * @since 1.3
     */
    private boolean x509CertificateBoundAccessTokens = false;

}
