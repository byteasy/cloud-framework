package com.byteasy.component;

import com.byteasy.controller.AbstractController;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class EntityControllerFactory implements ApplicationContextAware {
    private static Map<Class<?>, AbstractController> abstractControllerHashMap = new HashMap<Class<?>, AbstractController>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(RestController.class);
        for (Object value : map.values()) {
            if (value instanceof AbstractController) {
                AbstractController controller = (AbstractController) value;
                abstractControllerHashMap.put(controller.getEntityClass(), controller);
                log.info("entity class: {}, AbstractController: {}", controller.getEntityClass().getName(), value.getClass().getName());
            }
        }
    }

    public static AbstractController getController(Class<?> entityClass) {
        AbstractController controller = abstractControllerHashMap.get(entityClass);
        if (controller == null) {
            for (Map.Entry<Class<?>, AbstractController> entry : abstractControllerHashMap.entrySet()) {
                if (entry.getKey().isAssignableFrom(entityClass)) {
                    return entry.getValue();
                }
            }
        }
        return controller;
    }
}
