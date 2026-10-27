package com.byteasy.permission.component;

import com.byteasy.component.ICacheLoader;
import com.byteasy.infinispan.util.CacheEvictHelper;
import com.byteasy.platform.Constant;
import com.byteasy.platform.api.UserClient;
import com.byteasy.platform.dto.RoleUserDTO;
import com.byteasy.platform.dto.UserRoleDTO;
import com.byteasy.platform.entity.Role;
import com.byteasy.platform.entity.User;
import com.byteasy.platform.entity.UserRole;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class UserRoleLoadingCache implements ICacheLoader<String, UserRoleDTO> {

    @Autowired
    private UserClient userClient;
    @Autowired
    private CacheEvictHelper cacheEvictHelper;

    @Override
    @Cacheable(cacheNames = Constant.PLATFORM_REP_CACHE, key = "'com.byteasy.platform.entity.UserRole:' + #key")
    public UserRoleDTO load(String key) {
        Map<String, Object> params = new HashMap<>();
        params.put("username", key);
        params.put("usernameQueryType", "eq");
        return userClient.getUserRoleInfo(params).getIfPresent();
    }

    @Override
    public void invalid(Object entity) {
        if (entity instanceof User) {
            User user = (User) entity;
            cacheEvictHelper.evictAfterCommit(Constant.PLATFORM_REP_CACHE, UserRole.class.getName() + ":" + user.getUsername());
        } else if (entity instanceof Role) {
            Role role = (Role) entity;
            RoleUserDTO roleUserDTO = userClient.getUsersByRoleId(role.getId()).getIfPresent();
            if (roleUserDTO != null) {
                for (String userName : roleUserDTO.getUserNames()) {
                    cacheEvictHelper.evictAfterCommit(Constant.PLATFORM_REP_CACHE, UserRole.class.getName() + ":" + userName);
                }
            }
        }
    }

    @Override
    public String cacheType() {
        return UserRoleDTO.class.getName();
    }

    @Override
    public String[] invalidType() {
        return new String[]{User.class.getName(), Role.class.getName()};
    }

}
