package com.byteasy.script;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Log4j2
public class JsFunctionFactory {

    private Map<String, Function<Object[], Object>> jsFunctionMap = new HashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void init() {
        Map<String, JsFunction> map = SpringUtil.getBeansOfType(JsFunction.class);
        for (Map.Entry<String, JsFunction> entry : map.entrySet()) {
            jsFunctionMap.put(entry.getValue().name(), (args) -> {
                try {
                    return entry.getValue().run(args);
                } catch (Exception e) {
                    log.error(entry.getValue().name() + " run err", e);
                }
                return null;
            });
        }

        Map<String, JsFunctions> functions = SpringUtil.getBeansOfType(JsFunctions.class);
        for (Map.Entry<String, JsFunctions> functionsEntry : functions.entrySet()) {
            JsFunctions jsFunctions = functionsEntry.getValue();
            for (JsFunction jsFunction : jsFunctions.jsFunctions()) {
                jsFunctionMap.put(jsFunction.name(), (args) -> {
                    try {
                        return jsFunction.run(args);
                    } catch (Exception e) {
                        log.error(jsFunction.name() + " run err", e);
                    }
                    return null;
                });
            }
        }
    }

    public Map<String, Function<Object[], Object>> getJsFunctionMap() {
        return jsFunctionMap;
    }
}
