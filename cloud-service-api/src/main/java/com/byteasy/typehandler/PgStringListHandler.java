package com.byteasy.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * MyBatis TypeHandler：将 PostgreSQL 的 TEXT[] 数组映射为 Java Set<String>
 */
@MappedJdbcTypes(JdbcType.ARRAY)
@MappedTypes(Set.class)
@ConditionalOnClass(name = "org.postgresql.Driver")
public class PgStringListHandler extends AbstractPgArrayTypeHandler<String, List<String>> {

    public PgStringListHandler() {
        super(ArrayList::new);
    }

    @Override
    protected String stringToElement(String s) {
        return s;
    }

    @Override
    protected String elementToString(String t) {
        return t;
    }
}