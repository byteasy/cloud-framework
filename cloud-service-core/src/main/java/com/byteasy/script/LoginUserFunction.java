package com.byteasy.script;

import com.byteasy.common.token.TokenUtil;
import org.springframework.stereotype.Component;

@Component
public class LoginUserFunction implements JsFunction {
    @Override
    public String name() {
        return "currentUser";
    }

    @Override
    public String description() {
        return "获取当前登录用户";
    }

    @Override
    public Object run(Object[] args) throws Exception {
        return TokenUtil.getUserinfo();
    }
}
