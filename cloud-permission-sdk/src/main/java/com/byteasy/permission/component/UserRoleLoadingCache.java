package com.byteasy.permission.component;

import com.byteasy.component.ICacheLoader;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.EntityChangeSource;
import com.byteasy.event.IEntityChangeHandler;
import com.byteasy.platform.api.IUserService;
import com.byteasy.platform.dto.UserRoleDTO;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.platform.entity.Role;
import com.byteasy.platform.entity.User;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Component
@Log4j2
public class UserRoleLoadingCache implements IEntityChangeHandler, ICacheLoader<String, Set<Long>> {

    @Autowired
    private IUserService userService;

    private LoadingCache<String /* username */, Set<Long>> cache = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(3)).maximumSize(5000).build(new CacheLoader<>() {
        @Override
        public Set<Long> load(String key) {
            Map<String, Object> params = new HashMap<>();
            params.put("username", key);
            params.put("usernameQueryType", "eq");
            UserRoleDTO userRoleDTO = userService.getUserRoleInfo(params).getIfPresent();
            if (userRoleDTO == null) {
                return Collections.emptySet();
            }
            return userRoleDTO.getRoleIds();
        }
    });

    @Override
    public Set<Long> load(String key) throws ExecutionException {
        return cache.get(key);
    }

    @Override
    public String type() {
        return StringUtils.joinWith(",", Role.class.getName(), User.class.getName());
    }

    @Override
    public void handle(EntityChangeEvent event) {
        EntityChangeSource.ChangeType changeType = event.getSource().getChangeType();
        EntityChangeSource changeSource = event.getSource();
        switch (changeType) {
            case UPDATE -> {
                if (changeSource.getClassName().equals(Role.class.getName())) {
                    Long roleId = FieldValueUtil.getFieldValue(changeSource.getAfter(), "id", Long.class);
                    for (Map.Entry<String, Set<Long>> entry : cache.asMap().entrySet()) {
                        if (entry.getValue().contains(roleId)) {
                            cache.invalidate(entry.getKey());
                            log.info("UserRoleLoadingCache update. invalid roleId: {}, roleId: {}", roleId, entry.getKey());
                        }
                    }
                } else if (changeSource.getClassName().equals(User.class.getName())) {
                    String username = FieldValueUtil.getFieldValue(changeSource.getAfter(), "username", String.class);
                    cache.invalidate(username);
                    log.info("UserRoleLoadingCache update. invalid username: {}", username);
                }
            }
            case DELETE -> {
                if (changeSource.getClassName().equals(Role.class.getName())) {
                    Long roleId = FieldValueUtil.getFieldValue(changeSource.getBefore(), "id", Long.class);
                    for (Map.Entry<String, Set<Long>> entry : cache.asMap().entrySet()) {
                        if (entry.getValue().contains(roleId)) {
                            cache.invalidate(entry.getKey());
                            log.info("UserRoleLoadingCache delete. invalid roleId: {}, username: {}", roleId, entry.getKey());
                        }
                    }
                } else if (changeSource.getClassName().equals(User.class.getName())) {
                    String username = FieldValueUtil.getFieldValue(changeSource.getBefore(), "username", String.class);
                    cache.invalidate(username);
                    log.info("UserRoleLoadingCache delete. invalid username: {}", username);
                }
            }
        }
    }
}
