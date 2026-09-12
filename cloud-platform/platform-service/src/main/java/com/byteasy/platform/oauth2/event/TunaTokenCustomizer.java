package com.byteasy.platform.oauth2.event;

import com.byteasy.platform.service.impl.PlatformUserDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * 自定义ID Token的Claims
 */
@Log4j2
public class TunaTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {
        // 只处理ID Token类型
        if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
            Authentication principal = context.getPrincipal();

            // 1. 获取UserDetails（假设你的UserDetails实现类包含mobile等扩展字段）
            UserDetails userDetails = null;
            if (principal.getPrincipal() instanceof UserDetails) {
                userDetails = (UserDetails) principal.getPrincipal();
            }

            if (userDetails != null) {
                // 2. 标准OIDC Claims赋值
                // sub：用户唯一标识（必填，通常用用户名或用户ID）
                if (userDetails instanceof PlatformUserDetails) {
                    context.getClaims().claim(StandardClaimNames.SUB, ((PlatformUserDetails) userDetails).getUserId());
                }
                // preferred_username：首选用户名
                context.getClaims().claim(StandardClaimNames.PREFERRED_USERNAME, userDetails.getUsername());

                // 3. 自定义Claims - 手机号（需要从自定义UserDetails中获取）
                if (userDetails instanceof PlatformUserDetails platformUserDetails) {
                    context.getClaims().claim(StandardClaimNames.EMAIL, platformUserDetails.getEmail());
//                    context.getClaims().claim(StandardClaimNames.EMAIL_VERIFIED, platformUserDetails.getEmailVerified());
                    context.getClaims().claim(StandardClaimNames.PHONE_NUMBER, platformUserDetails.getMobile());
//                    context.getClaims().claim(StandardClaimNames.PHONE_NUMBER_VERIFIED, platformUserDetails.getMobileVerified());
                    context.getClaims().claim(StandardClaimNames.NAME, platformUserDetails.getRealName());
                    context.getClaims().claim(StandardClaimNames.NICKNAME, platformUserDetails.getNickname());
//                    context.getClaims().claim(StandardClaimNames.GENDER, platformUserDetails.getGender() != null ? platformUserDetails.getGender().name() : "");
                    context.getClaims().claim(StandardClaimNames.FAMILY_NAME, platformUserDetails.getRealName().substring(0, 1));
                    context.getClaims().claim(StandardClaimNames.GIVEN_NAME, platformUserDetails.getRealName().substring(1));
//                    context.getClaims().claim(StandardClaimNames.PICTURE, platformUserDetails.getAvatar());
                }
            }
        }
    }

}
