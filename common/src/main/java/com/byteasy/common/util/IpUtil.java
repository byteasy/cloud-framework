package com.byteasy.common.util;

import cn.hutool.core.net.NetUtil;
import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

public final class IpUtil {

    private static String[] headersToCheck = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    /**
     * 获取对端IP地址，考虑负载均衡、nginx代理等
     *
     * @param request
     * @return
     */
    public static List<String> getClientIp(HttpServletRequest request) {
        List<String> ipList = null;
        String ip = null;

        // 检查所有可能的头部
        for (String header : headersToCheck) {
            ip = request.getHeader(header);
            if (isValidIp(ip)) {
                break;
            }
        }

        // 处理X-Forwarded-For多IP情况
        if (ip != null && ip.contains(",")) {
            ipList = Arrays.stream(ip.split(",")).map(String::trim).toList();
            return ipList;
        }

        // 最终回退
        if (!isValidIp(ip)) {
            ip = request.getRemoteAddr();
        }

        // 处理本地地址
        if ("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip)) {
            // 可以根据需要返回实际服务器IP
            ip = NetUtil.getLocalhostStr();
        }

        return Lists.newArrayList(ip);
    }

    /**
     * 获取对端IP地址，考虑负载均衡、nginx代理等
     *
     * @param request
     * @return
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ip = null;
        // 检查所有可能的头部
        for (String header : headersToCheck) {
            ip = request.getHeader(header);
            if (isValidIp(ip)) {
                break;
            }
        }

        // 处理X-Forwarded-For多IP情况
        if (ip != null && ip.contains(",")) {
            return ip;
        }

        // 最终回退
        if (!isValidIp(ip)) {
            ip = request.getRemoteAddr();
        }

        // 处理本地地址
        if ("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip)) {
            // 可以根据需要返回实际服务器IP
            ip = NetUtil.getLocalhostStr();
        }
        return ip;
    }

    public static String getRemoteIpByProxy() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return getRemoteIp(attributes.getRequest());
    }

    private static boolean isValidIp(String ip) {
        return ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip);
    }

    /**
     * 获取对方IP地址
     *
     * @return
     */
    public static String getRemoteAddr() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return attributes.getRequest().getRemoteAddr();
    }
}
