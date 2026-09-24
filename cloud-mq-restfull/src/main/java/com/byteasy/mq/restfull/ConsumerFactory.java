package com.byteasy.mq.restfull;

import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.mq.IConsumer;
import com.byteasy.mq.SubscriptionType;
import com.byteasy.mq.restfull.dto.ConsumerInstance;
import com.byteasy.mq.restfull.dto.PubCmd;
import com.google.common.collect.Maps;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.compress.utils.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(prefix = "byteasy.mq.consumer", name = "enabled", havingValue = "true", matchIfMissing = true)
@Log4j2
public class ConsumerFactory {
    private Map<String, List<IConsumer>> cache = Maps.newHashMap();

    @PostConstruct
    public void init() {
        try {
            Map<String, IConsumer> map = SpringUtil.getBeansOfType(IConsumer.class);
            for (IConsumer value : map.values()) {
                log.info("register consumer , type: {}, groupId: {}, topic: {}", value.subscribeType(), value.groupId(), value.topic());
                if (!cache.containsKey(value.topic())) {
                    cache.put(value.topic(), Lists.newArrayList());
                }
                cache.get(value.topic()).add(value);
            }
        } catch (Exception e) {
            log.warn("EntityChangeHandler init error", e);
        }
    }

    public PubCmd receive(String topic, String key, String message) {
        if (cache.containsKey(topic)) {
            List<IConsumer> list = cache.get(topic);
            for (IConsumer iConsumer : list) {
                try {
                    iConsumer.receive(key, message);
                    if (SubscriptionType.SHARED.equals(iConsumer.subscribeType())) {
                        return PubCmd.TERMINATE;
                    }
                    return PubCmd.CONTINUE;
                } catch (Exception e) {
                    log.error("consumer receive error, groupId: {}, topic: {}, error: {}", iConsumer.groupId(), iConsumer.topic(), e);
                }
            }
        }
        return PubCmd.CONTINUE;
    }

    public Collection<ConsumerInstance> getConsumers() {
        List<ConsumerInstance> list = Lists.newArrayList();
        for (List<IConsumer> value : cache.values()) {
            for (IConsumer iConsumer : value) {
                ConsumerInstance instance = new ConsumerInstance();
                instance.setGroupId(iConsumer.groupId());
                instance.setTopic(iConsumer.topic());
                instance.setSubscribeType(iConsumer.subscribeType());
                instance.setServiceName(SpringUtil.getApplicationName());
                list.add(instance);
            }
        }
        return list;
    }

}
