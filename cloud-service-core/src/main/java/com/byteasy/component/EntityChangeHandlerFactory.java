package com.byteasy.component;

import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IEntityChangeHandler;
import com.byteasy.util.JacksonUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Log4j2
public class EntityChangeHandlerFactory {

    private Map<String, IEntityChangeHandler> map = null;

    @PostConstruct
    public void init() {
        map = SpringUtil.getBeansOfType(IEntityChangeHandler.class);
    }

    public void handle(String src, EntityChangeEvent event) {
        if (map != null) {
            for (IEntityChangeHandler value : map.values()) {
                try {
                    value.handle(event);
                } catch (Exception e) {
                    log.error("type: {} handler: {} src: {} event: {} error: {}", value.type(), value.getClass().getName(), src, JacksonUtil.toPrettyJson(event), e);
                }
            }
        }
    }
}
