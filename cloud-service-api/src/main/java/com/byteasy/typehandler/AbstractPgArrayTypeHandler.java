package com.byteasy.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 通用 PostgreSQL TEXT[] 到 Collection<T> 的 TypeHandler 基类
 *
 * @param <T> 元素类型，如 String, AuthorizationGrantType
 * @param <C> 集合类型，如 List<T>, Set<T>
 */
public abstract class AbstractPgArrayTypeHandler<T, C extends Collection<T>> extends BaseTypeHandler<C> {

    private final Supplier<C> collectionFactory;

    /**
     * 构造函数：传入如何创建空集合
     */
    public AbstractPgArrayTypeHandler(Supplier<C> collectionFactory) {
        this.collectionFactory = Objects.requireNonNull(collectionFactory);
    }

    protected abstract T stringToElement(String s);

    protected abstract String elementToString(T t);

    private C createEmptyCollection() {
        return collectionFactory.get();
    }

    // ========== 以下为通用实现，子类无需关心 ==========

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, C parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null || parameter.isEmpty()) {
            ps.setArray(i, ps.getConnection().createArrayOf("text", new String[0]));
        } else {
            String[] array = parameter.stream()
                    .filter(Objects::nonNull)
                    .map(this::elementToString)
                    .toArray(String[]::new);
            ps.setArray(i, ps.getConnection().createArrayOf("text", array));
        }
    }

    @Override
    public C getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseArray(rs.getArray(columnName));
    }

    @Override
    public C getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseArray(rs.getArray(columnIndex));
    }

    @Override
    public C getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseArray(cs.getArray(columnIndex));
    }

    private C parseArray(Array array) throws SQLException {
        C collection = createEmptyCollection();
        if (array == null) return collection;

        Object obj = array.getArray();
        if (obj == null) return collection;

        String[] strings;
        if (obj instanceof String[]) {
            strings = (String[]) obj;
        } else if (obj instanceof Object[]) {
            strings = Arrays.stream((Object[]) obj)
                    .map(Objects::toString)
                    .toArray(String[]::new);
        } else {
            return collection;
        }

        for (String s : strings) {
            if (s != null) {
                T element = stringToElement(s);
                if (element != null) {
                    collection.add(element);
                }
            }
        }
        return collection;
    }
}