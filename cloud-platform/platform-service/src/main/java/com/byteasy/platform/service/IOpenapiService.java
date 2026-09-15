package com.byteasy.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byteasy.platform.entity.Openapi;

/**
 * <p>
 * 平台接口定义 服务类
 * </p>
 *
 * @author devops
 * @since 2026-09-11
 */
public interface IOpenapiService extends IService<Openapi> {

    Openapi getByUriAndMethod(String actualUri, String uri, Openapi.HttpMethod method);

    Openapi getByCode(String code);
}
