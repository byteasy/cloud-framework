package com.byteasy.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteasy.platform.entity.OpenapiLog;
import com.byteasy.platform.mapper.OpenapiLogMapper;
import com.byteasy.platform.service.IOpenapiLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 接口执行日志记录表 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-09-17
 */
@Service
public class OpenapiLogServiceImpl extends ServiceImpl<OpenapiLogMapper, OpenapiLog> implements IOpenapiLogService {

}
