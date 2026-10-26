package com.byteasy.common.component;

import com.byteasy.component.ICacheLoader;
import com.byteasy.platform.api.EntityDefineClient;
import com.byteasy.platform.entity.EntityDefine;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class EntityDefineLoadingCache implements ICacheLoader<String, EntityDefine> {

    @Autowired
    private EntityDefineClient entityDefineClient;

    @Override
    public String type() {
        return EntityDefine.class.getName();
    }

    public String getEntityName(String className) {
        EntityDefine entityDefine = load(className);
        if (entityDefine != null) {
            return entityDefine.getName();
        }
        return null;
    }

    @Override
    @Cacheable(cacheNames = "dict", key = "'EntityDefine:' + #code")
    public EntityDefine load(String code) {
        return entityDefineClient.getEntityDefine(code).getIfPresent();
    }
}
