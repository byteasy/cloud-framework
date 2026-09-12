package com.byteasy.platform.oauth2.controller;

import cn.hutool.json.JSONUtil;
import com.byteasy.Constant;
import com.byteasy.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.SAPI + "/authorization")
public class AuthorizationController {

    private Logger logger = LogManager.getLogger();

    @GetMapping("/auth")
    public void auth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
//            if (!resourceService.authorization(request)) {
//                response.setStatus(403);
//                response.getWriter().write(JSONUtil.toJsonStr(Result.error("认证失败，未授权的操作")));
//            } else {
//                response.getWriter().write(JSONUtil.toJsonStr(Result.of()));
//            }
        } catch (Exception e) {
            response.setStatus(403);
            response.getWriter().write(JSONUtil.toJsonStr(Result.error("认证失败，服务器内部错误")));
            logger.error("认证报错", e);
        }
    }
}
