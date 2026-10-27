package com.byteasy.common.interceptor;

import cn.hutool.core.net.Ipv4Util;
import cn.hutool.http.Header;
import cn.hutool.json.JSONUtil;
import com.byteasy.Result;
import com.byteasy.util.IpUtil;
import com.byteasy.util.JacksonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Log4j2
public class WhiteIpListInterceptor implements HandlerInterceptor {

    private String[] whiteIps;

    public WhiteIpListInterceptor(String whiteIps) {
        this.whiteIps = whiteIps.split(",");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        List<String> ips = IpUtil.getClientIp(request);

        if (!isMatch(ips)) {
            log.warn("WhiteIpListInterceptor request ip: {} reject", JacksonUtil.toJson(ips));
            response.addHeader(Header.CONTENT_TYPE.getValue(), MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(403);
            response.getWriter().write(JSONUtil.toJsonStr(Result.error("认证失败，未授权的操作")));
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private boolean isMatch(List<String> ips) {
        for (String pattern : whiteIps) {
            for (String ip : ips) {
                if (Ipv4Util.matches(pattern, ip)) {
                    return true;
                }
            }
        }
        return false;
    }
}
