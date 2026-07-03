package com.byteasy.common.interceptor;

import com.byteasy.Constant;
import com.byteasy.annotations.Authentication;
import com.byteasy.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            Authentication anno = method.getMethodAnnotation(Authentication.class);
            if (anno == null) {
                return true;
            }
            if (StringUtils.isEmpty(request.getHeader(Constant.USER_INFO))) {
                throw new BizException("需要登录后访问");
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
