package com.byteasy.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byteasy.platform.entity.OpenapiLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 接口执行日志记录表 Mapper 接口
 * </p>
 *
 * @author devops
 * @since 2026-09-17
 */
@Mapper
public interface OpenapiLogMapper extends BaseMapper<OpenapiLog> {

}
