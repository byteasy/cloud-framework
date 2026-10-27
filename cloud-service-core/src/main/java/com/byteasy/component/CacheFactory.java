package com.byteasy.component;

import cn.hutool.extra.spring.SpringUtil;
import com.google.common.collect.Maps;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class CacheFactory {

    private Map<String, List<ICacheLoader>> cache = Maps.newHashMap();

    @PostConstruct
    public void init() {
        try {
            Map<String, ICacheLoader> map = SpringUtil.getBeansOfType(ICacheLoader.class);
            for (ICacheLoader value : map.values()) {
                log.info("ICacheLoader type: {}, handler: {}", value.cacheType(), value.getClass().getName());
                for (String s : value.invalidType()) {
                    if (!cache.containsKey(s)) {
                        cache.put(s, Lists.newArrayList());
                    }
                    cache.get(s).add(value);
                }
            }
        } catch (Exception e) {
            log.warn("ICacheLoader init error", e);
        }
    }

    public void invalid(Object entity) {
        String clsName = entity.getClass().getName();
        if (cache.containsKey(clsName)) {
            for (ICacheLoader iCacheLoader : cache.get(clsName)) {
                iCacheLoader.invalid(entity);
            }
        }
    }
}
