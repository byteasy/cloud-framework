package com.byteasy.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byteasy.platform.entity.RoleOpenapi;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色接口授权表 Mapper 接口
 * </p>
 *
 * @author devops
 * @since 2026-09-16
 */
@Mapper
public interface RoleOpenapiMapper extends BaseMapper<RoleOpenapi> {

    List<RoleOpenapi> listOpenapiByRoleIds(Set<Long> roleIds);
}
