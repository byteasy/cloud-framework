package com.byteasy.component;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteasy.Result;
import com.byteasy.api.IEntityDefineService;
import com.byteasy.api.IServiceInstanceService;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.dto.EntityDefineDTO;
import com.byteasy.dto.ServiceInstanceDTO;
import com.byteasy.enums.Enable;
import com.byteasy.event.IPlatformReady;
import com.byteasy.util.ColumnUtil;
import com.byteasy.util.SpringContextUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class AsyncWaitServiceListener {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private IServiceInstanceService serviceInstanceService;
    @Autowired
    private IEntityDefineService entityDefineService;
    @Autowired
    private EntityServiceFactory entityServiceFactory;

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
        registerSelfToPlatform();
        registerEntityDefine();

        Map<String, IPlatformReady> beans = SpringUtil.getBeansOfType(IPlatformReady.class);
        for (IPlatformReady value : beans.values()) {
            value.platformReady();
        }
    }


    private void registerSelfToPlatform() {
        try {
            ServiceInstanceDTO serviceInstanceDTO = new ServiceInstanceDTO();
            serviceInstanceDTO.setCode(SpringUtil.getApplicationName());
            serviceInstanceDTO.setContextPath(SpringContextUtil.getContextPath());
            serviceInstanceDTO.setStatus(Enable.ENABLE);
            serviceInstanceDTO.setName(I18nUtil.convertI18nFieldZhValue(SpringUtil.getApplicationName()));
            Result<ServiceInstanceDTO> result = serviceInstanceService.saveOrUpdate(serviceInstanceDTO);
            log.info("ServiceInstanceAutoRegister registerSelfToPlatform result: {}", JacksonUtil.toJson(result));
        } catch (Exception e) {
            log.error("ServiceInstanceAutoRegister err", e);
        }
    }

    private void registerEntityDefine() {
        try {
            List<EntityDefineDTO> list = new ArrayList<>();
            for (ServiceImpl service : entityServiceFactory.getEntityService()) {
                Class<?> entityClass = service.getEntityClass();

                EntityDefineDTO entityDefineDTO = new EntityDefineDTO();
                entityDefineDTO.setCode(entityClass.getName());
                entityDefineDTO.setTableName(ColumnUtil.getTableName(entityClass));
                String classDesc = ColumnUtil.getSchemaDescription(entityClass);
                String className = ColumnUtil.getSchemaName(entityClass);
                if (StringUtils.isEmpty(className)) {
                    className = entityClass.getSimpleName();
                }
                if (StringUtils.isEmpty(classDesc)) {
                    classDesc = entityClass.getSimpleName();
                }
                if (className.startsWith("{")) {
                    entityDefineDTO.setName(className);
                } else {
                    entityDefineDTO.setName(I18nUtil.convertI18nFieldZhValue(className));
                }
                entityDefineDTO.setRemark(classDesc);
                entityDefineDTO.setServiceCode(SpringUtil.getApplicationName());
                list.add(entityDefineDTO);
            }
            Result result = entityDefineService.saveOrUpdateBatch(list);
            log.info("ServiceInstanceAutoRegister registerEntityDefine result: {}", JacksonUtil.toJson(result));
        } catch (Exception e) {
            log.error("ServiceInstanceAutoRegister err", e);
        }
    }
}
