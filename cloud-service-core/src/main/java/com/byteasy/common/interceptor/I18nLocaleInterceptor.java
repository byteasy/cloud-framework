package com.byteasy.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

public class I18nLocaleInterceptor implements HandlerInterceptor {
    private LocaleResolver localeResolver;

    public I18nLocaleInterceptor(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Locale targetLocale = null;
        // 1. 优先解析 URL 参数 lang
        String langParam = request.getParameter("lang");
        if (langParam != null && !langParam.isEmpty()) {
            langParam = langParam.replace("-", "_");
            // 解析参数（支持 en_US、zh_CN 格式）
            String[] langParts = langParam.split("_");
            if (langParts.length == 2) {
                targetLocale = new Locale(langParts[0], langParts[1]);
            } else {
                targetLocale = new Locale(langParts[0]);
            }
            localeResolver.setLocale(request, response, targetLocale);
            org.springframework.context.i18n.LocaleContextHolder.setLocale(targetLocale);
            return true;
        }

        // 2. 尝试从HttpHeader中获取
        if (request.getHeader("Accept-Language") != null) {
            targetLocale = request.getLocale();
            localeResolver.setLocale(request, response, targetLocale);
            org.springframework.context.i18n.LocaleContextHolder.setLocale(targetLocale);
            return true;
        }

        // 3. 无参数则解析 Accept-Language 请求头, 尝试从session加载
        targetLocale = localeResolver.resolveLocale(request);
        org.springframework.context.i18n.LocaleContextHolder.setLocale(targetLocale);
        return true;
    }
}
