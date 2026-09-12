package com.byteasy.platform.oauth2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byteasy.platform.oauth2.entity.Oauth2RegisteredClient;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * OAuth2 已注册客户端信息表 Mapper 接口
 * </p>
 *
 * @author devops
 * @since 2026-02-22
 */
@Mapper
public interface Oauth2RegisteredClientMapper extends BaseMapper<Oauth2RegisteredClient> {

}
