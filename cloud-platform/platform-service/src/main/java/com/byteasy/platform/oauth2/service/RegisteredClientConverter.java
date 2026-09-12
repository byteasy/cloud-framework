package com.byteasy.platform.oauth2.service;

import com.byteasy.platform.oauth2.entity.Oauth2RegisteredClient;
import com.byteasy.platform.oauth2.entity.OAuth2ClientSettings;
import com.byteasy.platform.oauth2.entity.OAuth2TokenSettings;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 工具类：在 Oauth2RegisteredClient (Entity) 和 RegisteredClient (Domain) 之间相互转换
 */
public class RegisteredClientConverter {

    public static RegisteredClient toDomain(Oauth2RegisteredClient entity) {
        if (entity == null) return null;

        RegisteredClient.Builder builder = RegisteredClient.withId(entity.getId());

        builder.clientId(entity.getClientId())
                .clientIdIssuedAt(entity.getClientIdIssuedAt() != null ?
                        entity.getClientIdIssuedAt().atZone(java.time.ZoneId.systemDefault()).toInstant() : null)
                .clientSecret(entity.getClientSecret())
                .clientSecretExpiresAt(entity.getClientSecretExpiresAt() != null ?
                        entity.getClientSecretExpiresAt().atZone(java.time.ZoneId.systemDefault()).toInstant() : null)
                .clientName(entity.getClientName());

        // Client Authentication Methods: Set<String> → Set<ClientAuthenticationMethod>
        if (!CollectionUtils.isEmpty(entity.getClientAuthenticationMethods())) {
            entity.getClientAuthenticationMethods().forEach(method ->
                    builder.clientAuthenticationMethod(new ClientAuthenticationMethod(method)));
        }

        // Authorization Grant Types: Set<String> → Set<AuthorizationGrantType>
        if (!CollectionUtils.isEmpty(entity.getAuthorizationGrantTypes())) {
            entity.getAuthorizationGrantTypes().forEach(type ->
                    builder.authorizationGrantType(new AuthorizationGrantType(type)));
        }

        // Redirect URIs
        if (!CollectionUtils.isEmpty(entity.getRedirectUris())) {
            entity.getRedirectUris().forEach(builder::redirectUri);
        }

        // Post Logout Redirect URIs
        if (!CollectionUtils.isEmpty(entity.getPostLogoutRedirectUris())) {
            entity.getPostLogoutRedirectUris().forEach(builder::postLogoutRedirectUri);
        }

        // Scopes: String → Set<String>
        Set<String> scopes = entity.getScopes();
        if (scopes != null && !scopes.isEmpty()) {
            scopes.forEach(builder::scope);
        }

        // Client Settings: JSON String → ClientSettings
        ClientSettings clientSettings = toClientSettings(entity.getClientSettings());
        builder.clientSettings(clientSettings);

        // Token Settings: JSON String → TokenSettings
        TokenSettings tokenSettings = toTokenSettings(entity.getTokenSettings());
        builder.tokenSettings(tokenSettings);

        return builder.build();
    }

    // ========== Domain → Entity ==========

    public static Oauth2RegisteredClient toEntity(RegisteredClient domain) {
        if (domain == null) return null;

        Oauth2RegisteredClient entity = new Oauth2RegisteredClient();

        entity.setId(domain.getId());
        entity.setClientId(domain.getClientId());
        entity.setClientSecret(domain.getClientSecret());
        entity.setClientIdIssuedAt(domain.getClientIdIssuedAt() != null ? LocalDateTime.ofInstant(domain.getClientIdIssuedAt(), ZoneOffset.systemDefault()) : null);
        entity.setClientSecretExpiresAt(domain.getClientSecretExpiresAt() != null ?
                LocalDateTime.ofInstant(domain.getClientSecretExpiresAt(), java.time.ZoneId.systemDefault()) : null);
        entity.setClientName(domain.getClientName());

        // Client Authentication Methods: Set<ClientAuthenticationMethod> → Set<String>
        if (!CollectionUtils.isEmpty(domain.getClientAuthenticationMethods())) {
            Set<String> methods = domain.getClientAuthenticationMethods().stream()
                    .map(ClientAuthenticationMethod::getValue)
                    .collect(Collectors.toSet());
            entity.setClientAuthenticationMethods(methods);
        }

        // Authorization Grant Types: Set<AuthorizationGrantType> → Set<String>
        if (!CollectionUtils.isEmpty(domain.getAuthorizationGrantTypes())) {
            Set<String> types = domain.getAuthorizationGrantTypes().stream()
                    .map(AuthorizationGrantType::getValue)
                    .collect(Collectors.toSet());
            entity.setAuthorizationGrantTypes(types);
        }

        // Redirect URIs
        entity.setRedirectUris(CollectionUtils.isEmpty(domain.getRedirectUris()) ?
                Collections.emptySet() : new HashSet<>(domain.getRedirectUris()));

        // Post Logout Redirect URIs
        entity.setPostLogoutRedirectUris(CollectionUtils.isEmpty(domain.getPostLogoutRedirectUris()) ?
                Collections.emptySet() : new HashSet<>(domain.getPostLogoutRedirectUris()));

        // Scopes: Set<String> → String (comma-separated or space-separated)
        entity.setScopes(domain.getScopes());

        // Client Settings: ClientSettings → JSON String
        entity.setClientSettings(toClientSettings(domain.getClientSettings()));

        // Token Settings: TokenSettings → JSON String
        entity.setTokenSettings(toTokenSettings(domain.getTokenSettings()));

        return entity;
    }

    private static class CollectionUtils {
        public static boolean isEmpty(Collection<?> c) {
            return c == null || c.isEmpty();
        }
    }

    public static ClientSettings toClientSettings(OAuth2ClientSettings domain) {
        // 1. 空值保护：若 domain 为 null，返回默认的 ClientSettings
        if (domain == null) {
            return ClientSettings.builder().build();
        }

        // 2. 初始化 Builder（已包含默认值：requireProofKey=false、requireAuthorizationConsent=false）
        ClientSettings.Builder builder = ClientSettings.builder();

        // 3. 布尔类型属性：直接设置（布尔值无 null，domain 中默认是 false，与 ClientSettings 默认一致）
        // 若需严格区分 "未设置" 和 "显式false"，可给 domain 增加 Boolean 包装类型属性（如 Boolean requireProofKey）
        builder.requireProofKey(domain.isRequireProofKey())
                .requireAuthorizationConsent(domain.isRequireAuthorizationConsent());

        // 4. 字符串/对象类型属性：空值判断，非空才设置
        if (domain.getJwkSetUrl() != null && !domain.getJwkSetUrl().isEmpty()) {
            builder.jwkSetUrl(domain.getJwkSetUrl());
        }

        if (domain.getTokenEndpointAuthenticationSigningAlgorithm() != null) {
            builder.tokenEndpointAuthenticationSigningAlgorithm(toJwsAlgorithm(domain.getTokenEndpointAuthenticationSigningAlgorithm()));
        }

        if (domain.getX509CertificateSubjectDN() != null && !domain.getX509CertificateSubjectDN().isEmpty()) {
            builder.x509CertificateSubjectDN(domain.getX509CertificateSubjectDN());
        }

        // 5. 构建最终的 ClientSettings 实例
        return builder.build();
    }

    public static OAuth2ClientSettings toClientSettings(ClientSettings clientSettings) {
        OAuth2ClientSettings OAuth2ClientSettings = new OAuth2ClientSettings();
        OAuth2ClientSettings.setRequireProofKey(clientSettings.isRequireProofKey());
        OAuth2ClientSettings.setRequireAuthorizationConsent(clientSettings.isRequireAuthorizationConsent());
        OAuth2ClientSettings.setJwkSetUrl(clientSettings.getJwkSetUrl());
        OAuth2ClientSettings.setTokenEndpointAuthenticationSigningAlgorithm(toJwsAlgorithm(clientSettings.getTokenEndpointAuthenticationSigningAlgorithm()));
        OAuth2ClientSettings.setX509CertificateSubjectDN(clientSettings.getX509CertificateSubjectDN());
        return OAuth2ClientSettings;
    }

    public static TokenSettings toTokenSettings(OAuth2TokenSettings domain) {
        // 1. 空值保护：若 domain 为 null，返回默认的 TokenSettings（含框架默认值）
        if (domain == null) {
            return TokenSettings.builder().build();
        }

        // 2. 初始化 Builder（已包含所有默认值：如 accessTokenTimeToLive=5分钟、reuseRefreshTokens=true 等）
        TokenSettings.Builder builder = TokenSettings.builder();

        // 3. 字符串转 OAuth2TokenFormat：空值判断（避免 new OAuth2TokenFormat(null) 抛出异常）
        String accessTokenFormatStr = domain.getAccessTokenFormat();
        if (accessTokenFormatStr != null && !accessTokenFormatStr.isEmpty()) {
            builder.accessTokenFormat(new OAuth2TokenFormat(accessTokenFormatStr));
        }

        // 4. 对象类型属性：空值判断
        if (domain.getIdTokenSignatureAlgorithm() != null) {
            builder.idTokenSignatureAlgorithm(domain.getIdTokenSignatureAlgorithm());
        }

        // 5. Duration 类型属性：空值判断（Duration 为不可变对象，null 表示未设置）
        if (domain.getAccessTokenTimeToLive() != null) {
            builder.accessTokenTimeToLive(domain.getAccessTokenTimeToLive());
        }
        if (domain.getDeviceCodeTimeToLive() != null) {
            builder.deviceCodeTimeToLive(domain.getDeviceCodeTimeToLive());
        }
        if (domain.getAuthorizationCodeTimeToLive() != null) {
            builder.authorizationCodeTimeToLive(domain.getAuthorizationCodeTimeToLive());
        }
        if (domain.getRefreshTokenTimeToLive() != null) {
            builder.refreshTokenTimeToLive(domain.getRefreshTokenTimeToLive());
        }

        // 6. 布尔类型属性：直接设置（基本类型无 null，domain 默认值与 TokenSettings 默认值一致）
        // 若需区分「未设置」和「显式false」，可将 domain 中布尔属性改为 Boolean 包装类型并加空值判断
        builder.reuseRefreshTokens(domain.isReuseRefreshTokens())
                .x509CertificateBoundAccessTokens(domain.isX509CertificateBoundAccessTokens());

        // 7. 构建最终的 TokenSettings 实例
        return builder.build();
    }

    public static OAuth2TokenSettings toTokenSettings(TokenSettings tokenSettings) {
        OAuth2TokenSettings OAuth2TokenSettings = new OAuth2TokenSettings();
        OAuth2TokenSettings.setAccessTokenFormat(tokenSettings.getAccessTokenFormat().getValue());
        OAuth2TokenSettings.setAccessTokenTimeToLive(tokenSettings.getAccessTokenTimeToLive());
        OAuth2TokenSettings.setDeviceCodeTimeToLive(tokenSettings.getDeviceCodeTimeToLive());
        OAuth2TokenSettings.setAuthorizationCodeTimeToLive(tokenSettings.getAuthorizationCodeTimeToLive());
        OAuth2TokenSettings.setReuseRefreshTokens(tokenSettings.isReuseRefreshTokens());
        OAuth2TokenSettings.setRefreshTokenTimeToLive(tokenSettings.getRefreshTokenTimeToLive());
        OAuth2TokenSettings.setIdTokenSignatureAlgorithm(tokenSettings.getIdTokenSignatureAlgorithm());
        OAuth2TokenSettings.setX509CertificateBoundAccessTokens(tokenSettings.isX509CertificateBoundAccessTokens());
        return OAuth2TokenSettings;
    }

    public static String toJwsAlgorithm(JwsAlgorithm jwsAlgorithm) {
        if (jwsAlgorithm == null) {
            return null;
        }
        return jwsAlgorithm.getName();
    }

    public static JwsAlgorithm toJwsAlgorithm(String jwsAlgorithm) {
        for (MacAlgorithm value : MacAlgorithm.values()) {
            if (value.getName().equals(jwsAlgorithm)) {
                return value;
            }
        }
        for (SignatureAlgorithm value : SignatureAlgorithm.values()) {
            if (value.getName().equals(jwsAlgorithm)) {
                return value;
            }
        }
        return null;
    }
}
