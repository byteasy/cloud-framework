package com.byteasy.permission.component;

import com.byteasy.component.ICacheLoader;
import com.byteasy.platform.api.UserClient;
import com.byteasy.platform.dto.UserRoleDTO;
import com.byteasy.platform.entity.Role;
import com.byteasy.platform.entity.User;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Component
@Log4j2
public class UserRoleLoadingCache implements ICacheLoader<String, Set<Long>> {

    @Autowired
    private UserClient userService;

    @Override
    public Set<Long> load(String key) throws ExecutionException {
        Map<String, Object> params = new HashMap<>();
        params.put("username", key);
        params.put("usernameQueryType", "eq");
        UserRoleDTO userRoleDTO = userService.getUserRoleInfo(params).getIfPresent();
        if (userRoleDTO == null) {
            return Collections.emptySet();
        }
        return userRoleDTO.getRoleIds();
    }

    @Override
    public String type() {
        return StringUtils.joinWith(",", Role.class.getName(), User.class.getName());
    }

}
