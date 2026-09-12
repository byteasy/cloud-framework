package com.byteasy.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byteasy.platform.entity.RoleResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 平台资源角色授权表 Mapper 接口
 * </p>
 *
 * @author devops
 * @since 2026-04-02
 */
@Mapper
public interface RoleResourceMapper extends BaseMapper<RoleResource> {

    void removeByRoleIdAndGroupId(@Param("roleId") Long roleId, @Param("groupId") Long groupId);

    Set<Long> selectByRoleIdAndGroupId(@Param("roleId") Long roleId, @Param("groupId") Long groupId);
}
