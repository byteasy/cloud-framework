package com.byteasy.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.sql.*;
import java.util.UUID;

// 映射 Java 类型为 String，JDBC 类型为 UUID
@MappedTypes(String.class)
@MappedJdbcTypes(JdbcType.OTHER) // PostgreSQL 的 UUID 对应 JDBCType.OTHER
@ConditionalOnClass(name = "org.postgresql.Driver")
public class StringToUuidTypeHandler extends BaseTypeHandler<String> {

    // 设置参数时，将字符串转换为 UUID
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null || parameter.isEmpty()) {
            ps.setNull(i, Types.OTHER);
        } else {
            // 将字符串转为 UUID 并设置到 PreparedStatement
            ps.setObject(i, UUID.fromString(parameter), Types.OTHER);
        }
    }

    // 以下方法为查询时的转换（可选，根据需求实现）
    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        UUID uuid = (UUID) rs.getObject(columnName);
        return uuid == null ? null : uuid.toString();
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        UUID uuid = (UUID) rs.getObject(columnIndex);
        return uuid == null ? null : uuid.toString();
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        UUID uuid = (UUID) cs.getObject(columnIndex);
        return uuid == null ? null : uuid.toString();
    }
}
