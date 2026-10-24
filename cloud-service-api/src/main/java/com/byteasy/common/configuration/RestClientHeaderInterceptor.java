package com.byteasy.common.configuration;

import cn.hutool.core.codec.Base64;
import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.Constant;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.common.token.UserInfoBuilder;
import com.byteasy.common.util.JacksonUtil;
import com.google.common.net.HttpHeaders;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Locale;

@Component
public class RestClientHeaderInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest httpServletRequest = attributes.getRequest();

            // 1. 语言传递
            String locale = httpServletRequest.getParameter(Constant.PARAM_LANG);
            if (!StringUtils.hasText(locale)) {
                locale = httpServletRequest.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
            }
            if (!StringUtils.hasText(locale)) {
                Locale current = LocaleContextHolder.getLocale();
                locale = (current != null) ? current.getLanguage() : null;
            }
            if (StringUtils.hasText(locale)) {
                request.getHeaders().set(HttpHeaders.ACCEPT_LANGUAGE, locale);
            }

            // 2. TraceId 传递
            String traceId = httpServletRequest.getHeader(Constant.TRACE_ID);
            if (StringUtils.hasText(traceId)) {
                request.getHeaders().set(Constant.TRACE_ID, traceId);
            }
        }

        request.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        // 3. Token传递
        UserInfo token = TokenUtil.getUserinfo();
        if (token != null) {
            token.setForwardBy(true);
            request.getHeaders().set(Constant.USER_INFO, Base64.encode(JacksonUtil.toJson(token)));
        } else {
            String appName = SpringUtil.getApplicationName();
            request.getHeaders().set(Constant.USER_INFO, UserInfoBuilder.builderByMicroService(appName, appName));
        }
        return execution.execute(request, body);
    }
}
