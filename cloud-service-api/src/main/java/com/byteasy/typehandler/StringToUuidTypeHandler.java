package com.byteasy.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.sql.*;
import java.util.UUID;

/**
 * 兼容处理 PostgreSQL UUID 与 Java (String / UUID) 的类型转换
 */
@MappedTypes({String.class, UUID.class, Object.class})
@MappedJdbcTypes(JdbcType.OTHER)
@ConditionalOnClass(name = "org.postgresql.Driver")
public class StringToUuidTypeHandler extends BaseTypeHandler<Object> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        if (parameter instanceof UUID uuid) {
            // 如果传入的已经是 UUID 对象，直接存入 PostgreSQL
            ps.setObject(i, uuid, Types.OTHER);
        } else if (parameter instanceof String str) {
            if (str.isBlank()) {
                ps.setNull(i, Types.OTHER);
            } else {
                ps.setObject(i, UUID.fromString(str.trim()), Types.OTHER);
            }
        } else {
            // 其他兜底转换为字符串再解析为 UUID
            ps.setObject(i, UUID.fromString(parameter.toString()), Types.OTHER);
        }
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toResult(rs.getObject(columnName));
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toResult(rs.getObject(columnIndex));
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toResult(cs.getObject(columnIndex));
    }

    private Object toResult(Object obj) {
        if (obj == null) {
            return null;
        }
        // 如果数据库驱动返回的是 UUID，转为 String（若你的 Entity 属性是 String）
        // 若你的 Entity 属性是 UUID，直接返回 obj 也可以
        if (obj instanceof UUID) {
            return obj.toString();
        }
        return obj.toString();
    }
}