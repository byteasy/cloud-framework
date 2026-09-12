package com.byteasy.platform.oauth2.event;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.byteasy.common.util.IpUtil;
import com.byteasy.platform.entity.LoginLog;
import com.byteasy.platform.entity.User;
import com.byteasy.platform.entity.UserStatus;
import com.byteasy.platform.service.DictionaryUtil;
import com.byteasy.platform.service.ILoginLogService;
import com.byteasy.platform.service.IUserService;
import com.byteasy.platform.service.impl.PlatformUserDetails;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@Log4j2
public class AuthenticationEvents {

    @Autowired
    private ILoginLogService loginLogService;
    @Autowired
    private DictionaryUtil dictionaryUtil;
    @Autowired
    private IUserService userService;

    private Cache<String, LoginCounter> failureCache = CacheBuilder.newBuilder().maximumSize(5000).expireAfterWrite(Duration.ofSeconds(20)).build();

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
            WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
            PlatformUserDetails platformUserDetails = (PlatformUserDetails) token.getPrincipal();

            log.info("{} login success, ip: {}, session: {}", platformUserDetails.getUsername(), details.getRemoteAddress(), details.getSessionId());

            LoginLog loginLog = new LoginLog();
            loginLog.setType(LoginLog.Type.LOGIN);
            loginLog.setRealName(platformUserDetails.getRealName());
            loginLog.setUsername(platformUserDetails.getUsername());
            loginLog.setIpAddress(IpUtil.getRemoteIpByProxy());
            loginLog.setSessionId(details.getSessionId());
            loginLog.setUsername(platformUserDetails.getUsername());
            loginLog.setCreatedBy(platformUserDetails.getUsername());
            loginLog.setCreatorId(platformUserDetails.getUserId());
            loginLog.setCreatedTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getTimestamp()), ZoneId.systemDefault()));
            loginLogService.save(loginLog);
        }
    }

    @EventListener
    public void onLoginFailed(AbstractAuthenticationFailureEvent event) {
        String username = event.getAuthentication().getName();
        log.warn("{} login failed", username);
        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
            WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
            String account = (String) token.getPrincipal();

            LoginLog loginLog = new LoginLog();
            loginLog.setType(LoginLog.Type.LOGIN_FAILED);
            loginLog.setRealName(account);
            loginLog.setIpAddress(IpUtil.getRemoteIpByProxy());
            loginLog.setSessionId(details.getSessionId());
            loginLog.setUsername(account);
            loginLog.setCreatedBy(account);
            loginLog.setCreatorId(account);
            loginLog.setCreatedTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getTimestamp()), ZoneId.systemDefault()));
            loginLogService.save(loginLog);
        }
        // 密码错误
        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            LoginCounter loginCounter = failureCache.getIfPresent(username);
            User user = userService.getUserByUsername(username);
            if (loginCounter == null) {
                loginCounter = new LoginCounter();
                loginCounter.setCount(1);
                loginCounter.setTimestamp(event.getTimestamp());
                failureCache.put(username, loginCounter);
            } else if (loginCounter.getCount() >= dictionaryUtil.getInt("platform_settings", "login_failed_limit", 5)) {
                if (user != null) {
                    user.setStatus(UserStatus.LOCKED);
                }
            }
            if (user != null) {
                user.setLoginFailCount(user.getLoginFailCount() != null ? user.getLoginFailCount() + 1 : 1);
                userService.updateById(user);
            }
            loginCounter.setCount(loginCounter.getCount() + 1);
            log.warn("user: {} login failed, counter: {}", username, loginCounter.getCount());
        }
    }

    @EventListener
    public void onLogout(LogoutSuccessEvent logoutSuccessEvent) {
        String username = logoutSuccessEvent.getAuthentication().getName();
        log.warn("{} login out", username);
    }

    @Getter
    @Setter
    public static class LoginCounter {
        private long timestamp;
        private int count;
    }
}
