package com.byteasy.script;

import com.byteasy.common.util.JacksonUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Log4j2
public class Log4jFunction implements JsFunctions {

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
                return "logInfo";
            }

            @Override
            public String description() {
                return "日志打印函数";
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length == 0) return null;

                // 如果第一个参数包含占位符 {}
                String firstArg = String.valueOf(args[0]);
                if (firstArg.contains("{}") && args.length > 1) {
                    // 使用简单的 replace 替换占位符（或者使用 MessageFormatter）
                    String message = firstArg;
                    for (int i = 1; i < args.length; i++) {
                        message = message.replaceFirst("\\{\\}", JacksonUtil.toJson(args[i]));
                    }
                    log.info(message);
                } else {
                    // 否则直接拼接所有参数
                    log.info(Arrays.stream(args).map(String::valueOf).collect(Collectors.joining(" ")));
                }
                return null;
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "logWarn";
            }

            @Override
            public String description() {
                return "日志打印函数";
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length == 0) return null;

                // 如果第一个参数包含占位符 {}
                String firstArg = String.valueOf(args[0]);
                if (firstArg.contains("{}") && args.length > 1) {
                    // 使用简单的 replace 替换占位符（或者使用 MessageFormatter）
                    String message = firstArg;
                    for (int i = 1; i < args.length; i++) {
                        message = message.replaceFirst("\\{\\}", JacksonUtil.toJson(args[i]));
                    }
                    log.warn(message);
                } else {
                    // 否则直接拼接所有参数
                    log.warn(Arrays.stream(args).map(String::valueOf).collect(Collectors.joining(" ")));
                }
                return null;
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "logErr";
            }

            @Override
            public String description() {
                return "日志打印函数";
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length == 0) return null;

                // 如果第一个参数包含占位符 {}
                String firstArg = String.valueOf(args[0]);
                if (firstArg.contains("{}") && args.length > 1) {
                    // 使用简单的 replace 替换占位符（或者使用 MessageFormatter）
                    String message = firstArg;
                    for (int i = 1; i < args.length; i++) {
                        message = message.replaceFirst("\\{\\}", JacksonUtil.toJson(args[i]));
                    }
                    log.error(message);
                } else {
                    // 否则直接拼接所有参数
                    log.error(Arrays.stream(args).map(String::valueOf).collect(Collectors.joining(" ")));
                }
                return null;
            }
        });
        return list;
    }
}
