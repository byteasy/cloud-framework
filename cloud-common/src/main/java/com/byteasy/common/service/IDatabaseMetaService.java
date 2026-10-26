package com.byteasy.common.service;


import com.byteasy.platform.entity.FieldDefine;

import java.util.Collection;

public interface IDatabaseMetaService {

    String getCurrentSchema();

    Collection<FieldDefine> getColumns(String className, String schema, String tableName);

    void syncColumnsToDb(Collection<FieldDefine> columns, String schema, String tableName);
}
