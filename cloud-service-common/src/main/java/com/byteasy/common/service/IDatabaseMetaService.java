package com.byteasy.common.service;

import com.byteasy.dto.Column;

import java.util.Collection;

public interface IDatabaseMetaService {

    String getCurrentSchema();

    Collection<Column> getColumns(String className, String schema, String tableName);

    void syncColumnsToDb(Collection<Column> columns, String schema, String tableName);
}
