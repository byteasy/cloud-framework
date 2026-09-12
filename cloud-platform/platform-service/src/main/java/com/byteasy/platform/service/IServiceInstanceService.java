package com.byteasy.platform.service;

import com.byteasy.platform.entity.ServiceInstance;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 平台服务定义表 服务类
 * </p>
 *
 * @author devops
 * @since 2026-03-03
 */
public interface IServiceInstanceService extends IService<ServiceInstance> {

    ServiceInstance findServiceInstanceByCode(String code);
}
