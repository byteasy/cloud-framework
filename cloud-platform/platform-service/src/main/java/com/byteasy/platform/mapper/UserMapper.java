package com.byteasy.platform.mapper;

import com.byteasy.platform.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 通用平台用户表 Mapper 接口
 * </p>
 *
 * @author devops
 * @since 2026-02-27
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
