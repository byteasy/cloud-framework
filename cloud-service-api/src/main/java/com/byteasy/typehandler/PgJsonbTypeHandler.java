package com.byteasy.typehandler;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * PostgreSQL JSONB 类型处理器，支持 Java 8 时间类型
 */
public class PgJsonbTypeHandler extends JacksonTypeHandler {

    public PgJsonbTypeHandler(Class<?> type) {
        super(type);
    }

    public PgJsonbTypeHandler(Class<?> type, Field field) {
        super(type, field);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        PGobject jsonObject = new PGobject();
        jsonObject.setType("jsonb");                // ⭐️ 关键：声明类型为 jsonb
        jsonObject.setValue(toJson(parameter));
        ps.setObject(i, jsonObject);                 // 使用 setObject 传递 PGobject
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 获取数据库返回的对象（可能是 PGobject）
        Object obj = rs.getObject(columnName);
        return convertToObject(obj);
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object obj = rs.getObject(columnIndex);
        return convertToObject(obj);
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object obj = cs.getObject(columnIndex);
        return convertToObject(obj);
    }

    /**
     * 将数据库返回的对象转换为 Java 对象
     */
    private Object convertToObject(Object obj) {
        if (obj == null) {
            return null;
        }
        String jsonString;
        if (obj instanceof PGobject) {
            jsonString = ((PGobject) obj).getValue();
        } else {
            // 某些驱动或配置下可能直接返回字符串
            jsonString = obj.toString();
        }
        // 调用父类的 parse 方法将 JSON 字符串转换为目标类型
        return parse(jsonString);
    }
}
