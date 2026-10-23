package com.byteasy.platform.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.byteasy.platform.entity.RoleOpenapi;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色接口授权表 服务类
 * </p>
 *
 * @author devops
 * @since 2026-09-16
 */
public interface IRoleOpenapiService extends IService<RoleOpenapi> {

    List<RoleOpenapi> listOpenapiByRoleIds(Set<Long> roleIds);
}
