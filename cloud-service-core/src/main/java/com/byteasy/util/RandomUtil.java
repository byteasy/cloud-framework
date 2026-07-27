package com.byteasy.util;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public class RandomUtil {

    public static ServiceInstance random(List<ServiceInstance> list) {
        if (list.size() == 1) {
            return list.get(0);
        }
        int random = cn.hutool.core.util.RandomUtil.randomInt(list.size());
        return list.get(random);
    }
}
