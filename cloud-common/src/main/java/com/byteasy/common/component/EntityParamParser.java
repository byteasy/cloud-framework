package com.byteasy.common.component;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

public class EntityParamParser {

    @Data
    public static class ParseResult {
        private Class<?> entityClass;
        private TableInfo tableInfo;
        private List<Serializable> ids = new ArrayList<>();
        private List<Object> entities = new ArrayList<>();
        private Wrapper<?> wrapper;
        private boolean isWrapperOperation = false;
    }

    public static ParseResult parse(Object parameter, Class<?> entityClassFromMapper) {
        ParseResult result = new ParseResult();
        if (parameter == null) {
            return result;
        }

        // 1. 单实体插入 / 修改 (parameter 本身就是实体对象)
        TableInfo directTableInfo = TableInfoHelper.getTableInfo(parameter.getClass());
        if (directTableInfo != null) {
            result.setEntityClass(parameter.getClass());
            result.setTableInfo(directTableInfo);
            result.getEntities().add(parameter);
            extractIdFromEntity(parameter, directTableInfo, result.getIds());
            return result;
        }

        // 2. 单 ID 删除 deleteById (parameter 是主键包装类或 String)
        if (parameter instanceof Serializable && !(parameter instanceof Map) && !(parameter instanceof Collection)) {
            result.setEntityClass(entityClassFromMapper);
            result.setTableInfo(TableInfoHelper.getTableInfo(entityClassFromMapper));
            result.getIds().add((Serializable) parameter);
            return result;
        }

        // 3. 原生集合删除 deleteBatchIds (入参直接为 List/Set)
        if (parameter instanceof Collection) {
            result.setEntityClass(entityClassFromMapper);
            result.setTableInfo(TableInfoHelper.getTableInfo(entityClassFromMapper));
            for (Object item : (Collection<?>) parameter) {
                if (item instanceof Serializable) {
                    result.getIds().add((Serializable) item);
                }
            }
            return result;
        }

        // 4. ParamMap 复合参数处理 (MyBatis / MyBatis-Plus 多参数封装)
        if (parameter instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) parameter;

            // 4.1 deleteBatchIds 常见 Map 封装: key 为 "coll" 或 "collection" 或 "list"
            if (map.containsKey("coll") || map.containsKey("collection")) {
                Object coll = map.containsKey("coll") ? map.get("coll") : map.get("collection");
                if (coll instanceof Collection) {
                    result.setEntityClass(entityClassFromMapper);
                    result.setTableInfo(TableInfoHelper.getTableInfo(entityClassFromMapper));
                    for (Object item : (Collection<?>) coll) {
                        if (item instanceof Serializable) {
                            result.getIds().add((Serializable) item);
                        }
                    }
                    return result;
                }
            }

            // 4.2 批量插入 insertBatchSomeColumn: key 为 "list"
            if (map.containsKey("list") && map.get("list") instanceof Collection) {
                Collection<?> list = (Collection<?>) map.get("list");
                for (Object item : list) {
                    if (result.getTableInfo() == null) {
                        result.setTableInfo(TableInfoHelper.getTableInfo(item.getClass()));
                        result.setEntityClass(item.getClass());
                    }
                    result.getEntities().add(item);
                    if (result.getTableInfo() != null) {
                        extractIdFromEntity(item, result.getTableInfo(), result.getIds());
                    }
                }
                return result;
            }

            // 4.3 条件更新/删除: update(entity, wrapper) 或 remove(wrapper)
            // MP 默认规则: "ew" -> Wrapper, "et" -> Entity
            if (map.containsKey("ew")) {
                result.setWrapper((Wrapper<?>) map.get("ew"));
                result.setWrapperOperation(true);
                result.setEntityClass(entityClassFromMapper);
                result.setTableInfo(TableInfoHelper.getTableInfo(entityClassFromMapper));

                if (map.containsKey("et")) {
                    Object entity = map.get("et");
                    if (entity != null) {
                        result.getEntities().add(entity);
                    }
                }
                return result;
            }
        }

        return result;
    }

    private static void extractIdFromEntity(Object entity, TableInfo tableInfo, List<Serializable> idList) {
        if (tableInfo != null && tableInfo.getKeyProperty() != null) {
            Object idVal = tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty());
            if (idVal instanceof Serializable) {
                idList.add((Serializable) idVal);
            }
        }
    }
}
