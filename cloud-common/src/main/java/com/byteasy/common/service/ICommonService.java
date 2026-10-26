package com.byteasy.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.platform.entity.FieldDefine;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ICommonService {

    Map<String, Object> saveOrUpdate(String className, Map<String, Object> entity);

    boolean saveOrUpdateBatch(String className, List<Map<String, Object>> entities);

    Map<String, Object> getByPrimaryKey(String className, Object primaryKeyValue);

    Page<Map<String, Object>> page(String className);

    List<Map<String, Object>> list(String className);

    List<Map<String, Object>> list(String className, Map<String, Object> args);

    Map<String, Object> getOne(String className, Map<String, Object> args);

    boolean removeByPrimaryKey(String className, Object primaryKey);

    boolean removeByPrimaryKey(String className, Map<String, Object> entity);

    boolean batchDelete(String className, List<Map<String, Object>> entities);

    Collection<FieldDefine> getColumns(String className);

    void syncColumnsToDb(String className);

    Collection<FieldDefine> getColumnsByTableName(String tableName);
}
