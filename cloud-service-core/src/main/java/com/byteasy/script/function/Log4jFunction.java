package com.byteasy.script.function;

import com.byteasy.common.util.JacksonUtil;
import com.byteasy.script.JsFunction;
import com.byteasy.script.JsFunctions;
import lombok.extern.log4j.Log4j2;
import org.slf4j.helpers.MessageFormatter; // 🔥 引入 SLF4J 官方格式化工具
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Log4j2
public class Log4jFunction implements JsFunctions {

    /**
     * 提取公共的日志格式化逻辑，避免 info/warn/error 代码重复
     */
    private String formatMessage(Object[] args) {
        if (args == null || args.length == 0) return "";

        String firstArg = String.valueOf(args[0]);
        if (firstArg.contains("{}") && args.length > 1) {
            // 将后续参数序列化为 JSON 字符串
            Object[] formatArgs = new Object[args.length - 1];
            for (int i = 1; i < args.length; i++) {
                formatArgs[i - 1] = JacksonUtil.toJson(args[i]);
            }
            // 🔥 核心关键：使用 SLF4J 原生格式化器，不仅彻底解决正则 $2 崩溃，性能还提升几十倍
            return MessageFormatter.arrayFormat(firstArg, formatArgs).getMessage();
        } else {
            // 否则直接拼接所有参数
            return Arrays.stream(args).map(String::valueOf).collect(Collectors.joining(" "));
        }
    }

    @Override
    public Collection<JsFunction> jsFunctions() {
        List<JsFunction> list = new ArrayList<>();

        list.add(new JsFunction() {
            @Override
            public String group() { return "basic"; }
            @Override
            public String name() { return "logInfo"; }
            @Override
            public String description() { return "日志打印函数"; }
            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length == 0) return null;
                log.info(formatMessage(args));
                return null;
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() { return "basic"; }
            @Override
            public String name() { return "logWarn"; }
            @Override
            public String description() { return "日志打印函数"; }
            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length == 0) return null;
                log.warn(formatMessage(args));
                return null;
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() { return "basic"; }
            @Override
            public String name() { return "logErr"; }
            @Override
            public String description() { return "日志打印函数"; }
            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length == 0) return null;
                log.error(formatMessage(args));
                return null;
            }
        });

        return list;
    }
}