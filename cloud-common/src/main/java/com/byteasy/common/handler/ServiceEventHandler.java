package com.byteasy.common.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.common.service.IDatabaseMetaService;
import com.byteasy.platform.dto.CreateAuditLogTableDTO;
import com.byteasy.util.JacksonUtil;
import io.vertx.core.eventbus.EventBus;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class ServiceEventHandler {

    @Autowired
    private EventBus eventBus;
    @Autowired
    private IDatabaseMetaService databaseMetaService;

    @PostConstruct
    public void init() {
        eventBus.consumer(SpringUtil.getApplicationName(), message -> {
            String json = message.body().toString();
            Object entity = JacksonUtil.parseObj(json);
            if (entity instanceof CreateAuditLogTableDTO) {
                log.info("createAuditSchema json: {}", json);
                databaseMetaService.createAuditSchema((CreateAuditLogTableDTO) entity);
            }
        });
    }
}
