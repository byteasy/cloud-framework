package com.byteasy.script;

import com.byteasy.common.util.JacksonUtil;
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
                return "Use ICommonService to saveOrUpdate a map";
            }

            @Override
            public Object run(Object[] args) throws Exception {
                return commonService.saveOrUpdate((String) args[0], JacksonUtil.nativeRead((String) args[1], Map.class));
            }
        });

        list.add(new JsFunction() {
            @Override
            public String group() {
                return "basic";
            }

            @Override
            public String name() {
                return "saveOrUpdateBatch";
            }

            @Override
            public String description() {
                return "Use ICommonService to saveOrUpdateBatch rows";
            }

            @Override
            public Object run(Object[] args) throws Exception {
                return commonService.saveOrUpdateBatch((String) args[0], JacksonUtil.nativeRead((String) args[1], List.class));
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
                return "根据ID查询某行";
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
                return "分页查询，查询条件前端制定";
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
                return "单表批量查询,查询条件前端指定";
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
                return "单表批量条件查询";
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
                return "根据ID删除某一行";
            }

            @Override
            public Object run(Object[] args) throws Exception {
                return commonService.removeById((String) args[0], args[1]);
            }
        });
        return list;
    }
}
