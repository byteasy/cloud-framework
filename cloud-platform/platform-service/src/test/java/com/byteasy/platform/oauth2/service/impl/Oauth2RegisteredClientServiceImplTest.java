package com.byteasy.platform.oauth2.service.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.hc.core5.util.Asserts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@SpringBootTest(properties = {"spring.profiles.active=dev"}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"dev"})
@ComponentScan("com.jiuyang")
@Log4j2
public class Oauth2RegisteredClientServiceImplTest {

    @Autowired
    private RegisteredClientRepository registeredClientRepository;

//    @Test
    public void testSave() {
        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("demo-client").clientSecret("{noop}demo-client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://www.byteasy.cn/platform/sapi/redirect")
                .scope(OidcScopes.OPENID)
                .postLogoutRedirectUri("http://www.byteasy.cn/platform/index.html")
                .tokenSettings(TokenSettings.builder().build())
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                .build();
        registeredClientRepository.save(client);
    }

//    @Test
    public void testFindByClientId() {
        RegisteredClient registeredClient = registeredClientRepository.findByClientId("demo-client");
        Asserts.notNull(registeredClient, "");
    }
}