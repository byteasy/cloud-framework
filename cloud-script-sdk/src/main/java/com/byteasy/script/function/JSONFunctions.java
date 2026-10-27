package com.byteasy.script.function;

import com.byteasy.util.JacksonUtil;
import com.byteasy.exception.BizException;
import com.byteasy.script.JsFunction;
import com.byteasy.script.JsFunctions;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
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
                return """
                            Parse a json string to json object or object array.
                            函数原型：
                            JacksonUtil.nativeRead(String json);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                String json = String.valueOf(args[0]);
                if (json.trim().startsWith("{")) {
                    return JacksonUtil.nativeRead(String.valueOf(args[0]), new TypeReference<Map<String, Object>>() {
                    });
                } else if (json.trim().startsWith("[")) {
                    return JacksonUtil.nativeRead(String.valueOf(args[0]), new TypeReference<List<Object>>() {
                    });
                } else {
                    log.error("JSON parse error, {}", json);
                    throw new BizException("JSON parse error");
                }
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
                return """
                        Convert json object or object array to json string.
                        函数原型:
                                JacksonUtil.toJson(Object obj, boolean withClass);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args.length == 1) {
                    return JacksonUtil.toJson(args[0]);
                } else {
                    boolean withClass = (boolean) args[1];
                    if (withClass) {
                        return JacksonUtil.toJson(args[0], true);
                    } else {
                        return JacksonUtil.toJson(args[0], false);
                    }
                }
            }
        });
        return list;
    }
}
