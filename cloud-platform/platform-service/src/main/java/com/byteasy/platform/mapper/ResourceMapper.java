package com.byteasy.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byteasy.platform.entity.Resource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 平台资源定义表（菜单/目录/按钮权限） Mapper 接口
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

    List<Resource> selectByRoleIds(Set<Long> roleIds, Long groupId);

    List<Resource> selectByRoleIdsAndGroupIds(Set<Long> roleIds, Set<Long> groupIds);
}
