package com.byteasy.search.service;

import com.byteasy.event.EntityChangeEvent;
import com.byteasy.platform.Constant;
import com.byteasy.util.JacksonUtil;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.notifications.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

// TODO
@Component
@Listener // 标注为 Infinispan 集群事件监听器
public class LeaderConsumerCoordinator {

    private static final Logger log = LoggerFactory.getLogger(LeaderConsumerCoordinator.class);

    private final Vertx vertx;
    // 专门用来存集群协调元数据的缓存（建议配成 REPL_SYNC 复制模式）
    private final Cache<String, String> clusterMetaCache;
    private final SearchIndexService searchIndexService;

    // 当前实例的唯一标识（可以用 IP:Port 或者 Pod 名称）
    private final String instanceId = "PlatformService-" + UUID.randomUUID().toString().substring(0, 8);

    private static final String LEADER_KEY = "LEADER_LOCK:PlatformService";

    // 租约时长 10 秒
    private static final long LEASE_TIME_SEC = 10;

    private MessageConsumer<String> activeConsumer;
    private volatile boolean isLeader = false;

    public LeaderConsumerCoordinator(Vertx vertx, EmbeddedCacheManager cacheManager, SearchIndexService searchIndexService) {
        this.vertx = vertx;
        // 获取一个全局复制缓存
        this.clusterMetaCache = cacheManager.getCache("platformRepCache");
        this.searchIndexService = searchIndexService;
    }

    /**
     * 定时心跳任务（每 3 秒执行一次，必须小于 LEASE_TIME_SEC）
     */
    @Scheduled(fixedDelay = 3000)
    public synchronized void heartbeatAndElect() {
        try {
            // 1. 尝试抢占主节点（如果 key 不存在，写入自己，带过期时间）
            String currentLeader = clusterMetaCache.putIfAbsent(LEADER_KEY, instanceId, LEASE_TIME_SEC, TimeUnit.SECONDS);

            if (currentLeader == null || currentLeader.equals(instanceId)) {
                // 抢占成功，或者当前自己本来就是 Leader -> 执行心跳续约
                clusterMetaCache.put(LEADER_KEY, instanceId, LEASE_TIME_SEC, TimeUnit.SECONDS);

                if (!isLeader) {
                    isLeader = true;
                    onPromotedToLeader();
                }
            } else {
                // 当前是其他PlatformService实例在当Leader
                if (isLeader) {
                    isLeader = false;
                    onDemotedFromLeader();
                }
            }
        } catch (Exception e) {
            log.error("选主心跳异常: {}", e.getMessage());
        }
    }

    /**
     * 晋升为 Leader：注册并拉起 Consumer
     */
    private void onPromotedToLeader() {
        log.info(">>> [{}] 晋升为 PlatformService 的 Leader 实例，启动 EventBus 监听: {}", instanceId, Constant.TOPIC_ENTITY_CHANGE);
        if (activeConsumer == null) {
            activeConsumer = vertx.eventBus().consumer(Constant.TOPIC_ENTITY_CHANGE, message -> {
                vertx.executeBlocking(() -> {
                    try {
                        searchIndexService.handle(JacksonUtil.nativeRead(message.body(), new TypeReference<EntityChangeEvent>() {
                        }));
                    } catch (Exception e) {
                        log.error("searchIndexService.handle err", e);
                    }
                    return null;
                }, false);
            });
        }
    }

    /**
     * 降级为 Follower：注销 Consumer
     */
    private void onDemotedFromLeader() {
        log.warn("<<< [{}] 降级为 Follower，注销 EventBus 监听", instanceId);
        if (activeConsumer != null) {
            activeConsumer.unregister();
            activeConsumer = null;
        }
    }

    @PreDestroy
    public synchronized void destroy() {
        if (isLeader) {
            // 实例主动下线时，立即释放锁，让其他 PlatformService 瞬间接管，无需等待 10 秒超时
            clusterMetaCache.remove(LEADER_KEY, instanceId);
        }
        if (activeConsumer != null) {
            activeConsumer.unregister();
        }
    }
}
