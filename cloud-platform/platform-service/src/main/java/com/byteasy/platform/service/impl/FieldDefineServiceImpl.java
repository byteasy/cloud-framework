package com.byteasy.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.byteasy.platform.entity.FieldDefine;
import com.byteasy.platform.mapper.FieldDefineMapper;
import com.byteasy.platform.service.IFieldDefineService;
import com.byteasy.common.service.EntityChangeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 列配置表（存储业务实体字段的展示/校验/表单等配置） 服务实现类
 * </p>
 *
 * @author devops
 * @since 2026-03-13
 */
@Service
public class FieldDefineServiceImpl extends EntityChangeService<Long, FieldDefineMapper, FieldDefine> implements IFieldDefineService {

    @Override
    public List<FieldDefine> queryByClassName(String className) {
        LambdaUpdateWrapper<FieldDefine> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FieldDefine::getClassName, className);
        lambdaUpdateWrapper.orderByAsc(FieldDefine::getShowOrder);
        return list(lambdaUpdateWrapper);
    }

    @Override
    public boolean saveOrUpdate(FieldDefine entity) {
        if (entity.getShowOrder() == null) {
            entity.setShowOrder(getMaxShowOrder(entity.getClassName()));
        }
        return super.saveOrUpdate(entity);
    }

    protected Integer getMaxShowOrder(String className) {
        LambdaUpdateWrapper<FieldDefine> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(FieldDefine::getClassName, className);
        wrapper.orderByDesc(FieldDefine::getShowOrder);
        FieldDefine fieldDefine = getOne(wrapper, false);
        if (fieldDefine == null) {
            return 1;
        }
        return fieldDefine.getShowOrder() + 1;
    }

    @Override
    public void syncSchemaMetaInfo(String serviceName, String className, List<FieldDefine> fieldDefines) {
//        Class<?> cls = Class.forName(className);
//        List<Column> columns = ColumnUtil.getColumns(cls).values().stream().sorted(Comparator.comparingInt(Column::getShowOrder)).toList();
        for (FieldDefine fieldDefine : fieldDefines) {
            FieldDefine db = getBy(fieldDefine.getClassName(), fieldDefine.getName());

            if (db == null) {
                save(fieldDefine);
            }
        }
    }

    protected FieldDefine getBy(String className, String fieldName) {
        LambdaUpdateWrapper<FieldDefine> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(FieldDefine::getClassName, className);
        lambdaUpdateWrapper.eq(FieldDefine::getName, fieldName);
        lambdaUpdateWrapper.last("limit 1");
        return getOne(lambdaUpdateWrapper, false);
    }
}
