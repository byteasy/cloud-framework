package com.byteasy.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@MappedJdbcTypes(JdbcType.ARRAY)
@MappedTypes(List.class)
@ConditionalOnClass(name = "org.postgresql.Driver")
public class PgStringSetHandler extends AbstractPgArrayTypeHandler<String, Set<String>> {

    public PgStringSetHandler() {
        super(HashSet::new);
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
