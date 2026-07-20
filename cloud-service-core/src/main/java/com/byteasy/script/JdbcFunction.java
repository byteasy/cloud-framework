package com.byteasy.script;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class JdbcFunction implements JsFunctions {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Collection<JsFunction> jsFunctions() {
        List<JsFunction> list = new ArrayList<>();
        list.add(new JsFunction() {
            @Override
            public String name() {
                return "dbQueryForMap";
            }

            @Override
            public String description() {
                return "Use jdbcTemplate to query a map";
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length == 0) return null;

                Object[] tailerArgs = args.length < 2 ? null : Arrays.copyOfRange(args, 1, args.length);
                return jdbcTemplate.queryForMap(String.valueOf(args[0]), tailerArgs);
            }
        });

        list.add(new JsFunction() {
            @Override
            public String name() {
                return "dbQueryForList";
            }

            @Override
            public String description() {
                return "Use jdbcTemplate to query rows";
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length == 0) return null;

                Object[] tailerArgs = args.length < 2 ? null : Arrays.copyOfRange(args, 1, args.length);
                return jdbcTemplate.queryForList(String.valueOf(args[0]), tailerArgs);
            }
        });

        list.add(new JsFunction() {
            @Override
            public String name() {
                return "dbUpdate";
            }

            @Override
            public String description() {
                return "Use jdbcTemplate to execute a sql";
            }

            @Override
            public Object run(Object[] args) throws Exception {
                if (args == null || args.length == 0) return null;

                Object[] tailerArgs = args.length < 2 ? null : Arrays.copyOfRange(args, 1, args.length);
                return jdbcTemplate.update(String.valueOf(args[0]), tailerArgs);
            }
        });
        return list;
    }
}
