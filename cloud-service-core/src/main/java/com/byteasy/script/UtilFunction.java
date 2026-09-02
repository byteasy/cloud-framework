package com.byteasy.script;

import com.byteasy.util.EntityCodeUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Log4j2
public class UtilFunction implements JsFunctions {

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
                return "genCode";
            }

            @Override
            public String description() {
                return """
                        Use EntityCodeUtil to generate a union entity code.
                        函数原型:
                                 public static String generateCode(String prefix);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                return EntityCodeUtil.generateCode((String) args[0]);
            }
        });
        return list;
    }
}
