package com.byteasy.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.byteasy.platform.entity.ScriptFunction;

import java.util.List;

/**
 * <p>
 * 脚本函数定义 服务类
 * </p>
 *
 * @author devops
 * @since 2026-09-11
 */
public interface IScriptFunctionService extends IService<ScriptFunction> {

    List<ScriptFunction> register(List<ScriptFunction> scriptFunctions);
}
