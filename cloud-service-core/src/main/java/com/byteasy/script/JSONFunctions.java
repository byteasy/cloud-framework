package com.byteasy.script;

import com.byteasy.common.util.JacksonUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class JSONFunctions implements JsFunctions {
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
                return "JSONParse";
            }

            @Override
            public String description() {
                return "Parse a json string to json object";
            }

            @Override
            public Object run(Object[] args) throws Exception {
                return JacksonUtil.parseObj(String.valueOf(args[0]));
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "JSONString";
            }

            @Override
            public String description() {
                return "Convert json object to json string";
            }

            @Override
            public Object run(Object[] args) throws Exception {
                return JacksonUtil.toJson(args[0]);
            }
        });
        return list;
    }
}
