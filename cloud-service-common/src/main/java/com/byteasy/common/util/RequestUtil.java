package com.byteasy.common.util;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.common.component.FieldDefineLoadingCache;
import com.byteasy.enums.ColumnType;
import com.byteasy.enums.YesNo;
import com.byteasy.exception.BizException;
import com.byteasy.platform.entity.FieldDefine;
import com.google.common.collect.Maps;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;
import java.util.stream.Collectors;

import static com.byteasy.common.util.FieldDefineUtil.convertToDbValue;

public class RequestUtil {

    /**
     * 通过 Spring 上下文获取 FieldDefineLoadingCache，拉取当前类全名的元数据定义
     * Map Key: 实体属性名 propertyName (如 userName, userId)
     */
    private static Map<String, FieldDefine> getFieldDefineMap(Class<?> cls) {
        FieldDefineLoadingCache cacheLoader = SpringUtil.getBean(FieldDefineLoadingCache.class);
        if (cacheLoader == null) {
            return Collections.emptyMap();
        }
        List<FieldDefine> fieldDefines = cacheLoader.load(cls.getName());
        if (fieldDefines == null || fieldDefines.isEmpty()) {
            return Collections.emptyMap();
        }
        // 按实体属性名 name 建立映射（如 userId, username）
        return fieldDefines.stream().collect(Collectors.toMap(
                FieldDefine::getName,
                f -> f,
                (oldVal, newVal) -> newVal,
                LinkedHashMap::new
        ));
    }

    public static <T> Page<T> getPage() {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return new Page<>(1, 20);
        }
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
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) return null;
        String tmp = request.getParameter(key);
        if (StringUtils.isNotEmpty(tmp)) {
            return (T) FieldValueUtil.valueOf(tmp, cls);
        }
        return null;
    }

    public static <T extends Enum<T>> T getEnum(String key, Class<T> enumCls) {
        try {
            HttpServletRequest request = getHttpServletRequest();
            if (request == null) return null;
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
        Map<String, FieldDefine> fieldDefineMap = getFieldDefineMap(cls);
        Set<Map.Entry<String, Object>> entries = JSONUtil.parseObj(entity).entrySet();
        Map<String, String[]> multiMap = Maps.newHashMap();
        for (Map.Entry<String, Object> entry : entries) {
            if (entry.getValue() != null) {
                multiMap.put(entry.getKey(), new String[]{entry.getValue().toString()});
            }
        }
        return buildWrapper(multiMap, fieldDefineMap);
    }

    public static <T> QueryWrapper<T> getQueryWrapper(Class<T> cls) {
        HttpServletRequest request = getHttpServletRequest();
        Map<String, String[]> multiMap = request != null ? request.getParameterMap() : Collections.emptyMap();
        Map<String, FieldDefine> fieldDefineMap = getFieldDefineMap(cls);
        return buildWrapper(multiMap, fieldDefineMap);
    }

    private static String getSingleValue(Map<String, String[]> multiMap, String key, String defaultValue) {
        if (multiMap == null) return defaultValue;
        String[] arr = multiMap.get(key);
        if (arr == null || arr.length == 0) {
            arr = multiMap.get("_" + key);
        }
        if (arr != null && arr.length > 0 && StringUtils.isNotBlank(arr[0])) {
            return arr[0].trim();
        }
        return defaultValue;
    }

    /**
     * 核心逻辑路由：
     * 1. 存在具体高级字段检索参数 -> 走高级条件构建
     * 2. 不存在高级条件，且存在 oneKeySearch -> 走一键检索
     * 3. 都没有 -> 干净分页查询（不产生多余 WHERE）
     */
    private static <T> QueryWrapper<T> buildWrapper(Map<String, String[]> multiMap, Map<String, FieldDefine> fieldDefineMap) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        if (multiMap == null || multiMap.isEmpty() || fieldDefineMap.isEmpty()) {
            return wrapper;
        }

        // 1. 判断是否存在具体字段的高级检索参数
        boolean hasAdvanceQuery = multiMap.keySet().stream().anyMatch(key -> {
            String cleanKey = key.replace("[]", "");
            if (!fieldDefineMap.containsKey(cleanKey) || key.endsWith("QueryType") || key.endsWith("Readonly")) {
                return false;
            }
            String[] val = multiMap.get(key);
            String queryType = getSingleValue(multiMap, cleanKey + "QueryType", null);
            boolean isNullQuery = "null".equalsIgnoreCase(queryType) || "notNull".equalsIgnoreCase(queryType)
                    || "isnull".equalsIgnoreCase(queryType) || "isnotnull".equalsIgnoreCase(queryType);
            return isNullQuery || (val != null && val.length > 0 && StringUtils.isNotBlank(val[0]));
        });

        // 2. 二选一逻辑路由
        if (hasAdvanceQuery) {
            buildAdvanceWrapper(wrapper, multiMap, fieldDefineMap);
        } else {
            String oneKeySearch = getSingleValue(multiMap, "oneKeySearch", null);
            if (StringUtils.isNotBlank(oneKeySearch)) {
                buildOneKeySearchWrapper(wrapper, oneKeySearch, fieldDefineMap);
            }
        }

        // 3. 处理排序 Order By
        handleOrderBy(wrapper, multiMap, fieldDefineMap);

        wrapper.checkSqlInjection();
        return wrapper;
    }

    /**
     * 单框一键全文搜索（由 FieldDefine.isSearch 动态驱动）
     */
    private static <T> void buildOneKeySearchWrapper(QueryWrapper<T> wrapper, String oneKeySearch, Map<String, FieldDefine> fieldDefineMap) {
        if (StringUtils.isBlank(oneKeySearch)) {
            return;
        }
        if (SqlInjectionUtils.check(oneKeySearch)) {
            throw new BizException("Discovering SQL injection in oneKeySearch: " + oneKeySearch);
        }

        // 🔥 从元数据定义中过滤出所有允许检索的字段 (isSearch == YES)
        List<FieldDefine> searchFields = fieldDefineMap.values().stream()
                .filter(col -> YesNo.YES.equals(col.getIsSearch()) && StringUtils.isNotBlank(col.getFieldName()))
                .collect(Collectors.toList());

        if (searchFields.isEmpty()) {
            return;
        }

        // 外层包裹在 and 表达式中，内部所有字段通过 OR 拼接
        wrapper.and(sub -> {
            boolean isFirst = true;

            for (FieldDefine col : searchFields) {
                String dbFieldName = col.getFieldName();
                ColumnType columnType = col.getType();

                // 1. 字符串类型：普通文本使用 LIKE；如果是 UUID 字段做等值安全比较
                if (ColumnType.STRING.equals(columnType)) {
                    if (isUuidColumn(col)) {
                        if (isValidUuid(oneKeySearch)) {
                            if (!isFirst) sub.or();
                            sub.eq(dbFieldName, oneKeySearch);
                            isFirst = false;
                        }
                    } else {
                        if (!isFirst) sub.or();
                        sub.like(dbFieldName, oneKeySearch);
                        isFirst = false;
                    }
                }
                // 2. 数字类型：纯数字输入时才参与等值匹配
                else if (isNumericType(columnType) && StringUtils.isNumeric(oneKeySearch)) {
                    try {
                        Object numVal = convertToDbValue(oneKeySearch, col);
                        if (numVal != null) {
                            if (!isFirst) sub.or();
                            sub.eq(dbFieldName, numVal);
                            isFirst = false;
                        }
                    } catch (Exception ignored) {
                    }
                }
                // 3. 枚举 / 布尔类型
                else if (ColumnType.ENUM.equals(columnType) || ColumnType.BOOL.equals(columnType)) {
                    try {
                        Object enumVal = convertToDbValue(oneKeySearch, col);
                        if (enumVal != null) {
                            if (!isFirst) sub.or();
                            sub.eq(dbFieldName, enumVal);
                            isFirst = false;
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        });
    }

    /**
     * 高级条件检索（完全由 FieldDefine 元数据驱动）
     */
    private static <T> void buildAdvanceWrapper(QueryWrapper<T> wrapper, Map<String, String[]> multiMap, Map<String, FieldDefine> fieldDefineMap) {
        String matchLogic = getSingleValue(multiMap, "matchLogic", "AND").trim().toUpperCase();
        for (String name : multiMap.keySet()) {
            String defaultQueryType = null;
            String fieldName = name;
            if (fieldName.contains("[]")) {
                fieldName = name.replace("[]", "");
                defaultQueryType = "in";
            }

            if (fieldName.endsWith("QueryType")) {
                String queryType = getSingleValue(multiMap, fieldName, null);
                if (("isnull".equalsIgnoreCase(queryType) || "isnotnull".equalsIgnoreCase(queryType))) {
                    fieldName = fieldName.replace("QueryType", "");
                }
            }
            if (!fieldDefineMap.containsKey(fieldName) || fieldName.endsWith("QueryType") || fieldName.endsWith("Readonly")) {
                continue;
            }

            String[] value = multiMap.get(name);
            String queryType = getSingleValue(multiMap, fieldName + "QueryType", null);
            boolean isNullQuery = "null".equalsIgnoreCase(queryType) || "notNull".equalsIgnoreCase(queryType)
                    || "isnull".equalsIgnoreCase(queryType) || "isnotnull".equalsIgnoreCase(queryType);
            if (!isNullQuery && (value == null || value.length == 0 || StringUtils.isBlank(value[0]))) {
                continue;
            }

            // 防 SQL 注入检查
            if (value != null) {
                for (String val : value) {
                    if (StringUtils.isNotBlank(val) && SqlInjectionUtils.check(val)) {
                        throw new BizException("Discovering SQL injection in field: " + fieldName + ", value: " + val);
                    }
                }
            }

            FieldDefine column = fieldDefineMap.get(fieldName);
            String dbFieldName = column.getFieldName();

            // 智能缺省 queryType
            if (queryType == null) {
                if (value != null && value.length > 1) {
                    if (column.getType() == ColumnType.LOCAL_DATE || column.getType() == ColumnType.LOCAL_DATE_TIME) {
                        queryType = "between";
                    } else {
                        queryType = "in";
                    }
                } else if (ColumnType.STRING.equals(column.getType())) {
                    queryType = "like";
                } else {
                    queryType = "eq";
                }
            }

            // 拆分逗号分隔参数
            if (("between".equalsIgnoreCase(queryType) || "in".equalsIgnoreCase(queryType)) && value != null && value.length == 1 && value[0].contains(",")) {
                value = value[0].split(",");
            }

            applyFieldCondition(wrapper, dbFieldName, queryType, value, column, matchLogic);
        }
    }

    private static <T> void applyFieldCondition(QueryWrapper<T> wrapper, String dbFieldName, String queryType, String[] value, FieldDefine column, String matchLogic) {
        String typeLower = queryType.toLowerCase();
        switch (typeLower) {
            case "eq":
                if (StringUtils.isNotEmpty(value[0])) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.eq(dbFieldName, convertToDbValue(value[0], column));
                    } else {
                        wrapper.or(w -> w.eq(dbFieldName, convertToDbValue(value[0], column)));
                    }
                }
                break;
            case "ne":
                if (StringUtils.isNotEmpty(value[0])) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.ne(dbFieldName, convertToDbValue(value[0], column));
                    } else {
                        wrapper.or(w -> w.ne(dbFieldName, convertToDbValue(value[0], column)));
                    }
                }
                break;
            case "ge":
                if (StringUtils.isNotEmpty(value[0])) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.ge(dbFieldName, convertToDbValue(value[0], column));
                    } else {
                        wrapper.or(w -> w.ge(dbFieldName, convertToDbValue(value[0], column)));
                    }
                }
                break;
            case "gt":
                if (StringUtils.isNotEmpty(value[0])) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.gt(dbFieldName, convertToDbValue(value[0], column));
                    } else {
                        wrapper.or(w -> w.gt(dbFieldName, convertToDbValue(value[0], column)));
                    }
                }
                break;
            case "le":
                if (StringUtils.isNotEmpty(value[0])) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.le(dbFieldName, convertToDbValue(value[0], column));
                    } else {
                        wrapper.or(w -> w.le(dbFieldName, convertToDbValue(value[0], column)));
                    }
                }
                break;
            case "lt":
                if (StringUtils.isNotEmpty(value[0])) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.lt(dbFieldName, convertToDbValue(value[0], column));
                    } else {
                        wrapper.or(w -> w.lt(dbFieldName, convertToDbValue(value[0], column)));
                    }
                }
                break;
            case "in":
                List<?> inList = Arrays.stream(value)
                        .filter(StringUtils::isNotBlank)
                        .map(v -> convertToDbValue(v.trim(), column))
                        .collect(Collectors.toList());
                if (!inList.isEmpty()) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.in(dbFieldName, inList);
                    } else {
                        wrapper.or(w -> w.in(dbFieldName, inList));
                    }
                }
                break;
            case "notin":
                List<?> notInList = Arrays.stream(value)
                        .filter(StringUtils::isNotBlank)
                        .map(v -> convertToDbValue(v.trim(), column))
                        .collect(Collectors.toList());
                if (!notInList.isEmpty()) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.notIn(dbFieldName, notInList);
                    } else {
                        wrapper.or(w -> w.notIn(dbFieldName, notInList));
                    }
                }
                break;
            case "notnull":
            case "isnotnull":
                if (matchLogic.equalsIgnoreCase("and")) {
                    wrapper.isNotNull(dbFieldName);
                } else {
                    wrapper.or(w -> w.isNotNull(dbFieldName));
                }
                break;
            case "null":
            case "isnull":
                if (matchLogic.equalsIgnoreCase("and")) {
                    wrapper.isNull(dbFieldName);
                } else {
                    wrapper.or(w -> w.isNull(dbFieldName));
                }
                break;
            case "like":
                // ⭐️ 如果是 UUID 字段降级为 eq，杜绝 PG 语法报错
                if (isUuidColumn(column)) {
                    if (StringUtils.isNotEmpty(value[0])) {
                        if (matchLogic.equalsIgnoreCase("and")) {
                            wrapper.eq(dbFieldName, convertToDbValue(value[0], column));
                        } else {
                            wrapper.or(w -> w.eq(dbFieldName, convertToDbValue(value[0], column)));
                        }
                    }
                } else if (value.length > 1) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.and(sub -> {
                            for (int i = 0; i < value.length; i++) {
                                if (i > 0) sub.or();
                                sub.like(dbFieldName, value[i]);
                            }
                        });
                    } else {
                        wrapper.or(w -> w.and(sub -> {
                            for (int i = 0; i < value.length; i++) {
                                if (i > 0) sub.or();
                                sub.like(dbFieldName, value[i]);
                            }
                        }));
                    }
                } else {
                    if (StringUtils.isNotEmpty(value[0])) {
                        if (matchLogic.equalsIgnoreCase("and")) {
                            wrapper.like(dbFieldName, value[0]);
                        } else {
                            wrapper.or(w -> w.like(dbFieldName, value[0]));
                        }
                    }
                }
                break;
            case "notlike":
                if (StringUtils.isNotEmpty(value[0])) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.notLike(dbFieldName, value[0]);
                    } else {
                        wrapper.or(w -> w.notLike(dbFieldName, value[0]));
                    }
                }
                break;
            case "likeright":
                if (StringUtils.isNotEmpty(value[0])) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.likeLeft(dbFieldName, value[0]);
                    } else {
                        wrapper.or(w -> w.likeLeft(dbFieldName, value[0]));
                    }
                }
                break;
            case "notlikeright":
                if (StringUtils.isNotEmpty(value[0])) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.notLikeLeft(dbFieldName, value[0]);
                    } else {
                        wrapper.or(w -> w.notLikeLeft(dbFieldName, value[0]));
                    }
                }
                break;
            case "likeleft":
                if (StringUtils.isNotEmpty(value[0])) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.likeRight(dbFieldName, value[0]);
                    } else {
                        wrapper.or(w -> w.likeRight(dbFieldName, value[0]));
                    }
                }
                break;
            case "notlikeleft":
                if (StringUtils.isNotEmpty(value[0])) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.notLikeRight(dbFieldName, value[0]);
                    } else {
                        wrapper.or(w -> w.notLikeRight(dbFieldName, value[0]));
                    }
                }
                break;
            case "between":
                if (StringUtils.isNotEmpty(value[0]) && value.length > 1 && StringUtils.isNotEmpty(value[1])) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.between(dbFieldName, convertToDbValue(value[0].trim(), column), convertToDbValue(value[1].trim(), column));
                    } else {
                        wrapper.or(w -> w.between(dbFieldName, convertToDbValue(value[0].trim(), column), convertToDbValue(value[1].trim(), column)));
                    }
                }
                break;
            case "notbetween":
                if (StringUtils.isNotEmpty(value[0]) && value.length > 1 && StringUtils.isNotEmpty(value[1])) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.notBetween(dbFieldName, convertToDbValue(value[0].trim(), column), convertToDbValue(value[1].trim(), column));
                    } else {
                        wrapper.or(w -> w.notBetween(dbFieldName, convertToDbValue(value[0].trim(), column), convertToDbValue(value[1].trim(), column)));
                    }
                }
                break;
            default:
                if (StringUtils.isNotEmpty(value[0])) {
                    if (matchLogic.equalsIgnoreCase("and")) {
                        wrapper.eq(dbFieldName, convertToDbValue(value[0], column));
                    } else {
                        wrapper.or(w -> w.eq(dbFieldName, convertToDbValue(value[0], column)));
                    }
                }
                break;
        }
    }

    private static <T> void handleOrderBy(QueryWrapper<T> wrapper, Map<String, String[]> multiMap, Map<String, FieldDefine> fieldDefineMap) {
        String[] descColumns = multiMap.get("descColumns");
        if (descColumns != null && descColumns.length > 0) {
            List<String> colNames = Arrays.stream(descColumns)
                    .filter(fieldDefineMap::containsKey)
                    .map(fieldDefineMap::get)
                    .map(FieldDefine::getFieldName)
                    .collect(Collectors.toList());
            if (!colNames.isEmpty()) {
                wrapper.orderByDesc(colNames);
            }
        }

        String[] ascColumns = multiMap.get("ascColumns");
        if (ascColumns != null && ascColumns.length > 0) {
            List<String> colNames = Arrays.stream(ascColumns)
                    .filter(fieldDefineMap::containsKey)
                    .map(fieldDefineMap::get)
                    .map(FieldDefine::getFieldName)
                    .collect(Collectors.toList());
            if (!colNames.isEmpty()) {
                wrapper.orderByAsc(colNames);
            }
        }
    }

    private static boolean isNumericType(ColumnType type) {
        return type == ColumnType.INT || type == ColumnType.LONG || type == ColumnType.SHORT
                || type == ColumnType.BYTE || type == ColumnType.FLOAT || type == ColumnType.DOUBLE;
    }

    private static boolean isUuidColumn(FieldDefine col) {
//        return "StringToUuidTypeHandler".equalsIgnoreCase(col.getTypeHandler())
//                || "uuid".equalsIgnoreCase(col.getFieldName())
//                || (col.getName() != null && col.getName().toLowerCase().endsWith("id") && col.getType() == ColumnType.STRING);
        // TODO
        return false;
    }

    private static boolean isValidUuid(String str) {
        if (StringUtils.isBlank(str) || str.length() != 36) return false;
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static String getHttpHeader(String name) {
        HttpServletRequest request = getHttpServletRequest();
        return request != null ? request.getHeader(name) : null;
    }

    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
}