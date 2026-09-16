package com.byteasy.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteasy.common.util.CastUtil;
import com.byteasy.platform.entity.ScriptFunction;
import com.byteasy.platform.mapper.ScriptFunctionMapper;
import com.byteasy.platform.service.IScriptFunctionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 脚本函数定义 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-09-11
 */
@Service
public class ScriptFunctionServiceImpl extends ServiceImpl<ScriptFunctionMapper, ScriptFunction> implements IScriptFunctionService {

    @Override
    @Transactional
    public List<ScriptFunction> register(List<ScriptFunction> scriptFunctions) {
        LambdaUpdateWrapper<ScriptFunction> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.in(ScriptFunction::getName, scriptFunctions.stream().map(ScriptFunction::getName).collect(Collectors.toSet()));
        List<ScriptFunction> dbFunctions = list(lambdaUpdateWrapper);

        Map<String, ScriptFunction> dbMap = CastUtil.listToMap(dbFunctions, ScriptFunction::getName);

        List<ScriptFunction> dbNotExists = scriptFunctions.stream().filter(v -> !dbMap.containsKey(v.getName())).toList();
        saveBatch(dbNotExists);

        List<ScriptFunction> needUpdates = scriptFunctions.stream()
                .filter(v -> dbMap.containsKey(v.getName()))
                .filter(v -> !dbMap.get(v.getName()).getDescription().equals(v.getDescription())).map(
                        v -> {
                            ScriptFunction dbRow = dbMap.get(v.getName());
                            dbRow.setDescription(v.getDescription());
                            return dbRow;
                        }
                ).toList();
        updateBatchById(needUpdates);
        return list();
    }
}
