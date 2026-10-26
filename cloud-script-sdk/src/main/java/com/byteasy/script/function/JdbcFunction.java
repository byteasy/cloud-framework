package com.byteasy.script.function;

import com.byteasy.util.CastUtil;
import com.byteasy.script.JsFunction;
import com.byteasy.script.JsFunctions;
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
                return "dbQueryForOneColumn";
            }

            @Override
            public String description() {
                return """
                        Use jdbcTemplate to query a object column list.
                        函数原型:
                                List<Object> queryForList(String sql, @Nullable Object... args) throws DataAccessException;
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length == 0) return null;

                NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
                MapSqlParameterSource paramSource = fromMap(args[1]);
                List<Map<String, Object>> list = namedJdbcTemplate.queryForList(String.valueOf(args[0]), paramSource);
                return CastUtil.objectListToColumnList(list, map -> map.get(map.keySet().stream().findAny().get()));
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "dbQueryForLong";
            }

            @Override
            public String description() {
                return """
                        Use jdbcTemplate to query a column to Long.
                        函数原型:
                                Long queryForList(String sql, @Nullable Object... args) throws DataAccessException;
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length == 0) return null;

                NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
                MapSqlParameterSource paramSource = fromMap(args[1]);
                return namedJdbcTemplate.queryForObject(String.valueOf(args[0]), paramSource, Long.class);
            }
        });


        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "dbQueryForString";
            }

            @Override
            public String description() {
                return """
                        Use jdbcTemplate to query a column to String.
                        函数原型:
                                String queryForObject(String sql, @Nullable Object... args) throws DataAccessException;
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length == 0) return null;

                NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
                MapSqlParameterSource paramSource = fromMap(args[1]);
                return namedJdbcTemplate.queryForObject(String.valueOf(args[0]), paramSource, String.class);
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
