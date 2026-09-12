package com.byteasy.platform.service;

import com.byteasy.platform.entity.EntityDefine;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 平台模型定义 服务类
 * </p>
 *
 * @author devops
 * @since 2026-05-21
 */
public interface IEntityDefineService extends IService<EntityDefine> {

    EntityDefine findByCode(String code);
}
