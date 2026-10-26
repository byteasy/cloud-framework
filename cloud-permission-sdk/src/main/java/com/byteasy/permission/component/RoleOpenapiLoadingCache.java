package com.byteasy.permission.component;

import com.byteasy.component.ICacheLoader;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IEntityChangeHandler;
import com.byteasy.platform.api.RoleOpenapiClient;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.platform.entity.Role;
import com.byteasy.platform.entity.RoleOpenapi;
import com.byteasy.util.ObjectValueUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
@Log4j2
public class RoleOpenapiLoadingCache implements IEntityChangeHandler, ICacheLoader<Long, Set<Long>> {

    @Autowired
    private RoleOpenapiClient roleOpenapiService;

    private LoadingCache<Long, Set<Long>> cache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(3)).maximumSize(5000).build(new CacheLoader<>() {
        @Override
        public Set<Long> load(Long key) {
            List<RoleOpenapi> list = roleOpenapiService.listOpenapiByRoleIds(Set.of(key)).getIfPresent();
            if (list == null) {
                return Collections.emptySet();
            }
            return list.stream().map(RoleOpenapi::getOpenapiId).collect(Collectors.toSet());
        }
    });

    @Override
    public Set<Long> load(Long key) throws ExecutionException {
        return cache.get(key);
    }

    @Override
    public String type() {
        return StringUtils.joinWith(",", Role.class.getName(), Openapi.class.getName());
    }

    @Override
    public void handle(EntityChangeEvent event) {
        EntityChangeEvent.ChangeType changeType = event.getChangeType();
        switch (changeType) {
            case UPDATE -> {
                if (event.getClassName().equals(Role.class.getName())) {
                    Long roleId = ObjectValueUtil.getFieldValue(event.getAfter(), "id", Long.class);
                    cache.invalidate(roleId);
                    log.info("RoleOpenapiLoadingCache update. invalid roleId: {}", roleId);
                } else if (event.getClassName().equals(Openapi.class.getName())) {
                    Long apiId = ObjectValueUtil.getFieldValue(event.getAfter(), "id", Long.class);
                    for (Map.Entry<Long, Set<Long>> entry : cache.asMap().entrySet()) {
                        if (entry.getValue().contains(apiId)) {
                            cache.invalidate(entry.getKey());
                            log.info("RoleOpenapiLoadingCache update. invalid apiId: {}, roleId: {}", apiId, entry.getKey());
                        }
                    }
                }
            }
            case DELETE -> {
                if (event.getClassName().equals(Role.class.getName())) {
                    Long roleId = ObjectValueUtil.getFieldValue(event.getBefore(), "id", Long.class);
                    cache.invalidate(roleId);
                    log.info("RoleOpenapiLoadingCache delete. invalid roleId: {}", roleId);
                } else if (event.getClassName().equals(Openapi.class.getName())) {
                    Long apiId = ObjectValueUtil.getFieldValue(event.getBefore(), "id", Long.class);
                    for (Map.Entry<Long, Set<Long>> entry : cache.asMap().entrySet()) {
                        if (entry.getValue().contains(apiId)) {
                            cache.invalidate(entry.getKey());
                            log.info("RoleOpenapiLoadingCache delete. invalid apiId: {}, roleId: {}", apiId, entry.getKey());
                        }
                    }
                }
            }
        }
    }
}
