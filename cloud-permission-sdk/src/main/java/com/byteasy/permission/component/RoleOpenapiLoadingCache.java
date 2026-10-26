package com.byteasy.permission.component;

import com.byteasy.component.ICacheLoader;
import com.byteasy.platform.api.RoleOpenapiClient;
import com.byteasy.platform.entity.Openapi;
import com.byteasy.platform.entity.Role;
import com.byteasy.platform.entity.RoleOpenapi;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
@Log4j2
public class RoleOpenapiLoadingCache implements ICacheLoader<Long, Set<Long>> {

    @Autowired
    private RoleOpenapiClient roleOpenapiService;

    @Override
    public Set<Long> load(Long key) throws ExecutionException {
        List<RoleOpenapi> list = roleOpenapiService.listOpenapiByRoleIds(Set.of(key)).getIfPresent();
        if (list == null) {
            return Collections.emptySet();
        }
        return list.stream().map(RoleOpenapi::getOpenapiId).collect(Collectors.toSet());
    }

    @Override
    public String type() {
        return StringUtils.joinWith(",", Role.class.getName(), Openapi.class.getName());
    }

}
