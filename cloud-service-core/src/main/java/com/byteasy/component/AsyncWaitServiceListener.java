package com.byteasy.component;

import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.event.IPlatformReady;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class AsyncWaitServiceListener {

    @Autowired
    private DiscoveryClient discoveryClient;

    private static final String TARGET_SERVICE = "platform-service";

    @Async // 异步执行，不阻塞 Spring Boot 主线程启动
    @EventListener(ApplicationReadyEvent.class)
    public void waitUntilServiceReady() {

        int maxRetries = 30; // 最多等待 30 次
        int retryIntervalSec = 2; // 每次间隔 2 秒

        for (int i = 0; i < maxRetries; i++) {
            var instances = discoveryClient.getInstances(TARGET_SERVICE);
            if (instances != null && !instances.isEmpty()) {
                log.info(">>> 目标服务 " + TARGET_SERVICE + " 已上线，开始执行 API 调用...");
                callRemoteApi();
                return;
            }

            try {
                TimeUnit.SECONDS.sleep(retryIntervalSec);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        log.info(">>> 超时：目标服务 " + TARGET_SERVICE + " 未在预定时间内上线！");
    }

    private void callRemoteApi() {
        Map<String, IPlatformReady> beans = SpringUtil.getBeansOfType(IPlatformReady.class);
        List<IPlatformReady> list = beans.values().stream().sorted(Comparator.comparingInt(IPlatformReady::order)).toList();
        for (IPlatformReady iPlatformReady : list) {
            iPlatformReady.ready();
        }
    }

}
