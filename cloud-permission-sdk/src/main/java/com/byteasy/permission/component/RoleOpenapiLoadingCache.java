package com.byteasy.permission.component;

import com.byteasy.component.ICacheLoader;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.EntityChangeSource;
import com.byteasy.event.IEntityChangeHandler;
import com.byteasy.platform.api.IRoleOpenapiService;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.platform.entity.Role;
import com.byteasy.platform.entity.RoleOpenapi;
import com.byteasy.util.FieldValueUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
@Log4j2
public class RoleOpenapiLoadingCache implements IEntityChangeHandler, ICacheLoader<Long, Set<Long>> {

    @Autowired
    private IRoleOpenapiService roleOpenapiService;

    private LoadingCache<Long, Set<Long>> cache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(3)).maximumSize(5000).build(new CacheLoader<>() {
        @Override
        public @Nullable Set<Long> load(Long key) {
            List<RoleOpenapi> list = roleOpenapiService.listOpenapiByRoleIds(Set.of(key)).getIfPresent();
            if (list == null) {
                return null;
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
        EntityChangeSource.ChangeType changeType = event.getSource().getChangeType();
        EntityChangeSource changeSource = event.getSource();
        switch (changeType) {
            case UPDATE -> {
                if (changeSource.getClassName().equals(Role.class.getName())) {
                    Long roleId = FieldValueUtil.getFieldValue(changeSource.getAfter(), "id", Long.class);
                    cache.invalidate(roleId);
                    log.info("RoleOpenapiLoadingCache update. invalid roleId: {}", roleId);
                } else if (changeSource.getClassName().equals(Openapi.class.getName())) {
                    Long apiId = FieldValueUtil.getFieldValue(changeSource.getAfter(), "id", Long.class);
                    for (Map.Entry<Long, Set<Long>> entry : cache.asMap().entrySet()) {
                        if (entry.getValue().contains(apiId)) {
                            cache.invalidate(entry.getKey());
                            log.info("RoleOpenapiLoadingCache update. invalid apiId: {}, roleId: {}", apiId, entry.getKey());
                        }
                    }
                }
            }
            case DELETE -> {
                if (changeSource.getClassName().equals(Role.class.getName())) {
                    Long roleId = FieldValueUtil.getFieldValue(changeSource.getBefore(), "id", Long.class);
                    cache.invalidate(roleId);
                    log.info("RoleOpenapiLoadingCache delete. invalid roleId: {}", roleId);
                } else if (changeSource.getClassName().equals(Openapi.class.getName())) {
                    Long apiId = FieldValueUtil.getFieldValue(changeSource.getBefore(), "id", Long.class);
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
