package com.byteasy.platform.service;

import com.baomidou.mybatisplus.spring.service.IService;
import com.byteasy.Result;
import com.byteasy.platform.entity.OpenapiLog;

import java.io.IOException;

/**
 * <p>
 * 接口执行日志记录表 服务类
 * </p>
 *
 * @author devops
 * @since 2026-09-17
 */
public interface IOpenapiLogService extends IService<OpenapiLog> {

    Result replay(Long logId) throws IOException;
}
