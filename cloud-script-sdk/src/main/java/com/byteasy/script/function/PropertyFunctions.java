package com.byteasy.script.function;

import com.byteasy.script.JsFunction;
import com.byteasy.script.JsFunctions;
import com.byteasy.util.PropertyUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Log4j2
public class PropertyFunctions implements JsFunctions {


    @Override
    public Collection<JsFunction> jsFunctions() {
        List<JsFunction> list = new ArrayList<>();
        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "getProperty";
            }

            @Override
            public String description() {
                return """
                            获取环境属性 函数原型：
                            String get(String key, String defaultValue)
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args.length == 1) {
                    return PropertyUtil.get(String.valueOf(args[0]));
                } else if (args.length == 2) {
                    return PropertyUtil.get(String.valueOf(args[0]), String.valueOf(args[1]));
                }
                return null;
            }
        });

        return list;
    }
}
