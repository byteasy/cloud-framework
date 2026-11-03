package com.byteasy.infinispan.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.annotations.Openapi;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(Constant.SAPI + "/admin/infinispan")
public class CacheManagerController {

    @Autowired
    private EmbeddedCacheManager cacheManager;

    @GetMapping("/caches")
    @Openapi(title = "查询所有缓存集合", entityName = "com.byteasy.platform.entity.Cache")
    public Result<Set<String>> getCacheNames() {
        return Result.success(cacheManager.getCacheNames());
    }

    @GetMapping("/caches/{cache}")
    @Openapi(title = "查看指定缓存的所有Key及数量", entityName = "com.byteasy.platform.entity.Cache")
    public Result<Map<String, Object>> getCacheKeys(@PathVariable("cache") String cacheName) {
        Cache<Object, Object> cache = cacheManager.getCache(cacheName, false);
        if (cache == null) {
            return Result.success(Map.of("error", "Cache不存在: " + cacheName));
        }

        // 注意：分布式缓存(Distributed Cache)建议使用 keySet() 或 stream
        Set<Object> keys = new HashSet<>(cache.keySet());

        Map<String, Object> result = new HashMap<>();
        result.put("cacheName", cacheName);
        result.put("size", cache.size());
        result.put("keys", keys);
        return Result.success(result);
    }

    @DeleteMapping("/caches/{cache}")
    @Openapi(title = "清空指定缓存集合", entityName = "com.byteasy.platform.entity.Cache")
    public Result<String> clearCache(@PathVariable("cache") String cache) {
        Cache<Object, Object> cacheMap = cacheManager.getCache(cache, false);
        if (cacheMap != null) {
            cacheMap.clear();
            return Result.success("Cleared " + cache);
        }
        return Result.error("Not found");
    }

    @GetMapping("/caches/{cache}/key")
    @Openapi(title = "根据Key查看具体的值", entityName = "com.byteasy.platform.entity.Cache")
    public Result<Map<String, Object>> getCacheValue(@PathVariable("cache") String cacheName, @RequestParam("key") String key) {
        Cache<Object, Object> cache = cacheManager.getCache(cacheName, false);
        if (cache == null) {
            return Result.success(Map.of("error", "Cache不存在"));
        }

        Object value = cache.get(key);
        Map<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("value", value);
        result.put("valueType", value != null ? value.getClass().getName() : "null");
        return Result.success(result);
    }

    @DeleteMapping("/caches/{cache}/key")
    @Openapi(title = "删除cache下的key", entityName = "com.byteasy.platform.entity.Cache")
    public Result<Map<String, Object>> deleteCacheValue(@PathVariable("cache") String cacheName, @RequestParam("key") String key) {
        Cache<Object, Object> cache = cacheManager.getCache(cacheName, false);
        if (cache == null) {
            return Result.success(Map.of("error", "Cache不存在"));
        }
        cache.remove(key);
        return Result.success();
    }

    @PutMapping("/caches/{cache}")
    @Openapi(title = "打印指定缓存的所有数据（调试时小心大数据量）", entityName = "com.byteasy.platform.entity.Cache")
    public Result<Map<Object, Object>> dumpCache(@PathVariable("cache") String cacheName) {
        Cache<Object, Object> cache = cacheManager.getCache(cacheName, false);
        if (cache == null) {
            return Result.success(Map.of());
        }
        Map<Object, Object> map = new HashMap<>();
        // 使用 entrySet 遍历（Infinispan 会自动触发集群范围的分布式抓取）
        cache.entrySet().forEach(entry -> map.put(entry.getKey(), entry.getValue()));
        return Result.success(map);
    }


}
