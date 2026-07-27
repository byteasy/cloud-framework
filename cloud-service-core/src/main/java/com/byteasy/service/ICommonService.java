package com.byteasy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.dto.Column;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ICommonService {

    Map<String, Object> saveOrUpdate(String className, Map<String, Object> entity);

    boolean saveOrUpdateBatch(String className, List<Map<String, Object>> entities);

    Map<String, Object> getById(String className, Object id);

    Page<Map<String, Object>> page(String className);

    List<Map<String, Object>> list(String className);

    List<Map<String, Object>> list(String className, Map<String, Object> args);

    Map<String, Object> getOne(String className, Map<String, Object> args);

    boolean removeById(String className, Object id);

    Collection<Column> getColumns(String className);

    Collection<Column> getColumnsByTableName(String tableName);
}
