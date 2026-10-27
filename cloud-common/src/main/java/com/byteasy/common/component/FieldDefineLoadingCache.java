package com.byteasy.common.component;

import com.byteasy.component.ICacheLoader;
import com.byteasy.infinispan.util.CacheEvictHelper;
import com.byteasy.platform.Constant;
import com.byteasy.platform.api.FieldDefineClient;
import com.byteasy.platform.entity.EntityDefine;
import com.byteasy.platform.entity.FieldDefine;
import com.byteasy.platform.entity.FieldGroup;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class FieldDefineLoadingCache implements ICacheLoader<String, List<FieldDefine>> {

    @Autowired
    private FieldDefineClient fieldDefineService;
    @Autowired
    private CacheEvictHelper cacheEvictHelper;

    @Override
    @Cacheable(cacheNames = Constant.PLATFORM_REP_CACHE, key = "'com.byteasy.platform.entity.FieldDefine:' + #className")
    public List<FieldDefine> load(String className) {
        List<FieldDefine> list = fieldDefineService.listEntityFieldDefines(className).getIfPresent();
        if (list != null && !list.isEmpty()) {
            return list;
        }
        return List.of();
    }

    @Override
    public void invalid(Object entity) {
        if (entity instanceof EntityDefine) {
            cacheEvictHelper.evictAfterCommit(Constant.PLATFORM_REP_CACHE, FieldDefine.class.getName() + ":" + ((EntityDefine) entity).getCode());
        } else if (entity instanceof FieldDefine) {
            cacheEvictHelper.evictAfterCommit(Constant.PLATFORM_REP_CACHE, FieldDefine.class.getName() + ":" + ((FieldDefine) entity).getClassName());
        } else if (entity instanceof FieldGroup) {
            cacheEvictHelper.evictAfterCommit(Constant.PLATFORM_REP_CACHE, FieldDefine.class.getName() + ":" + ((FieldGroup) entity).getClassName());
        }
    }

    @Override
    public String cacheType() {
        return null;
    }

    @Override
    public String[] invalidType() {
        return new String[]{FieldDefine.class.getName(), FieldGroup.class.getName(), EntityDefine.class.getName()};
    }
}
