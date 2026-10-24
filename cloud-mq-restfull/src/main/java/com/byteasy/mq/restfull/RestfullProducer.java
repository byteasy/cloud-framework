package com.byteasy.mq.restfull;

import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.Constant;
import com.byteasy.Result;
import com.byteasy.common.component.ServiceInstanceLoadingCache;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.common.token.UserInfoBuilder;
import com.byteasy.common.util.HttpResult;
import com.byteasy.common.util.HttpUtil;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.common.util.JacksonUtil2;
import com.byteasy.mq.IProducer;
import com.byteasy.mq.Message;
import com.byteasy.mq.restfull.dto.PubCmd;
import com.byteasy.mq.restfull.dto.PublishResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

@Component
@ConditionalOnProperty(prefix = "byteasy.mq.producer", name = "enabled", havingValue = "true", matchIfMissing = true)
@Log4j2
public class RestfullProducer implements IProducer {

    @Autowired(required = false)
    @Qualifier("asyncExecutor")
    private Executor executor;
    @Autowired
    private ConsumerFactory consumerFactory;

    @Override
    public void publish(String topic, String key, String message) {
        Runnable saveTask = () -> {
            PubCmd pubCmd = null;
            try {
                pubCmd = doLocalPublish(topic, key, message);
                log.info("doLocalPublish message topic: {}, key: {}, message: {}", topic, key, message);
            } catch (Exception e) {
                log.error("doLocalPublish message err, topic: {}, key: {}, message: {}, err: {}", topic, key, message, e);
            }

            if (PubCmd.TERMINATE.equals(pubCmd)) {
                return;
            }

            try {
                doPublish(topic, key, message);
                log.info("publish message topic: {}, key: {}, message: {}", topic, key, message);
            } catch (Exception e) {
                log.error("publish message err, topic: {}, key: {}, message: {}, err: {}", topic, key, message, e);
            }
        };
        CompletableFuture.runAsync(saveTask, executor);
    }

    @Override
    public void publishMessage(String topic, String key, Object message) {
        publish(topic, key, JacksonUtil2.toJson(message, true));
    }


    private void doPublish(String topic, String key, String message) throws ExecutionException {
        DiscoveryClient discoveryClient = SpringUtil.getBean(DiscoveryClient.class);
        ServiceInstanceLoadingCache serviceInstanceLoadingCache = SpringUtil.getBean(ServiceInstanceLoadingCache.class);

        String applicationName = SpringUtil.getApplicationName();
        List<String> serviceIds = discoveryClient.getServices();
        if (serviceIds.isEmpty()) return;
        for (String serviceId : serviceIds) {
            if (!serviceId.equals(applicationName)) {
                UserInfo token = TokenUtil.getUserinfo();
                List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);

                for (ServiceInstance instance : instances) {
                    HttpResult<Result<PublishResult>> httpResult = HttpUtil.post(instance.getUri().toString() + serviceInstanceLoadingCache.load(serviceId).getContextPath() + Constant.SAPI + "/consumers/" + topic)
                            .header(Constant.USER_INFO, (token != null ? JacksonUtil.toJson(token) : UserInfoBuilder.builderByMicroService(SpringUtil.getApplicationName(), SpringUtil.getApplicationName())))
                            .jsonBody(new Message(key, message)).execute(new TypeReference<Result<PublishResult>>() {
                            });
                    PublishResult publishResult = httpResult.getData().getData();
                    // SubscribeType 是shared的，投递成功一次，即结束
                    if (PubCmd.TERMINATE.equals(publishResult.getCmd())) {
                        return;
                    }
                }
            }
        }
    }

    private PubCmd doLocalPublish(String topic, String key, String message) {
        return consumerFactory.receive(topic, key, message);
    }
}
