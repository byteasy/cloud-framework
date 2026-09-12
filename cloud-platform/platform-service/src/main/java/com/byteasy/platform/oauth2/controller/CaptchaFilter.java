package com.byteasy.platform.oauth2.controller;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CaptchaFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("POST".equalsIgnoreCase(request.getMethod()) && "/login".equals(request.getServletPath())) {
            String sessionCaptcha = (String) request.getSession().getAttribute("captcha_key");
            String inputCaptcha = request.getParameter("captcha");

            if (inputCaptcha == null || !inputCaptcha.equals(sessionCaptcha)) {
                // 验证码错误，重定向回登录页并带上错误标记
                response.sendRedirect(request.getContextPath() + "/login?error=captcha");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}

