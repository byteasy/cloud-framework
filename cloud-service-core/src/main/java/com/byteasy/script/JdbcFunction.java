package com.byteasy.script;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class JdbcFunction implements JsFunctions {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Collection<JsFunction> jsFunctions() {
        List<JsFunction> list = new ArrayList<>();
        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "dbQueryForMap";
            }

            @Override
            public String description() {
                return """
                        Use jdbcTemplate to query a object.
                        函数原型:
                                Map<String, Object> queryForMap(String sql, @Nullable Object... args) throws DataAccessException;
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length == 0) return null;

                NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
                MapSqlParameterSource paramSource = fromMap(args[1]);
                return namedJdbcTemplate.queryForMap(String.valueOf(args[0]), paramSource);
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "dbQueryForList";
            }

            @Override
            public String description() {
                return """
                        Use jdbcTemplate to query a object list.
                        函数原型:
                                List<Map<String, Object>> queryForList(String sql, @Nullable Object... args) throws DataAccessException;
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length == 0) return null;

                NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
                MapSqlParameterSource paramSource = fromMap(args[1]);
                return namedJdbcTemplate.queryForList(String.valueOf(args[0]), paramSource);
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "dbUpdate";
            }

            @Override
            public String description() {
                return """
                        Use jdbcTemplate to execute a sql.
                        函数原型:
                                int update(String sql, @Nullable Object... args) throws DataAccessException;
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length == 0) return null;

                NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
                MapSqlParameterSource paramSource = fromMap(args[1]);
                return namedJdbcTemplate.update(String.valueOf(args[0]), paramSource);
            }
        });
        return list;
    }

    protected MapSqlParameterSource fromMap(Object args) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        if (args instanceof Map<?, ?>) {
            Map<String, Object> map = (Map<String, Object>) args;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                paramSource.addValue(entry.getKey(), entry.getValue());
            }
        }
        return paramSource;
    }
}
