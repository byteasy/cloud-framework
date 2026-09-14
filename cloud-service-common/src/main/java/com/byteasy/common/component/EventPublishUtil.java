package com.byteasy.common.component;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.Constant;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.common.token.UserInfoBuilder;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.common.util.OkHttpUtil;
import com.byteasy.event.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.byteasy.util.RandomUtil.random;

@Component
@Log4j2
public class EventPublishUtil {

    @Async
    public void publishEvent(PlatformEvent event) {
        if (event instanceof EntityChangeEvent) {
            EntityChangeSource source = ((EntityChangeEvent) event).getSource();
            source.setSrcApplicationName(SpringUtil.getApplicationName());
        }
        SpringUtil.publishEvent(event);

        if (!SourceType.SINGLETON.equals(event.getSourceType())) {
            DiscoveryClient discoveryClient = SpringUtil.getBean(DiscoveryClient.class);
            ServiceInstanceLoadingCache serviceInstanceLoadingCache = SpringUtil.getBean(ServiceInstanceLoadingCache.class);

            EventSource eventSource = event.getSource();

            String applicationName = SpringUtil.getApplicationName();
            List<String> serviceIds = discoveryClient.getServices();
            if (serviceIds.isEmpty()) return;
            for (String serviceId : serviceIds) {
                if (!serviceId.equals(applicationName)) {
                    if (CollectionUtil.isNotEmpty(eventSource.getServiceIds())) {
                        // 指定的服务发布
                        if (!eventSource.getServiceIds().contains(serviceId)) {
                            continue;
                        }
                    }

                    Map<String, String> header = new HashMap<>();
                    UserInfo token = TokenUtil.getUserinfo();
                    if (token != null) {
                        header.put(Constant.USER_INFO, Base64.encode(JacksonUtil.toJson(token)));
                    } else {
                        header.put(Constant.USER_INFO, UserInfoBuilder.builderByMicroService(SpringUtil.getApplicationName(), SpringUtil.getApplicationName()));
                    }
                    List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);

                    if (SourceType.ONE_INSTANCE_PER_SERVICE_GROUP.equals(event.getSourceType())) {
                        ServiceInstance randomService = random(instances);
                        try {
                            OkHttpUtil.postJson(randomService.getUri().toString() + serviceInstanceLoadingCache.load(serviceId).getContextPath() + Constant.SAPI + "/platformEvents", JacksonUtil.toJson(event, true), header, null);
                        } catch (Exception e) {
                            log.error("publish event err", e);
                        }
                    } else if (SourceType.ALL.equals(event.getSourceType())) {
                        for (ServiceInstance instance : instances) {
                            try {
                                OkHttpUtil.postJson(instance.getUri().toString() + serviceInstanceLoadingCache.load(serviceId).getContextPath() + Constant.SAPI + "/platformEvents", JacksonUtil.toJson(event, true), header, null);
                            } catch (Exception e) {
                                log.error("publish event err", e);
                            }
                        }
                    }
                }
            }
        }
    }

}
