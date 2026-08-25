package com.byteasy.component;

import cn.hutool.extra.spring.SpringUtil;
import com.google.common.collect.Maps;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Log4j2
public class CacheFactory {

    private Map<String, ICacheLoader> cache = Maps.newHashMap();

    @PostConstruct
    public void init() {
        try {
            Map<String, ICacheLoader> map = SpringUtil.getBeansOfType(ICacheLoader.class);
            for (ICacheLoader value : map.values()) {
                log.info("ICacheLoader type: {}, handler: {}", value.type(), value.getClass().getName());
                cache.put(value.type(), value);
            }
        } catch (Exception e) {
            log.warn("ICacheLoader init error", e);
        }
    }

    public boolean cacheable(String type) {
        return cache.containsKey(type);
    }

    public ICacheLoader getCacheLoader(String type) {
        return cache.get(type);
    }
}
