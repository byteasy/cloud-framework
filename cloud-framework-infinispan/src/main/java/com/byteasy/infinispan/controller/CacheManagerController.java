package com.byteasy.infinispan.controller;

import com.byteasy.Constant;
import com.byteasy.Result;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(Constant.SAPI + "/admin/infinispan")
public class CacheManagerController {

    @Autowired
    private EmbeddedCacheManager cacheManager;

    /**
     * 1. 查看所有缓存名称列表
     * GET http://localhost:8080/admin/cache/names
     */
    @GetMapping("/names")
    public Result<Set<String>> getCacheNames() {
        return Result.success(cacheManager.getCacheNames());
    }

    /**
     * 2. 查看指定缓存的所有 Key 及数量
     * GET http://localhost:8080/admin/cache/keys?cacheName=dict
     */
    @GetMapping("/keys")
    public Result<Map<String, Object>> getCacheKeys(@RequestParam String cacheName) {
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

    /**
     * 3. 根据 Key 查看具体的值
     * GET http://localhost:8080/admin/cache/get?cacheName=dict&key=SysUser
     */
    @GetMapping("/get")
    public Result<Map<String, Object>> getCacheValue(@RequestParam String cacheName, @RequestParam String key) {
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

    /**
     * 4. 打印指定缓存的所有数据（调试时小心大数据量）
     * GET http://localhost:8080/admin/cache/dump?cacheName=dict
     */
    @GetMapping("/dump")
    public Result<Map<Object, Object>> dumpCache(@RequestParam String cacheName) {
        Cache<Object, Object> cache = cacheManager.getCache(cacheName, false);
        if (cache == null) {
            return Result.success(Map.of());
        }
        Map<Object, Object> map = new HashMap<>();
        // 使用 entrySet 遍历（Infinispan 会自动触发集群范围的分布式抓取）
        cache.entrySet().forEach(entry -> map.put(entry.getKey(), entry.getValue()));
        return Result.success(map);
    }

    /**
     * 5. 清空指定缓存
     */
    @DeleteMapping("/clear")
    public Result<String> clearCache(@RequestParam String cacheName) {
        Cache<Object, Object> cache = cacheManager.getCache(cacheName, false);
        if (cache != null) {
            cache.clear();
            return Result.success("Cleared " + cacheName);
        }
        return Result.error("Not found");
    }
}
