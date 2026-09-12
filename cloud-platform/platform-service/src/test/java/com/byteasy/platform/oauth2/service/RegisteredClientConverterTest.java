package com.byteasy.platform.oauth2.service;

import com.byteasy.platform.oauth2.service.RegisteredClientConverter;
import com.byteasy.platform.oauth2.entity.Oauth2RegisteredClient;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegisteredClientConverterTest {

    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

    // ==================== toDomain 测试 ====================

    @Test
    void toDomain_shouldConvertAllFieldsCorrectly_whenEntityIsFull() {
        // Given
        Oauth2RegisteredClient entity = createFullEntity();

        // When
        RegisteredClient domain = RegisteredClientConverter.toDomain(entity);

        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(entity.getId());
        assertThat(domain.getClientId()).isEqualTo(entity.getClientId());
        assertThat(domain.getClientIdIssuedAt())
                .isEqualTo(entity.getClientIdIssuedAt().atZone(DEFAULT_ZONE).toInstant());
        assertThat(domain.getClientSecret()).isEqualTo(entity.getClientSecret());
        assertThat(domain.getClientSecretExpiresAt())
                .isEqualTo(entity.getClientSecretExpiresAt().atZone(DEFAULT_ZONE).toInstant());
        assertThat(domain.getClientName()).isEqualTo(entity.getClientName());

        // Client Authentication Methods
        Set<ClientAuthenticationMethod> expectedAuthMethods = entity.getClientAuthenticationMethods().stream()
                .map(ClientAuthenticationMethod::new)
                .collect(java.util.stream.Collectors.toSet());
        assertThat(domain.getClientAuthenticationMethods()).isEqualTo(expectedAuthMethods);

        // Authorization Grant Types
        Set<AuthorizationGrantType> expectedGrantTypes = entity.getAuthorizationGrantTypes().stream()
                .map(AuthorizationGrantType::new)
                .collect(java.util.stream.Collectors.toSet());
        assertThat(domain.getAuthorizationGrantTypes()).isEqualTo(expectedGrantTypes);

        // Redirect URIs
        assertThat(domain.getRedirectUris()).isEqualTo(entity.getRedirectUris());

        // Post Logout Redirect URIs
        assertThat(domain.getPostLogoutRedirectUris()).isEqualTo(entity.getPostLogoutRedirectUris());

        // Scopes
        assertThat(domain.getScopes()).isEqualTo(entity.getScopes());

        // Client Settings
        assertThat(domain.getClientSettings()).isEqualTo(RegisteredClientConverter.toClientSettings(entity.getClientSettings()));

        // Token Settings
        assertThat(domain.getTokenSettings()).isEqualTo(RegisteredClientConverter.toTokenSettings(entity.getTokenSettings()));
    }

    @Test
    void toDomain_shouldHandleNullFieldsGracefully() {
        // Given
        Oauth2RegisteredClient entity = new Oauth2RegisteredClient();
        entity.setId("id");
        entity.setClientId("client");
        entity.setClientAuthenticationMethods(Sets.newTreeSet(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()));
        entity.setAuthorizationGrantTypes(Sets.newTreeSet(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()));
        entity.setRedirectUris(null);
        entity.setPostLogoutRedirectUris(null);
        entity.setScopes(null);
        entity.setClientSettings(RegisteredClientConverter.toClientSettings(ClientSettings.builder().build()));
        entity.setTokenSettings(RegisteredClientConverter.toTokenSettings(TokenSettings.builder().build()));

        // When
        RegisteredClient domain = RegisteredClientConverter.toDomain(entity);

        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo("id");
        assertThat(domain.getClientId()).isEqualTo("client");
        assertThat(domain.getClientAuthenticationMethods()).contains(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
        assertThat(domain.getAuthorizationGrantTypes()).contains(AuthorizationGrantType.CLIENT_CREDENTIALS);
        assertThat(domain.getRedirectUris()).isEmpty();
        assertThat(domain.getPostLogoutRedirectUris()).isEmpty();
        assertThat(domain.getScopes()).isEmpty();
        assertThat(domain.getClientSettings()).isNotNull();
        assertThat(domain.getTokenSettings()).isNotNull();
    }

    @Test
    void toDomain_shouldHandleEmptyCollections() {
        // Given
        Oauth2RegisteredClient entity = new Oauth2RegisteredClient();
        entity.setId("id");
        entity.setClientId("client");
        entity.setClientAuthenticationMethods(Sets.newTreeSet(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()));
        entity.setAuthorizationGrantTypes(Sets.newTreeSet(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()));
        entity.setRedirectUris(Collections.emptySet());
        entity.setPostLogoutRedirectUris(Collections.emptySet());
        entity.setScopes(Collections.emptySet());
        entity.setClientSettings(RegisteredClientConverter.toClientSettings(ClientSettings.builder().build()));
        entity.setTokenSettings(RegisteredClientConverter.toTokenSettings(TokenSettings.builder().build()));

        // When
        RegisteredClient domain = RegisteredClientConverter.toDomain(entity);

        // Then
        assertThat(domain).isNotNull();
        assertThat(domain.getClientAuthenticationMethods()).contains(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
        assertThat(domain.getAuthorizationGrantTypes()).contains(AuthorizationGrantType.CLIENT_CREDENTIALS);
        assertThat(domain.getRedirectUris()).isEmpty();
        assertThat(domain.getPostLogoutRedirectUris()).isEmpty();
        assertThat(domain.getScopes()).isEmpty();
        assertThat(domain.getClientSettings()).isNotNull();
        assertThat(domain.getTokenSettings()).isNotNull();
    }


    // ==================== toEntity 测试 ====================
    @Test
    void toEntity_shouldConvertAllFieldsCorrectly_whenDomainIsFull() {
        // Given
        RegisteredClient domain = createFullDomain();

        // When
        Oauth2RegisteredClient entity = RegisteredClientConverter.toEntity(domain);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(domain.getId());
        assertThat(entity.getClientId()).isEqualTo(domain.getClientId());
        assertThat(entity.getClientIdIssuedAt())
                .isEqualTo(LocalDateTime.ofInstant(domain.getClientIdIssuedAt(), DEFAULT_ZONE));
        assertThat(entity.getClientSecret()).isEqualTo(domain.getClientSecret());
        assertThat(entity.getClientSecretExpiresAt())
                .isEqualTo(LocalDateTime.ofInstant(domain.getClientSecretExpiresAt(), DEFAULT_ZONE));
        assertThat(entity.getClientName()).isEqualTo(domain.getClientName());

        // Client Authentication Methods
        Set<String> expectedAuthMethods = domain.getClientAuthenticationMethods().stream()
                .map(ClientAuthenticationMethod::getValue)
                .collect(java.util.stream.Collectors.toSet());
        assertThat(entity.getClientAuthenticationMethods()).isEqualTo(expectedAuthMethods);

        // Authorization Grant Types
        Set<String> expectedGrantTypes = domain.getAuthorizationGrantTypes().stream()
                .map(AuthorizationGrantType::getValue)
                .collect(java.util.stream.Collectors.toSet());
        assertThat(entity.getAuthorizationGrantTypes()).isEqualTo(expectedGrantTypes);

        // Redirect URIs
        assertThat(entity.getRedirectUris()).isEqualTo(domain.getRedirectUris());

        // Post Logout Redirect URIs
        assertThat(entity.getPostLogoutRedirectUris()).isEqualTo(domain.getPostLogoutRedirectUris());

        // Scopes
        assertThat(entity.getScopes()).isEqualTo(domain.getScopes());

        // Client Settings (JSON)
        assertThat(entity.getClientSettings()).isEqualTo(RegisteredClientConverter.toClientSettings(domain.getClientSettings()));

        // Token Settings (JSON)
        assertThat(entity.getTokenSettings()).isEqualTo(RegisteredClientConverter.toTokenSettings(domain.getTokenSettings()));
    }

    @Test
    void toEntity_shouldHandleEmptyCollections() {
        // Given: 构造一个最小合法的 RegisteredClient，部分集合为空
        RegisteredClient domain = RegisteredClient.withId("id")
                .clientId("client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)  // 至少一个
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)          // 至少一个
                .build();

        // When
        Oauth2RegisteredClient entity = RegisteredClientConverter.toEntity(domain);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo("id");
        assertThat(entity.getClientId()).isEqualTo("client");
        // 非空集合（因为我们设置了必需的认证方式和授权类型）
        assertThat(entity.getClientAuthenticationMethods())
                .containsExactly(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue());
        assertThat(entity.getAuthorizationGrantTypes())
                .containsExactly(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue());
        // 其他集合应为空
        assertThat(entity.getRedirectUris()).isEmpty();
        assertThat(entity.getPostLogoutRedirectUris()).isEmpty();
        assertThat(entity.getScopes()).isEmpty();
        // JSON 字段应序列化为非空字符串（默认为 "{}" 或 "null"? 注意转换器实现：domain.getClientSettings() 可能返回默认设置，会序列化成 JSON）
        assertThat(entity.getClientSettings()).isNotNull();
        assertThat(entity.getTokenSettings()).isNotNull();
    }

    @Test
    void toEntity_shouldHandleNullExpirationDates() {
        // Given
        RegisteredClient domain = RegisteredClient.withId("id")
                .clientId("client")
                .clientIdIssuedAt(Instant.now())
                .clientSecret("secret")
                .clientSecretExpiresAt(null)  // 显式设为 null
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();

        // When
        Oauth2RegisteredClient entity = RegisteredClientConverter.toEntity(domain);

        // Then
        assertThat(entity.getClientSecretExpiresAt()).isNull();
    }

    // ==================== 辅助构造方法 ====================

    private Oauth2RegisteredClient createFullEntity() {
        Oauth2RegisteredClient entity = new Oauth2RegisteredClient();
        entity.setId(UUID.randomUUID().toString());
        entity.setClientId("client-id");
        entity.setClientIdIssuedAt(LocalDateTime.now().minusDays(1));
        entity.setClientSecret("secret");
        entity.setClientSecretExpiresAt(LocalDateTime.now().plusDays(30));
        entity.setClientName("Test Client");

        Set<String> authMethods = new HashSet<>();
        authMethods.add("client_secret_basic");
        authMethods.add("client_secret_post");
        entity.setClientAuthenticationMethods(authMethods);

        Set<String> grantTypes = new HashSet<>();
        grantTypes.add("authorization_code");
        grantTypes.add("refresh_token");
        entity.setAuthorizationGrantTypes(grantTypes);

        Set<String> redirectUris = new HashSet<>();
        redirectUris.add("https://example.com/callback");
        entity.setRedirectUris(redirectUris);

        Set<String> logoutRedirectUris = new HashSet<>();
        logoutRedirectUris.add("https://example.com/logout");
        entity.setPostLogoutRedirectUris(logoutRedirectUris);

        Set<String> scopes = new HashSet<>();
        scopes.add("read");
        scopes.add("write");
        entity.setScopes(scopes);

        entity.setClientSettings(RegisteredClientConverter.toClientSettings(ClientSettings.builder().build()));
        entity.setTokenSettings(RegisteredClientConverter.toTokenSettings(TokenSettings.builder().build()));

        return entity;
    }

    private RegisteredClient createFullDomain() {
        Set<ClientAuthenticationMethod> authMethods = new HashSet<>();
        authMethods.add(new ClientAuthenticationMethod("client_secret_basic"));
        authMethods.add(new ClientAuthenticationMethod("client_secret_post"));

        Set<AuthorizationGrantType> grantTypes = new HashSet<>();
        grantTypes.add(new AuthorizationGrantType("authorization_code"));
        grantTypes.add(new AuthorizationGrantType("refresh_token"));

        Set<String> redirectUris = new HashSet<>();
        redirectUris.add("https://example.com/callback");

        Set<String> logoutRedirectUris = new HashSet<>();
        logoutRedirectUris.add("https://example.com/logout");

        Set<String> scopes = new HashSet<>();
        scopes.add("read");
        scopes.add("write");

        ClientSettings clientSettings = ClientSettings.builder().build();

        TokenSettings tokenSettings = TokenSettings.builder().build();

        return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("client-id")
                .clientIdIssuedAt(Instant.now().minusSeconds(86400))
                .clientSecret("secret")
                .clientSecretExpiresAt(Instant.now().plusSeconds(2592000))
                .clientName("Test Client")
                .clientAuthenticationMethods(methods -> methods.addAll(authMethods))
                .authorizationGrantTypes(types -> types.addAll(grantTypes))
                .redirectUris(uris -> uris.addAll(redirectUris))
                .postLogoutRedirectUris(uris -> uris.addAll(logoutRedirectUris))
                .scopes(s -> s.addAll(scopes))
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();
    }
}