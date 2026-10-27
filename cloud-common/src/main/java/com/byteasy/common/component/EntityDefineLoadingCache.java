package com.byteasy.common.component;

import com.byteasy.component.ICacheLoader;
import com.byteasy.infinispan.util.CacheEvictHelper;
import com.byteasy.platform.Constant;
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
    @Autowired
    private CacheEvictHelper cacheEvictHelper;

    public String getEntityName(String className) {
        EntityDefine entityDefine = load(className);
        if (entityDefine != null) {
            return entityDefine.getName();
        }
        return null;
    }

    @Override
    @Cacheable(cacheNames = Constant.PLATFORM_REP_CACHE, key = "'com.byteasy.platform.entity.EntityDefine:' + #code")
    public EntityDefine load(String code) {
        return entityDefineClient.getEntityDefine(code).getIfPresent();
    }

    @Override
    public void invalid(Object entity) {
        if (entity instanceof EntityDefine) {
            cacheEvictHelper.evictAfterCommit(Constant.PLATFORM_REP_CACHE, EntityDefine.class.getName() + ":" + ((EntityDefine) entity).getCode());
        } else if (entity instanceof String) {
            cacheEvictHelper.evictAfterCommit(Constant.PLATFORM_REP_CACHE, EntityDefine.class.getName() + ":" + entity);
        }
    }

    @Override
    public String cacheType() {
        return EntityDefine.class.getName();
    }

    @Override
    public String[] invalidType() {
        return new String[]{EntityDefine.class.getName()};
    }
}
