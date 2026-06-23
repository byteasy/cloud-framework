package com.byteasy.component;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteasy.Result;
import com.byteasy.api.IEntityDefineService;
import com.byteasy.api.IServiceInstanceService;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.controller.AbstractController;
import com.byteasy.dto.EntityDefineDTO;
import com.byteasy.dto.ServiceInstanceDTO;
import com.byteasy.enums.Enable;
import com.byteasy.util.ColumnUtil;
import com.byteasy.util.SpringContextUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Order(value = Ordered.LOWEST_PRECEDENCE)
@Log4j2
public class ServiceInstanceAutoRegister {
    @Autowired
    private IServiceInstanceService serviceInstanceService;
    @Autowired
    private IEntityDefineService entityDefineService;
    @Autowired
    private EntityServiceFactory entityServiceFactory;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void onRegistered() {
        scheduler.schedule(this::registerSelfToPlatform, 30, TimeUnit.SECONDS);
        scheduler.schedule(this::registerEntityDefine, 35, TimeUnit.SECONDS);
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
                AbstractController controller = EntityControllerFactory.getController(entityClass);
                Class<?> srcController = null;
                if (controller != null) {
                    srcController = AopUtils.getTargetClass(controller);
                }

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
                entityDefineDTO.setUrl(getUrl(srcController));
                list.add(entityDefineDTO);
            }
            Result result = entityDefineService.saveOrUpdateBatch(list);
            log.info("ServiceInstanceAutoRegister registerEntityDefine result: {}", JacksonUtil.toJson(result));
        } catch (Exception e) {
            log.error("ServiceInstanceAutoRegister err", e);
        }
    }

    private String getUrl(Class<?> srcController) {
        if (srcController == null) {
            return null;
        }
        RequestMapping requestMapping = srcController.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return null;
        }
        return requestMapping.value()[0];
    }
}
