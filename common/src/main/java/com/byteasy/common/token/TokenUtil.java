package com.byteasy.common.token;

import cn.hutool.core.codec.Base64;
import com.byteasy.Constant;
import com.byteasy.common.util.JacksonUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class TokenUtil {

    /**
     * 有定时任务类的使用场景，为了保持框架上的统一，增加一个设置当前操作人信息的入口，方便填充记录的创建人、修改人、删除人等审计信息
     */
    public static UserInfo getUserinfo() {
        UserInfo userInfo = getByHttpHeader();
        if (userInfo == null) {
            userInfo = UserInfoHolder.getUserInfo();
        }
        return userInfo;
    }

    private static UserInfo getByHttpHeader() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String base64codeString = request.getHeader(Constant.USER_INFO);
        UserInfo userInfo = JacksonUtil.parse(Base64.decodeStr(base64codeString), UserInfo.class);
        if (userInfo != null) {
            if (userInfo.getName() != null) {
                userInfo.setName(userInfo.getName().replace(" ", ""));
            }
        }
        return userInfo;
    }

    public static String getPreferredUserName() {
        UserInfo userInfo = getUserinfo();
        if (userInfo == null) {
            return null;
        }
        return userInfo.getUsername();
    }

    public static String getEmail() {
        UserInfo userInfo = getUserinfo();
        if (userInfo == null) {
            return null;
        }
        return userInfo.getEmail();
    }

    public static String getMobile() {
        UserInfo userInfo = getUserinfo();
        if (userInfo == null) {
            return null;
        }
        return userInfo.getMobile();
    }

    public static String getUserId() {
        UserInfo userInfo = getUserinfo();
        if (userInfo == null) {
            return null;
        }
        return userInfo.getUserId();
    }
}
