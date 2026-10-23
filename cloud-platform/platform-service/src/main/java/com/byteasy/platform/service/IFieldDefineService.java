package com.byteasy.platform.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.byteasy.platform.entity.FieldDefine;

import java.util.List;

/**
 * <p>
 * 列配置表（存储业务实体字段的展示/校验/表单等配置） 服务类
 * </p>
 *
 * @author devops
 * @since 2026-03-13
 */
public interface IFieldDefineService extends IService<FieldDefine> {

    List<FieldDefine> queryByClassName(String className);

    void syncSchemaMetaInfo(String serviceName, String className, List<FieldDefine> fieldDefines);
}
