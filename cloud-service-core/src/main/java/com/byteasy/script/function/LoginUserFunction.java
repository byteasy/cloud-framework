package com.byteasy.script.function;

import com.byteasy.common.token.TokenUtil;
import com.byteasy.script.JsFunction;
import org.springframework.stereotype.Component;

@Component
public class LoginUserFunction implements JsFunction {
    @Override
    public String group() {
        return "basic";
    }

    @Override
    public String name() {
        return "currentUser";
    }

    @Override
    public String description() {
        return """
                Get current login user info. Has no args, will get user info from http header of param name: x-userinfo.
                函数原型:
                getUserinfo();
                """;
    }

    @Override
    public Object run(Object[] args) throws Exception {
        return TokenUtil.getUserinfo();
    }
}
