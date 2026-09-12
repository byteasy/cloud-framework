package com.byteasy.platform.mapper;

import com.byteasy.platform.entity.ResourceOpenapi;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * OAuth2客户端与平台资源的授权关联表 Mapper 接口
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
@Mapper
public interface ResourceOpenapiMapper extends BaseMapper<ResourceOpenapi> {

}
