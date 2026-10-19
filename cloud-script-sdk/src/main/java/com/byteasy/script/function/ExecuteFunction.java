package com.byteasy.script.function;

import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.platform.dto.ScriptFunctionDTO;
import com.byteasy.script.JsFunction;
import com.byteasy.script.JsFunctionFactory;
import org.springframework.stereotype.Component;

@Component
public class ExecuteFunction implements JsFunction {

    @Override
    public String group() {
        return "basic";
    }

    @Override
    public String name() {
        return "executeFunction";
    }

    @Override
    public String description() {
        return """
                Execute js function.
                函数原型:
                Object apply(Object[] args);
                """;
    }

    @Override
    public Object run(Object[] args) throws Exception {
        if (args == null || args.length == 0) return null;
        String functionCode = args[0].toString();
        JsFunctionFactory factory = SpringUtil.getBean(JsFunctionFactory.class);
        ScriptFunctionDTO scriptFunctionDTO = factory.getJsFunctionMap().get(functionCode);
        Object[] inputArgs = new Object[args.length - 1];
        System.arraycopy(args, 1, inputArgs, 0, args.length - 1);
        return scriptFunctionDTO.getFunction().apply(inputArgs);
    }
}
