package com.byteasy.platform.script.function;

import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.platform.service.IUserService;
import com.byteasy.script.JsFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginUserFunction implements JsFunction {

    @Autowired
    private IUserService userService;

    @Override
    public String serviceName() {
        return SpringUtil.getApplicationName();
    }

    @Override
    public String group() {
        return SpringUtil.getApplicationName();
    }

    @Override
    public String name() {
        return "getLoginUser";
    }

    @Override
    public String description() {
        return "获取当前登录用户";
    }

    @Override
    public Object run(Object[] args) throws Exception {
        UserInfo userInfo = TokenUtil.getUserinfo();
        return userService.getUserByUserId(userInfo.getUserId());
    }
}
