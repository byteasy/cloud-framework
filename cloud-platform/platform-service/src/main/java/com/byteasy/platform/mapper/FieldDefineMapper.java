package com.byteasy.platform.mapper;

import com.byteasy.platform.entity.FieldDefine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 列配置表（存储业务实体字段的展示/校验/表单等配置） Mapper 接口
 * </p>
 *
 * @author devops
 * @since 2026-03-13
 */
@Mapper
public interface FieldDefineMapper extends BaseMapper<FieldDefine> {

}
