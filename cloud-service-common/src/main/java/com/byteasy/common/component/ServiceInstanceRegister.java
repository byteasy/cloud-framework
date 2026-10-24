package com.byteasy.common.component;

import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.Result;
import com.byteasy.common.util.JacksonUtil2;
import com.byteasy.enums.Enable;
import com.byteasy.event.IPlatformReady;
import com.byteasy.platform.api.ServiceInstanceClient;
import com.byteasy.platform.entity.ServiceInstance;
import com.byteasy.util.I18nUtil;
import com.byteasy.util.SpringContextUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class ServiceInstanceRegister implements IPlatformReady {

    @Autowired
    private ServiceInstanceClient serviceInstanceClient;

    @Override
    public void ready() {
        log.info("ServiceInstanceRegister platformReady");
        try {
            ServiceInstance serviceInstance = new ServiceInstance();
            serviceInstance.setCode(SpringUtil.getApplicationName());
            serviceInstance.setContextPath(SpringContextUtil.getContextPath());
            serviceInstance.setStatus(Enable.ENABLE);
            serviceInstance.setName(I18nUtil.convertI18nFieldZhValue(SpringUtil.getApplicationName()));
            Result<ServiceInstance> result = serviceInstanceClient.saveOrUpdate(serviceInstance);
            log.info("ServiceInstanceRegister result: {}", JacksonUtil2.toJson(result));
        } catch (Exception e) {
            log.error("ServiceInstanceRegister err", e);
        }
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }
}
