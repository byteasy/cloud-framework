package com.byteasy.infinispan.util;

import com.byteasy.api.ICacheHelper;
import com.byteasy.util.JacksonUtil;
import org.infinispan.commons.api.BasicCache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;

import java.util.concurrent.TimeUnit;

@Component
public class InfinispanCacheHelper implements ICacheHelper {
    @Autowired
    private EmbeddedCacheManager cacheManager;

    @Override
    public void put(String cacheName, String key, Object value, long lifeSpan, TimeUnit unit) {
        BasicCache<String, String> cache = cacheManager.getCache(cacheName);
        cache.put(key, JacksonUtil.toJson(value), lifeSpan, unit);
    }

    @Override
    public void put(String cacheName, String key, Object value) {
        BasicCache<String, String> cache = cacheManager.getCache(cacheName);
        cache.put(key, JacksonUtil.toJson(value));
    }

    @Override
    public <T> T get(String cacheName, String key, TypeReference<T> type) {
        BasicCache<String, String> cache = cacheManager.getCache(cacheName);
        String value = cache.get(key);
        return JacksonUtil.nativeRead(value, type);
    }

    @Override
    public String getStr(String cacheName, String key) {
        BasicCache<String, String> cache = cacheManager.getCache(cacheName);
        return cache.get(key);
    }

    @Override
    public String removeStr(String cacheName, String key) {
        BasicCache<String, String> cache = cacheManager.getCache(cacheName);
        return cache.remove(key);
    }

    @Override
    public <T> T remove(String cacheName, String key, TypeReference<T> type) {
        BasicCache<String, String> cache = cacheManager.getCache(cacheName);
        String value = cache.remove(key);
        return JacksonUtil.nativeRead(value, type);
    }
}
