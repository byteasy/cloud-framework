package com.byteasy.script.function;

import com.byteasy.exception.BizException;
import com.byteasy.script.JsFunction;
import com.byteasy.script.JsFunctions;
import com.byteasy.util.EntityCodeUtil;
import io.vertx.core.eventbus.EventBus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Component
@Log4j2
public class UtilFunction implements JsFunctions {

    @Autowired
    private EventBus eventBus;

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
                Object arg = args == null ? null : args[0];
                return EntityCodeUtil.generateCode((String) arg);
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "producerPublish";
            }

            @Override
            public String description() {
                return """
                        Use mq producer to public a message.
                        函数原型:
                                 public static void publish(String topic, String message);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length != 3) {
                    throw new BizException("参数输入不合法");
                }
                String json = (String) args[2];
                json = json.replace("com.oracle.truffle.polyglot.PolyglotMap", HashMap.class.getName());
                eventBus.publish((String) args[0], json);
                return null;
            }
        });
        return list;
    }
}
