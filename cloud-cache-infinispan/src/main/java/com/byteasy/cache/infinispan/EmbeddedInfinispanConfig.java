package com.byteasy.cache.infinispan;

import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.spring.embedded.provider.SpringEmbeddedCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

@Configuration
public class EmbeddedInfinispanConfig {
    /**
     * 初始化底层原生 EmbeddedCacheManager
     */
    @Bean(destroyMethod = "stop")
    public EmbeddedCacheManager embeddedCacheManager() throws IOException {
        // 加载生产 XML 配置文件
        DefaultCacheManager cacheManager = new DefaultCacheManager("infinispan-embedded.xml");
        // 启动各命名缓存
        cacheManager.start();
        return cacheManager;
    }

    /**
     * 桥接至 Spring Cache 抽象管理器，支持 @Cacheable、@CachePut
     */
    @Bean
    @Primary
    public SpringEmbeddedCacheManager cacheManager(EmbeddedCacheManager embeddedCacheManager) {
        return new SpringEmbeddedCacheManager(embeddedCacheManager);
    }
}
