package com.byteasy.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byteasy.platform.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * <p>
 * 平台角色授权表 Mapper 接口
 * </p>
 *
 * @author devops
 * @since 2026-04-02
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    Set<Long> getUserRoleIdsByUserId(Long userId);
}
