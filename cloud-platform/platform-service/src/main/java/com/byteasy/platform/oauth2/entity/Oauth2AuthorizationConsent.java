package com.byteasy.platform.oauth2.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
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
@TableName("oauth2_authorization_consent")
@Schema(name = "Oauth2AuthorizationConsent", description = "")
public class Oauth2AuthorizationConsent implements Serializable {

    private String registeredClientId;

    private String principalName;

    private String authorities;
}
