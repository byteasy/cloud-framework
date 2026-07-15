package com.byteasy.service;

import cn.hutool.json.JSONObject;
import com.byteasy.common.util.NameConvertUtil;
import com.byteasy.dto.Column;
import com.byteasy.enums.ColumnType;
import com.byteasy.enums.YesNo;
import com.byteasy.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class PostgresMetaServiceImpl implements IDatabaseMetaService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String getCurrentSchema() {
        return jdbcTemplate.queryForObject("SELECT current_schema()", String.class);
    }

    @Override
    public Collection<Column> getColumns(String className, String schema, String tableName) {
        String sql = String.format("""
                SELECT a.attname AS column_name,
                    pg_catalog.format_type(a.atttypid, a.atttypmod) AS data_type,
                    CASE WHEN a.attnotnull THEN 'NO' ELSE 'YES' END AS is_nullable,
                    pg_catalog.pg_get_expr(ad.adbin, ad.adrelid) AS column_default,
                    pg_catalog.col_description(a.attrelid, a.attnum) AS column_comment
                FROM
                    pg_catalog.pg_attribute a
                LEFT JOIN
                    pg_catalog.pg_attrdef ad ON a.attrelid = ad.adrelid AND a.attnum = ad.adnum
                INNER JOIN pg_catalog.pg_class c ON a.attrelid = c.oid
                INNER JOIN pg_catalog.pg_namespace n ON c.relnamespace = n.oid
                WHERE n.nspname = ?
                    AND c.relname = ?
                    AND a.attnum > 0 AND NOT a.attisdropped ORDER BY a.attnum
                """, schema, tableName);

        List<Column> list = jdbcTemplate.query(sql, new String[]{schema, tableName}, (rs, rowNum) -> {
            Column column = new Column();
            column.setClassName(className);
            column.setFieldName(rs.getString("column_name"));
            column.setName(NameConvertUtil.underlineToCamel(column.getFieldName()));
            JSONObject i18nName = new JSONObject();
            i18nName.set("zh_CN", rs.getString("column_comment"));
            column.setDisplayName(i18nName.toString());
            column.setType(convert(rs.getString("data_type")));
            column.setIsRequire("NO".equals(rs.getString("is_nullable")) ? YesNo.YES : YesNo.NO);
            return column;
        });
        int showOrder = 1;
        for (Column column : list) {
            column.setShowOrder(showOrder++);
        }
        return list;
    }

    protected ColumnType convert(String dbFieldType) {
        if (dbFieldType.equalsIgnoreCase("integer")) {
            return ColumnType.INT;
        } else if (dbFieldType.equalsIgnoreCase("smallint")) {
            return ColumnType.SHORT;
        } else if (dbFieldType.equalsIgnoreCase("boolean")) {
            return ColumnType.BOOL;
        } else if (dbFieldType.contains("timestamp")) {
            return ColumnType.LOCAL_DATE_TIME;
        } else if (dbFieldType.contains("character varying")) {
            return ColumnType.STRING;
        } else if (dbFieldType.equalsIgnoreCase("text")) {
            return ColumnType.STRING;
        } else if (dbFieldType.equalsIgnoreCase("uuid")) {
            return ColumnType.STRING;
        } else if (dbFieldType.equalsIgnoreCase("text[]")) {
            return ColumnType.ARRAY;
        } else if (dbFieldType.equalsIgnoreCase("date")) {
            return ColumnType.LOCAL_DATE;
        } else if (dbFieldType.equalsIgnoreCase("jsonb")) {
            return ColumnType.JSON;
        } else if (dbFieldType.equalsIgnoreCase("bigint")) {
            return ColumnType.LONG;
        } else if (dbFieldType.equalsIgnoreCase("double")) {
            return ColumnType.DOUBLE;
        } else if (dbFieldType.equalsIgnoreCase("float")) {
            return ColumnType.FLOAT;
        }
        throw new BizException("Postgres dataType: " + dbFieldType + " undefined.");
    }
}
