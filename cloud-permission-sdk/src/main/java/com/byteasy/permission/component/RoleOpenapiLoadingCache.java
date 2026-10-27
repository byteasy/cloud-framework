package com.byteasy.permission.component;

import com.byteasy.component.ICacheLoader;
import com.byteasy.infinispan.util.CacheEvictHelper;
import com.byteasy.platform.Constant;
import com.byteasy.platform.api.RoleOpenapiClient;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.platform.entity.Role;
import com.byteasy.platform.entity.RoleOpenapi;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Log4j2
public class RoleOpenapiLoadingCache implements ICacheLoader<Long, Set<Long>> {

    @Autowired
    private RoleOpenapiClient roleOpenapiService;
    @Autowired
    private CacheEvictHelper cacheEvictHelper;

    @Override
    @Cacheable(cacheNames = Constant.PLATFORM_REP_CACHE, key = "'com.byteasy.platform.entity.RoleOpenapi:' + #key")
    public Set<Long> load(Long key) {
        List<RoleOpenapi> list = roleOpenapiService.listOpenapiByRoleIds(Set.of(key)).getIfPresent();
        if (list == null) {
            return Collections.emptySet();
        }
        return list.stream().map(RoleOpenapi::getOpenapiId).collect(Collectors.toSet());
    }

    @Override
    public void invalid(Object entity) {
        if (entity instanceof Role) {
            cacheEvictHelper.evictAfterCommit(Constant.PLATFORM_REP_CACHE, RoleOpenapi.class.getName() + ":" + ((Role) entity).getId());
        } else if (entity instanceof Openapi) {
            Openapi openapi = (Openapi) entity;
            // 反查所有分配openapi的角色，然后失效所有的角色
            // TODO
        }
    }

    @Override
    public String cacheType() {
        return null;
    }

    @Override
    public String[] invalidType() {
        return new String[]{Role.class.getName(), Openapi.class.getName()};
    }

}
