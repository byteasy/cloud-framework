package com.byteasy.common.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.byteasy.util.NameConvertUtil;
import com.byteasy.enums.ColumnType;
import com.byteasy.enums.YesNo;
import com.byteasy.exception.BizException;
import com.byteasy.common.service.IDatabaseMetaService;
import com.byteasy.platform.entity.FieldDefine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostgresMetaServiceImpl implements IDatabaseMetaService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String getCurrentSchema() {
        return jdbcTemplate.queryForObject("SELECT current_schema()", String.class);
    }

    @Override
    public Collection<FieldDefine> getColumns(String className, String schema, String tableName) {
        String sql = """
                SELECT a.attname AS column_name,
                    pg_catalog.format_type(a.atttypid, a.atttypmod) AS data_type,
                    CASE WHEN a.attnotnull THEN 'NO' ELSE 'YES' END AS is_nullable,
                    pg_catalog.pg_get_expr(ad.adbin, ad.adrelid) AS column_default,
                    pg_catalog.col_description(a.attrelid, a.attnum) AS column_comment,
                    CASE WHEN con.contype = 'p' THEN 'YES' ELSE 'NO' END AS is_primary
                FROM
                    pg_catalog.pg_attribute a
                LEFT JOIN
                    pg_catalog.pg_attrdef ad ON a.attrelid = ad.adrelid AND a.attnum = ad.adnum
                INNER JOIN pg_catalog.pg_class c ON a.attrelid = c.oid
                INNER JOIN pg_catalog.pg_namespace n ON c.relnamespace = n.oid
                LEFT JOIN pg_catalog.pg_constraint con ON con.conrelid = c.oid AND a.attnum = ANY(con.conkey) AND con.contype = 'p'
                WHERE n.nspname = ?
                    AND c.relname = ?
                    AND a.attnum > 0 AND NOT a.attisdropped 
                ORDER BY a.attnum
                """;

        List<FieldDefine> list = jdbcTemplate.query(sql, new String[]{schema, tableName}, (rs, rowNum) -> {
            FieldDefine column = new FieldDefine();
            column.setClassName(className);
            column.setFieldName(rs.getString("column_name"));
            column.setName(NameConvertUtil.underlineToCamel(column.getFieldName()));
            JSONObject i18nName = new JSONObject();
            i18nName.set("zh_CN", rs.getString("column_comment"));
            column.setDisplayName(i18nName.toString());
            column.setType(convert(rs.getString("data_type")));
            column.setIsRequire("NO".equals(rs.getString("is_nullable")) ? YesNo.YES : YesNo.NO);
            column.setIsPrimary("YES".equals(rs.getString("is_primary")) ? YesNo.YES : YesNo.NO);
            column.setDesensitize(YesNo.NO);
            column.setEncrypt(YesNo.NO);
            return column;
        });
        int showOrder = 1;
        for (FieldDefine column : list) {
            column.setShowOrder(showOrder++);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncColumnsToDb(Collection<FieldDefine> columns, String schema, String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            throw new BizException("table name is empty");
        }
        if (StringUtils.isEmpty(schema)) {
            schema = getCurrentSchema();
            if (StringUtils.isEmpty(schema)) {
                schema = "public";
            }
        }
        if (columns == null || columns.isEmpty()) {
            return;
        }

        // 1. 判断表是否存在
        String checkTableSql = "SELECT count(1) FROM information_schema.tables WHERE table_schema = ? AND table_name = ?";
        Integer tableCount = jdbcTemplate.queryForObject(checkTableSql, Integer.class, schema, tableName);
        boolean isTableExist = tableCount != null && tableCount > 0;

        String fullTableName = String.format("\"%s\".\"%s\"", schema, tableName);

        if (!isTableExist) {
            // 2. 表不存在：创建新表
            createTable(fullTableName, schema, tableName, columns);
        } else {
            // 3. 表存在：进行 DDL 增量对比与修改
            updateTableColumns(fullTableName, schema, tableName, columns);
        }
    }

    /**
     * 创建新表
     */
    private void createTable(String fullTableName, String schema, String tableName, Collection<FieldDefine> columns) {
        StringBuilder createSql = new StringBuilder("CREATE TABLE ").append(fullTableName).append(" (\n");
        List<String> columnDefs = new ArrayList<>();
        List<String> primaryKeys = new ArrayList<>();
        List<String> commentSqls = new ArrayList<>();

        for (FieldDefine col : columns) {
            String fieldName = col.getFieldName();
            if (StringUtils.isEmpty(fieldName)) {
                fieldName = NameConvertUtil.camelToUnderline(col.getName());
            }
            String colType = convertToPgType(col);
            StringBuilder colDef = new StringBuilder(String.format("  \"%s\" %s", fieldName, colType));

            if (col.getIsRequire() == YesNo.YES || col.getIsPrimary() == YesNo.YES) {
                colDef.append(" NOT NULL");
            }
            columnDefs.add(colDef.toString());

            if (col.getIsPrimary() == YesNo.YES) {
                primaryKeys.add(String.format("\"%s\"", fieldName));
            }

            // 收集列注释
            String comment = extractComment(col);
            if (StringUtils.isNotEmpty(comment)) {
                commentSqls.add(String.format("COMMENT ON COLUMN %s.\"%s\" IS '%s'",
                        fullTableName, fieldName, escapeSql(comment)));
            }
        }

        if (!primaryKeys.isEmpty()) {
            columnDefs.add(String.format("  PRIMARY KEY (%s)", String.join(", ", primaryKeys)));
        }

        createSql.append(String.join(",\n", columnDefs));
        createSql.append("\n);");

        // 执行建表
        jdbcTemplate.execute(createSql.toString());

        // 执行列注释
        for (String commentSql : commentSqls) {
            jdbcTemplate.execute(commentSql);
        }
    }

    /**
     * 对比现有列进行增量更新（新增列、改类型、非空、注释）
     */
    private void updateTableColumns(String fullTableName, String schema, String tableName, Collection<FieldDefine> columns) {
        // 获取数据库现有字段元数据
        Collection<FieldDefine> existingColumns = getColumns(null, schema, tableName);
        Map<String, FieldDefine> existingMap = existingColumns.stream()
                .collect(Collectors.toMap(c -> c.getFieldName().toLowerCase(), c -> c, (k1, k2) -> k1));

        for (FieldDefine col : columns) {
            String fieldName = col.getFieldName();
            if (StringUtils.isEmpty(fieldName)) {
                fieldName = NameConvertUtil.camelToUnderline(col.getName());
            }
            String targetPgType = convertToPgType(col);
            String comment = extractComment(col);

            FieldDefine existCol = existingMap.get(fieldName.toLowerCase());

            if (existCol == null) {
                // 1. 字段不存在：执行 ADD COLUMN
                StringBuilder addColSql = new StringBuilder(String.format("ALTER TABLE %s ADD COLUMN \"%s\" %s",
                        fullTableName, fieldName, targetPgType));
                if (col.getIsRequire() == YesNo.YES || col.getIsPrimary() == YesNo.YES) {
                    addColSql.append(" NOT NULL");
                }
                jdbcTemplate.execute(addColSql.toString());
            } else {
                // 2. 字段已存在：检查类型是否有变动
                if (existCol.getType() != col.getType()) {
                    try {
                        String alterTypeSql = String.format("ALTER TABLE %s ALTER COLUMN \"%s\" TYPE %s USING \"%s\"::%s",
                                fullTableName, fieldName, targetPgType, fieldName, targetPgType);
                        jdbcTemplate.execute(alterTypeSql);
                    } catch (Exception e) {
                        throw new BizException(String.format("修改字段 [%s] 类型从 %s 到 %s 失败: %s",
                                fieldName, existCol.getType(), col.getType(), e.getMessage()));
                    }
                }

                // 3. 检查非空约束变更（🔥 关键修复：排除主键字段，主键在 PG 中禁止 DROP NOT NULL）
                boolean isPrimaryKey = (YesNo.YES.equals(col.getIsPrimary())) || (YesNo.YES.equals(existCol.getIsPrimary()));
                if (!isPrimaryKey && existCol.getIsRequire() != col.getIsRequire()) {
                    if (col.getIsRequire() == YesNo.YES) {
                        jdbcTemplate.execute(String.format("ALTER TABLE %s ALTER COLUMN \"%s\" SET NOT NULL",
                                fullTableName, fieldName));
                    } else if (col.getIsRequire() == YesNo.NO) {
                        jdbcTemplate.execute(String.format("ALTER TABLE %s ALTER COLUMN \"%s\" DROP NOT NULL",
                                fullTableName, fieldName));
                    }
                }
            }

            // 4. 更新列注释
            if (StringUtils.isNotEmpty(comment)) {
                String commentSql = String.format("COMMENT ON COLUMN %s.\"%s\" IS '%s'",
                        fullTableName, fieldName, escapeSql(comment));
                jdbcTemplate.execute(commentSql);
            }
        }
    }

    /**
     * 正向映射：ColumnType -> PostgreSQL DDL 类型
     */
    public String convertToPgType(FieldDefine column) {
        if (column == null || column.getType() == null) {
            return "varchar(255)";
        }
        return switch (column.getType()) {
            case BOOL -> "boolean";
            case BYTE -> "smallint";
            case SHORT -> "smallint";
            case INT -> "integer";
            case LONG -> "bigint";
            case FLOAT -> "real";
            case DOUBLE -> "double precision";
            case STRING -> {
                if (column.getTextareaRows() != null && column.getTextareaRows() > 1) {
                    yield "text";
                }
                Integer maxLen = column.getMaxLength();
                if (maxLen != null && maxLen > 0 && maxLen <= 10485760) {
                    yield "varchar(" + maxLen + ")";
                }
                yield "text";
            }
            case LOCAL_DATE -> "date";
            case LOCAL_DATE_TIME -> "timestamp";
            case ENUM -> "varchar(64)";
            case ARRAY -> "text[]";
            case JSON, JSON_ARRAY -> "jsonb";
            case SPLITTER -> "varchar(255)";
        };
    }

    /**
     * 从 FieldDefine 的 displayName(JSON) 或 description 中提取注释
     */
    private String extractComment(FieldDefine col) {
        if (StringUtils.isNotEmpty(col.getDisplayName())) {
            try {
                if (JSONUtil.isTypeJSON(col.getDisplayName())) {
                    JSONObject obj = JSONUtil.parseObj(col.getDisplayName());
                    String zh = obj.getStr("zh_CN");
                    if (StringUtils.isNotEmpty(zh)) {
                        return zh;
                    }
                }
                return col.getDisplayName();
            } catch (Exception ignored) {
                return col.getDisplayName();
            }
        }
        return col.getDescription();
    }

    private String escapeSql(String val) {
        return val == null ? "" : val.replace("'", "''");
    }

    protected ColumnType convert(String dbFieldType) {
        String lower = dbFieldType.toLowerCase();
        if (lower.equals("integer") || lower.equals("int4") || lower.equals("int")) {
            return ColumnType.INT;
        } else if (lower.equals("smallint") || lower.equals("int2")) {
            return ColumnType.SHORT;
        } else if (lower.equals("boolean") || lower.equals("bool")) {
            return ColumnType.BOOL;
        } else if (lower.contains("timestamp")) {
            return ColumnType.LOCAL_DATE_TIME;
        } else if (lower.contains("character varying") || lower.contains("varchar")) {
            return ColumnType.STRING;
        } else if (lower.equals("text") || lower.equals("uuid")) {
            return ColumnType.STRING;
        } else if (lower.contains("[]") || lower.equals("text[]")) {
            return ColumnType.ARRAY;
        } else if (lower.equals("date")) {
            return ColumnType.LOCAL_DATE;
        } else if (lower.equals("jsonb") || lower.equals("json")) {
            return ColumnType.JSON;
        } else if (lower.equals("bigint") || lower.equals("int8")) {
            return ColumnType.LONG;
        } else if (lower.equals("double precision") || lower.equals("double") || lower.equals("float8")) {
            return ColumnType.DOUBLE;
        } else if (lower.equals("real") || lower.equals("float4") || lower.equals("float")) {
            return ColumnType.FLOAT;
        }
        throw new BizException("Postgres dataType: " + dbFieldType + " undefined.");
    }
}