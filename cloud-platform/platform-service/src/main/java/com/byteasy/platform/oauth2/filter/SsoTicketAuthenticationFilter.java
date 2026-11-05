package com.byteasy.platform.oauth2.filter;

import com.byteasy.api.ICacheHelper;
import com.byteasy.platform.Constant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SsoTicketAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final ICacheHelper cacheManager;
    private final Environment environment;

    private final SecurityContextRepository securityContextRepository = new DelegatingSecurityContextRepository(
            new RequestAttributeSecurityContextRepository(),
            new HttpSessionSecurityContextRepository()
    );

    public SsoTicketAuthenticationFilter(Environment environment, ICacheHelper cacheManager, UserDetailsService userDetailsService) {
        this.cacheManager = cacheManager;
        this.userDetailsService = userDetailsService;
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ticket = request.getParameter(Constant.OAUTH2_COOKIE_THIRD_TOKEN_NAME.toLowerCase());
        if (org.apache.commons.lang3.StringUtils.isEmpty(ticket)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(com.byteasy.platform.Constant.OAUTH2_COOKIE_THIRD_TOKEN_NAME)) {
                        ticket = cookie.getValue();
                    }
                }
            }
        }

        // 如果请求携带了 sso_ticket，且当前用户未登录
        if (StringUtils.hasText(ticket) && SecurityContextHolder.getContext().getAuthentication() == null) {

            String cacheName = environment.getProperty("platformCacheName", "platformRepCache");
            // 原子性消费凭证：读取并立即删除，杜绝重放攻击！
            String username = cacheManager.removeStr(cacheName, ticket);

            if (username != null) {
                // 1. 查找本系统用户
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 2. 构建已认证成功的 Authentication
                UsernamePasswordAuthenticationToken authentication =
                        UsernamePasswordAuthenticationToken.authenticated(userDetails, null, userDetails.getAuthorities());

                // 3. 填入 SecurityContext
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);

                // 4. 【至关重要】：保存到 Session，确保随后的 OAuth2 授权端点能识别已登录
                securityContextRepository.saveContext(context, request, response);
            }
        }

        filterChain.doFilter(request, response);
    }
}
