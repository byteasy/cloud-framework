package com.byteasy.platform.service.impl;

import com.byteasy.platform.entity.LoginLog;
import com.byteasy.platform.mapper.LoginLogMapper;
import com.byteasy.platform.service.ILoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录日志 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-05-06
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements ILoginLogService {

}
