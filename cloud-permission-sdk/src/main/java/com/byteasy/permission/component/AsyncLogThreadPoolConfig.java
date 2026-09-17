package com.byteasy.permission.component;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@Log4j2
public class AsyncLogThreadPoolConfig {

    @Bean("openapiLogExecutor")
    public Executor openapiLogExecutor() {
        // 1. 动态感知系统/容器的有效 CPU 核心数
        int cpuCores = Runtime.getRuntime().availableProcessors();
        // 2. I/O 密集型线程池自适应计算法则
        // 核心线程数：2 * CPU，保底至少 4 个
        int corePoolSize = Math.max(4, cpuCores * 2);
        // 最大线程数：4 * CPU，保底至少 16 个，上限不超过 64（防止暴走创建过多线程）
        int maxPoolSize = Math.min(32, Math.max(16, cpuCores * 4));
        // 有界缓冲队列大小（根据每条日志约占用 2KB 计算，2000 约占用 4MB 堆内存，极其安全）
        int queueCapacity = 2000;

        log.info("【初始化 OpenAPI 日志专属线程池】系统检测 CPU 核心数: {}, corePoolSize: {}, maxPoolSize: {}, queueCapacity: {}",
                cpuCores, corePoolSize, maxPoolSize, queueCapacity);

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("async-openapi-log-");

        // 🔥 优雅丢弃拒绝策略：队列满且工作线程打满时，丢弃并记录警告日志，坚决不阻塞业务主线程
        executor.setRejectedExecutionHandler((r, exec) -> {
            log.warn("【警告】OpenAPI 异步日志队列已满，触发丢弃保护，防止拖垮业务接口！当前队列大小: {}", queueCapacity);
        });

        // 应用关闭时等待线程池任务执行完再退出（优雅停机）
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);

        executor.initialize();
        return executor;
    }
}
