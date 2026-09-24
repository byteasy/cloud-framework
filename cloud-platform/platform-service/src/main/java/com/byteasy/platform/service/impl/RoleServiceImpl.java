package com.byteasy.platform.service.impl;

import com.byteasy.common.service.EntityChangeService;
import com.byteasy.platform.entity.Role;
import com.byteasy.platform.mapper.RoleMapper;
import com.byteasy.platform.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 平台角色定义表 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-04-02
 */
@Service
public class RoleServiceImpl extends EntityChangeService<Long, RoleMapper, Role> implements IRoleService {

}
