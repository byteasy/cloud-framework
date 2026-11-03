package com.byteasy.script.function;

import com.byteasy.script.JsFunction;
import com.byteasy.script.JsFunctions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Log4j2
public class CacheFunctions implements JsFunctions {

    @Autowired
    private CacheManager cacheManager;

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
                return "cacheGet";
            }

            @Override
            public String description() {
                return """
                            函数原型：
                            Object CacheManager.get(String cacheName, String key);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                String cacheName = args[0].toString();
                String key = args[1].toString();
                Cache cache = cacheManager.getCache(cacheName);
                if (cache == null) {
                    return null;
                }
                return cache.get(key);
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "cachePut";
            }

            @Override
            public String description() {
                return """
                            函数原型：
                            Object CacheManager.put(String cacheName, String key);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                String cacheName = args[0].toString();
                String key = args[1].toString();
                Object value = args[2];
                Cache cache = cacheManager.getCache(cacheName);
                if (cache == null) {
                    return null;
                }
                cache.put(key, value);
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
                return "cacheEvict";
            }

            @Override
            public String description() {
                return """
                            函数原型：
                            Object CacheManager.evict(String cacheName, String key);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                String cacheName = args[0].toString();
                String key = args[1].toString();
                Cache cache = cacheManager.getCache(cacheName);
                if (cache == null) {
                    return null;
                }
                cache.evict(key);
                return null;
            }
        });
        return list;
    }
}
