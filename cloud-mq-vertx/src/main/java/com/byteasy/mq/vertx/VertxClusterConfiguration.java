package com.byteasy.mq.vertx;

import io.vertx.core.Vertx;
import io.vertx.ext.cluster.infinispan.InfinispanClusterManager;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VertxClusterConfiguration {

    /**
     * 注册集成的 Clustered Vert.x Bean
     * Spring 容器启动到这里时会同步阻塞，直到 Vert.x 集群构建成功
     */
    @Bean(destroyMethod = "close")
    public Vertx clusteredVertx(EmbeddedCacheManager cacheManager) {
        try {
            // 1. 初始化 Infinispan 集群管理器
            InfinispanClusterManager mgr = new InfinispanClusterManager((DefaultCacheManager) cacheManager);

            // 2. 异步构建集群，并转为标准 CompletableFuture 阻塞等待
            return Vertx.builder()
                    .withClusterManager(mgr)
                    .buildClustered()
                    .toCompletionStage() // 将 Vert.x Future 转为 Java 标准 CompletionStage
                    .toCompletableFuture()
                    .get();

        } catch (Exception e) {
            throw new IllegalStateException("Vert.x 集群启动失败", e);
        }
    }
}
