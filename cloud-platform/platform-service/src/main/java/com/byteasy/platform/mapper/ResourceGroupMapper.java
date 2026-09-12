package com.byteasy.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byteasy.platform.entity.ResourceGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 平台资源分组定义表（用于对资源进行分类管理，如：系统管理、业务管理、报表管理等） Mapper 接口
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
@Mapper
public interface ResourceGroupMapper extends BaseMapper<ResourceGroup> {

    List<ResourceGroup> selectByRoleIds(Set<Long> roleIds);
}
