package com.byteasy.infinispan.util;

import lombok.extern.log4j.Log4j2;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * 为什么必须在事务提交后失效？
 * 如果直接在带 @Transactional 的方法上用 @CacheEvict，默认会在方法执行过程中（DB 事务尚未 Commit 时）就清除了缓存。
 * 如果此时有高并发读请求进来：
 * 缓存未命中，去查数据库；
 * 此时 DB 事务还没 commit，读出来的是旧数据；
 * 读请求将旧数据重新写入缓存；
 * 随后写事务提交。
 * 👉 结果：缓存里永久留存了旧的脏数据！
 * 通用解决方案：结合 Spring 事务同步器（TransactionSynchronization）
 */
@Component
@Log4j2
public class CacheEvictHelper {

    @Autowired
    private EmbeddedCacheManager cacheManager;

    /**
     * 事务提交后安全失效缓存
     */
    public void evictAfterCommit(String cacheName, Object key) {
//        if (TransactionSynchronizationManager.isSynchronizationActive()) {
//            // 如果在事务中，注册事务提交后回调
//            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//                @Override
//                public void afterCommit() {
//                    doEvict(cacheName, key);
//                }
//            });
//        } else {
//            // 无事务则直接删除
        doEvict(cacheName, key);
//        }
    }

    private void doEvict(String cacheName, Object key) {
        Cache<Object, Object> cache = cacheManager.getCache(cacheName, false);
        if (cache != null) {
            cache.remove(key); // 或者 cache.evict(key)
            log.info("invalid cache: {} key: {}", cacheName, key);
        }
    }

    /**
     * 按前缀批量失效：事务提交后安全执行
     *
     * @param cacheName 缓存名称
     * @param prefix    Key 的前缀（例如 "com.byteasy.platform.entity.EntityDefine:"）
     */
    public void evictByPrefixAfterCommit(String cacheName, String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return;
        }

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    doEvictByPrefix(cacheName, prefix);
                }
            });
        } else {
            doEvictByPrefix(cacheName, prefix);
        }
    }

    /**
     * 执行按前缀匹配删除
     */
    private void doEvictByPrefix(String cacheName, String prefix) {
        Cache<Object, Object> cache = cacheManager.getCache(cacheName, false);
        if (cache == null) {
            return;
        }

        // 核心实现：
        // 1. (Predicate<Object> & Serializable) 交叉类型确保集群模式下 Lambda 可以跨节点网络序列化传输
        // 2. removeIf 会在底层批量删除所有返回 true 的 entry
        Predicate<Object> prefixPredicate = (Predicate<Object> & Serializable) key ->
                key != null && key.toString().startsWith(prefix);

        cache.keySet().removeIf(prefixPredicate);
        log.info("invalid cache: {} prefix: {}", cacheName, prefix);
    }
}
