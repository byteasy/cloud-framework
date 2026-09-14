package com.byteasy.common.util;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class RequestUtil {

    private static Map<Class<?>, Map<String, FieldType>> cache = Maps.newConcurrentMap();

    private static Map<String, FieldType> getByEntityClass(Class<?> cls) {
        if (!cache.containsKey(cls)) {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            Map<String, FieldType> propertyColumnMap = CastUtil.listToMap(tableInfo.getFieldList(), TableFieldInfo::getProperty, e -> new FieldType(e.getColumn(), e.getField()));
            // 主键也放进去
            propertyColumnMap.put(tableInfo.getKeyProperty(), new FieldType(tableInfo.getKeyColumn(), tableInfo.getKeyType()));
            cache.put(cls, propertyColumnMap);
        }
        return cache.get(cls);
    }

    public static <T> Page<T> getPage() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String tmp = request.getParameter("current");

        Long current = null;
        Long size = null;
        if (StringUtils.isNotEmpty(tmp)) {
            current = Long.parseLong(tmp);
        }
        tmp = request.getParameter("size");
        if (StringUtils.isNotEmpty(tmp)) {
            size = Long.parseLong(tmp);
        }
        return new Page<>(current == null ? 1 : current, size == null ? 20 : size);
    }

    public static <T> T getParameter(String key, Class<T> cls) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String tmp = request.getParameter(key);
        if (StringUtils.isNotEmpty(tmp)) {
            return (T) FieldValueUtil.valueOf(tmp, cls);
        }
        return null;
    }

    public static <T extends Enum<T>> T getEnum(String key, Class<T> enumCls) {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null) {
                return null;
            }

            HttpServletRequest request = attrs.getRequest();
            String tmp = request.getParameter(key);

            if (StringUtils.isNotEmpty(tmp)) {
                return (T) FieldValueUtil.valueOf(tmp, enumCls);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static <T> QueryWrapper<T> getQueryWrapper(Class<T> cls, T entity) {
        Map<String, FieldType> propertyColumnMap = getByEntityClass(cls);
        Set<Map.Entry<String, Object>> entries = JSONUtil.parseObj(entity).entrySet();
        Map<String, String[]> multiMap = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : entries) {
            multiMap.put(entry.getKey(), new String[]{entry.getValue().toString()});
        }
        return buildWrapper(multiMap, propertyColumnMap);
    }

    public static <T> QueryWrapper<T> getQueryWrapper(Class<T> cls) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Map<String, String[]> multiMap = request.getParameterMap();
        Map<String, FieldType> propertyColumnMap = getByEntityClass(cls);
        return buildWrapper(multiMap, propertyColumnMap);
    }

    private static <T> QueryWrapper<T> buildWrapper(Map<String, String[]> multiMap, Map<String, FieldType> propertyColumnMap) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        Set<String> names = multiMap.keySet();
        for (String name : names) {
            String defaultQueryType = null;
            String fieldName = name;
            if (fieldName.contains("[]")) {
                fieldName = name.replace("[]", "");
                defaultQueryType = "in";
            }
            if (!propertyColumnMap.containsKey(fieldName)) {
                continue;
            }
            String[] value = multiMap.get(name);
            if (value == null || value.length < 1) {
                continue;
            }
            FieldType fieldType = propertyColumnMap.get(fieldName);
            String columnName = fieldType.getColumnName();
            String queryType = multiMap.containsKey(fieldName + "QueryType") ? multiMap.get(fieldName + "QueryType")[0] : defaultQueryType;

            if (value.length > 1 && queryType == null) {
                if (fieldType.getType().equals(LocalDate.class) || fieldType.getType().equals(LocalDateTime.class) || fieldType.getType().equals(Date.class)) {
                    queryType = "between";
                }
            }
            if (queryType == null && value.length > 1) {
                queryType = "in";
            }
            if (queryType == null && fieldType.getType().equals(String.class)) {
                queryType = "like";
            }
            if (queryType == null || queryType.equalsIgnoreCase("eq")) {
                // default is eq, 默认是等于搜索
                if (StringUtils.isNotEmpty(value[0])) {
                    wrapper.eq(columnName, FieldValueUtil.valueOf(value[0], fieldType.getType()));
                }
            } else if (queryType.equalsIgnoreCase("ne")) { // 不等于
                if (StringUtils.isNotEmpty(value[0])) {
                    wrapper.ne(columnName, FieldValueUtil.valueOf(value[0], fieldType.getType()));
                }
            } else if (queryType.equalsIgnoreCase("ge")) { // 大于等于
                if (StringUtils.isNotEmpty(value[0])) {
                    wrapper.ge(columnName, FieldValueUtil.valueOf(value[0], fieldType.getType()));
                }
            } else if (queryType.equalsIgnoreCase("gt")) { // 大于
                if (StringUtils.isNotEmpty(value[0])) {
                    wrapper.gt(columnName, FieldValueUtil.valueOf(value[0], fieldType.getType()));
                }
            } else if (queryType.equalsIgnoreCase("le")) { // 小于等于
                if (StringUtils.isNotEmpty(value[0])) {
                    wrapper.le(columnName, FieldValueUtil.valueOf(value[0], fieldType.getType()));
                }
            } else if (queryType.equalsIgnoreCase("lt")) { // 小于
                if (StringUtils.isNotEmpty(value[0])) {
                    wrapper.lt(columnName, FieldValueUtil.valueOf(value[0], fieldType.getType()));
                }
            } else if (queryType.equalsIgnoreCase("in")) { // in
                wrapper.in(columnName, FieldValueUtil.toArray(value, fieldType.getType()));
            } else if (queryType.equalsIgnoreCase("notIn")) { // not in
                wrapper.notIn(columnName, FieldValueUtil.toArray(value, fieldType.getType()));
            } else if (queryType.equalsIgnoreCase("notNull")) { // 非控
                wrapper.isNotNull(columnName);
            } else if (queryType.equalsIgnoreCase("null")) { // 空
                wrapper.isNull(columnName);
            } else if (queryType.equalsIgnoreCase("like")) { // like查找
                if (value != null && value.length > 1) {
                    for (String val : value) {
                        wrapper.and(s -> s.like(columnName, FieldValueUtil.valueOf(val, fieldType.getType())));
                    }
                } else {
                    if (StringUtils.isNotEmpty(value[0])) {
                        wrapper.like(columnName, FieldValueUtil.valueOf(value[0], fieldType.getType()));
                    }
                }
            } else if (queryType.equalsIgnoreCase("notLike")) { // like反向查找
                if (StringUtils.isNotEmpty(value[0])) {
                    wrapper.notLike(columnName, FieldValueUtil.valueOf(value[0], fieldType.getType()));
                }
            } else if (queryType.equalsIgnoreCase("likeRight")) { // 最左匹配，索引友好
                if (StringUtils.isNotEmpty(value[0])) {
                    wrapper.likeRight(columnName, FieldValueUtil.valueOf(value[0], fieldType.getType()));
                }
            } else if (queryType.equalsIgnoreCase("notLikeRight")) { // 反向最左匹配
                if (StringUtils.isNotEmpty(value[0])) {
                    wrapper.notLikeRight(columnName, FieldValueUtil.valueOf(value[0], fieldType.getType()));
                }
            } else if (queryType.equalsIgnoreCase("likeLeft")) { // 左侧模糊查询
                if (StringUtils.isNotEmpty(value[0])) {
                    wrapper.likeLeft(columnName, FieldValueUtil.valueOf(value[0], fieldType.getType()));
                }
            } else if (queryType.equalsIgnoreCase("notLikeLeft")) { // 反向左侧模糊查询
                if (StringUtils.isNotEmpty(value[0])) {
                    wrapper.notLikeLeft(columnName, FieldValueUtil.valueOf(value[0], fieldType.getType()));
                }
            } else if (queryType.equalsIgnoreCase("between")) { // 区间查询，闭区间
                if (StringUtils.isNotEmpty(value[0]) && StringUtils.isNotEmpty(value[1])) {
                    wrapper.between(columnName, FieldValueUtil.valueOf(value[0], fieldType.getType()), FieldValueUtil.valueOf(value[1], fieldType.getType()));
                }
            } else if (queryType.equalsIgnoreCase("notBetween")) { // 反向区间查询
                if (StringUtils.isNotEmpty(value[0]) && StringUtils.isNotEmpty(value[1])) {
                    wrapper.notBetween(columnName, FieldValueUtil.valueOf(value[0], fieldType.getType()), FieldValueUtil.valueOf(value[1], fieldType.getType()));
                }
            }
        }

        // order by
        String[] descColumns = multiMap.get("descColumns");
        if (!(descColumns == null || descColumns.length < 1)) {
            List<String> colNames = Arrays.stream(descColumns).filter(propertyColumnMap::containsKey).map(propertyColumnMap::get).map(FieldType::getColumnName).collect(Collectors.toList());
            wrapper.orderByDesc(colNames);
        }

        String[] ascColumns = multiMap.get("ascColumns");
        if (!(ascColumns == null || ascColumns.length < 1)) {
            List<String> colNames = Arrays.stream(ascColumns).filter(propertyColumnMap::containsKey).map(propertyColumnMap::get).map(FieldType::getColumnName).collect(Collectors.toList());
            wrapper.orderByAsc(colNames);
        }
        wrapper.checkSqlInjection();
        return wrapper;
    }

    public static String getHttpHeader(String name) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader(name);
    }

    @Setter
    @Getter
    private static class FieldType {

        private String columnName;
        private Class<?> type;

        public FieldType(String columnName, Field field) {
            this.columnName = columnName;
            this.type = field.getType();
        }

        public FieldType(String columnName, Class<?> type) {
            this.columnName = columnName;
            this.type = type;
        }

    }
}
