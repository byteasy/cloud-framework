package com.byteasy.script;

import com.byteasy.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class CommonServiceFunction implements JsFunctions {
    @Autowired
    private ICommonService commonService;

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
                return "commonServiceSaveOrUpdate";
            }

            @Override
            public String description() {
                return """
                        Use ICommonService to saveOrUpdate a map.
                        函数原型:
                                Map<String, Object> saveOrUpdate(String className, Map<String, Object> entity);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                return commonService.saveOrUpdate((String) args[0], (Map) args[1]);
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "commonServiceSaveOrUpdateBatch";
            }

            @Override
            public String description() {
                return """
                        Use ICommonService to commonServiceSaveOrUpdateBatch a list.
                        函数原型:
                                boolean saveOrUpdateBatch(String className, List<Map<String, Object>> entities);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                return commonService.saveOrUpdateBatch((String) args[0], (List) args[1]);
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "commonServiceGetById";
            }

            @Override
            public String description() {
                return """
                        Use ICommonService to getById a entity.
                        函数原型:
                                Map<String, Object> getById(String className, Object id);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                return commonService.getById((String) args[0], args[1]);
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "commonServicePage";
            }

            @Override
            public String description() {
                return """
                        Use ICommonService to get one page entities.
                        函数原型:
                                Page<Map<String, Object>> page(String className);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                return commonService.page((String) args[0]);
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "commonServiceList";
            }

            @Override
            public String description() {
                return """
                        Use ICommonService to list all entities math the query params.
                        函数原型:
                                List<Map<String, Object>> list(String className);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                return commonService.list((String) args[0]);
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "commonServiceListBy";
            }

            @Override
            public String description() {
                return """
                        Use ICommonService to list all entities math the given params.
                        函数原型:
                                List<Map<String, Object>> list(String className, Map<String, Object> args);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                return commonService.list((String) args[0], (Map<String, Object>) args[1]);
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "commonServiceRemoveById";
            }

            @Override
            public String description() {
                return """
                        Use ICommonService to remove an record.
                        函数原型:
                                boolean removeById(String className, Object id);
                        """;
            }

            @Override
            public Object run(Object[] args) throws Exception {
                return commonService.removeById((String) args[0], args[1]);
            }
        });
        return list;
    }
}
