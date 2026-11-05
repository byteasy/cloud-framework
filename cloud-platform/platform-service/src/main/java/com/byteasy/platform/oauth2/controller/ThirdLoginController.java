package com.byteasy.platform.oauth2.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.api.ICacheHelper;
import com.byteasy.platform.configuration.PlatformProperties;
import com.byteasy.platform.oauth2.dto.ExternalUser;
import com.byteasy.script.service.IJsScriptApiService;
import com.byteasy.util.CastUtil;
import com.byteasy.util.MapUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(Constant.PAPI + "/sso")
@RefreshScope
public class ThirdLoginController {

    private Logger logger = LogManager.getLogger();
    @Autowired
    private PlatformProperties platformProperties;
    @Autowired
    private ICacheHelper cacheHelper;
    @Autowired
    private IJsScriptApiService jsScriptApiService;

    @GetMapping("/{thirdAppName}/callback")
    public void auth(@PathVariable("thirdAppName") String thirdAppName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 拿code 向thirdAppName后台换取用户信息,这里标准的Auth2 server返回会携带code和state
        // 同时也支持非标准协议的系统登录，支持通过传递一个token来进行登录

        if (!platformProperties.getThirdLoginPermittedFunctionCodes().contains(thirdAppName)) {
            logger.error("api function: {} not in permitted list: {}", thirdAppName, platformProperties.getThirdLoginPermittedFunctionCodes());
            response.sendRedirect(request.getContextPath() + "/index.html");
            return;
        }

        Map<String, String[]> params = request.getParameterMap();

        Map<String, String> headers = new HashMap<>();
        request.getHeaderNames().asIterator().forEachRemaining(key -> {
            headers.put(key, request.getHeader(key));
        });

        try {
            Result result = jsScriptApiService.runScript(thirdAppName, MapUtil.convertToMap(params), headers, null, false);
            if (result.isSuccess()) {
                Map<String, Object> data = (Map<String, Object>) result.getData();
                // 不过通过那种方式，调用第三方平台获取accessToken然后获取用户信息，或者从传递过来的ToKen解密，均在脚本中实现，最后返回标准的结果集Userinfo
                ExternalUser userinfo = CastUtil.mapToBean(data, ExternalUser.class);
                if (StringUtils.isNotEmpty(userinfo.getUsername())) {
                    // 生成一次性短时凭证 Ticket (30秒有效)
                    String ssoTicket = "BT_SSO_TICKET_" + UUID.randomUUID().toString().replace("-", "");
                    cacheHelper.put(platformProperties.getPlatformCacheName(), ssoTicket, userinfo.getUsername(), 15L, TimeUnit.SECONDS);

                    Cookie ticketCookie = new Cookie(com.byteasy.platform.Constant.OAUTH2_COOKIE_THIRD_TOKEN_NAME, ssoTicket);
                    ticketCookie.setPath("/");
                    ticketCookie.setMaxAge(60);
                    ticketCookie.setHttpOnly(false);
                    response.addCookie(ticketCookie);

                    response.sendRedirect(request.getContextPath() + "/index.html#" + (userinfo.getPath() != null ? userinfo.getPath() : ""));
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("third app: {} auto login failed, err: {}", thirdAppName, e);
        }
        response.sendRedirect(request.getContextPath() + "/index.html");
    }
}
