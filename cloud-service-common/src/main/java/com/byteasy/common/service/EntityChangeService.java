package com.byteasy.common.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.byteasy.Constant;
import com.byteasy.common.event.EntityChangeFactory;
import com.byteasy.common.util.JacksonUtil2;
import com.byteasy.entity.Entity;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.exception.BizException;
import com.byteasy.mq.IProducer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@Log4j2
public abstract class EntityChangeService<ID, M extends BaseMapper<T>, T extends Entity<ID>> extends ServiceImpl<M, T> {

    @Autowired
    private IProducer producer;
    @Autowired
    private EntityChangeFactory entityChangeFactory;

    protected void publishEvent(EntityChangeEvent event) {
        producer.publish(Constant.TOPIC_PLATFORM_ENTITY_CHANGE, getEntityClass().getName(), JacksonUtil2.toJson(event, true));
        entityChangeFactory.handle(event);
    }

    protected void doHandleSaveOrUpdate(EntityChangeEvent.ChangeType changeType, T before, T after) {
        publishEvent(new EntityChangeEvent(changeType, before, after, getEntityClass().getName()));
    }

    protected EntityChangeEvent.ChangeType getChangeType(T entity) {
        Object idVal = getIdVal(entity);
        if (StringUtils.checkValNull(idVal) || Objects.isNull(baseMapper.selectById((Serializable) idVal))) {
            return EntityChangeEvent.ChangeType.CREATE;
        } else {
            return EntityChangeEvent.ChangeType.UPDATE;
        }
    }

    protected Object getIdVal(T entity) {
        Class<?> entityClass = getEntityClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
        return tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty());
    }

    protected void doHandleDelete(T entity) {
        publishEvent(new EntityChangeEvent(EntityChangeEvent.ChangeType.DELETE, entity, null, getEntityClass().getName()));
    }

    protected void doHandleDelete(Serializable id) {
        publishEvent(new EntityChangeEvent(EntityChangeEvent.ChangeType.DELETE, getById(id), null, getEntityClass().getName()));
    }

    @Override
    public boolean saveOrUpdate(T entity) {
        boolean result = false;
        EntityChangeEvent.ChangeType changeType = getChangeType(entity);
        if (EntityChangeEvent.ChangeType.CREATE.equals(changeType)) {
            result = super.saveOrUpdate(entity);
            /**
             * 回填了ID，此时再调用，变更数据更全面
             */
            doHandleSaveOrUpdate(changeType, null, entity);
        } else {
            /**
             * 修改类操作，先通知变更，再保存
             */
            T before = getById((Serializable) getIdVal(entity));
            result = super.saveOrUpdate(entity);
            doHandleSaveOrUpdate(changeType, before, getById((Serializable) getIdVal(entity)));
        }
        return result;
    }

    @Override
    public boolean save(T entity) {
        boolean result = super.save(entity);
        doHandleSaveOrUpdate(EntityChangeEvent.ChangeType.CREATE, null, entity);
        return result;
    }

    @Override
    public boolean saveBatch(Collection<T> entityList) {
        boolean result = super.saveBatch(entityList);
        for (T t : entityList) {
            doHandleSaveOrUpdate(EntityChangeEvent.ChangeType.CREATE, null, t);
        }
        return result;
    }

    @Override
    @Transactional
    public boolean saveOrUpdateBatch(Collection<T> entityList) {
        boolean result = true;
        for (T t : entityList) {
            EntityChangeEvent.ChangeType changeType = getChangeType(t);
            if (EntityChangeEvent.ChangeType.CREATE.equals(changeType)) {
                if (!super.save(t)) {
                    result = false;
                }
                doHandleSaveOrUpdate(EntityChangeEvent.ChangeType.CREATE, null, t);
            } else {
                T before = getById((Serializable) getIdVal(t));
                if (!super.saveOrUpdate(t)) {
                    result = false;
                }
                doHandleSaveOrUpdate(EntityChangeEvent.ChangeType.UPDATE, before, getById((Serializable) getIdVal(t)));
            }
        }
        return result;
    }

    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        boolean result = super.saveBatch(entityList, batchSize);
        for (T t : entityList) {
            doHandleSaveOrUpdate(EntityChangeEvent.ChangeType.CREATE, null, t);
        }
        return result;
    }

    @Override
    @Transactional
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        return this.saveOrUpdateBatch(entityList);
    }

    @Override
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        boolean result = true;
        for (T t : entityList) {
            Object idVal = getIdVal(t);
            T before = getById((Serializable) idVal);
            if (!updateById(t)) {
                result = false;
            }
            doHandleSaveOrUpdate(EntityChangeEvent.ChangeType.UPDATE, before, getById((Serializable) idVal));
        }
        return result;
    }

    @Override
    public boolean removeById(Serializable id, boolean useFill) {
        doHandleDelete(id);
        return super.removeById(id, useFill);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {
        for (Object o : list) {
            doHandleDelete((Serializable) o);
        }
        return super.removeBatchByIds(list);
    }

    @Override
    public boolean removeById(Serializable id) {
        doHandleDelete(id);
        return super.removeById(id);
    }

    @Override
    public boolean removeById(T entity) {
        doHandleDelete(entity);
        return super.removeById(entity);
    }

    @Override
    public boolean removeByMap(Map<String, Object> columnMap) {
        if (columnMap != null) {
            throw new BizException("不支持此方法");
        }
        return super.removeByMap(columnMap);
    }

    @Override
    public boolean remove(Wrapper<T> queryWrapper) {
        if (queryWrapper != null) {
            throw new BizException("不支持此方法");
        }
        return super.remove(queryWrapper);
    }

    @Override
    public boolean removeByIds(Collection<?> list) {
        for (Object o : list) {
            doHandleDelete((Serializable) o);
        }
        return super.removeByIds(list);
    }

    @Override
    public boolean removeByIds(Collection<?> list, boolean useFill) {
        for (Object o : list) {
            doHandleDelete((Serializable) o);
        }
        return super.removeByIds(list, useFill);
    }

    @Override
    public boolean updateById(T entity) {
        T before = getById(entity);
        boolean ret = super.updateById(entity);
        doHandleSaveOrUpdate(EntityChangeEvent.ChangeType.UPDATE, before, getById(entity));
        return ret;
    }

    @Override
    public boolean update(Wrapper<T> updateWrapper) {
        if (updateWrapper != null) {
            throw new BizException("不支持此方法");
        }
        return super.update(updateWrapper);
    }

    @Override
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        if (updateWrapper != null) {
            throw new BizException("不支持此方法");
        }
        return super.update(entity, updateWrapper);
    }

    @Override
    @Transactional
    public boolean updateBatchById(Collection<T> entityList) {
        return updateBatchById(entityList, 0);
    }
}
