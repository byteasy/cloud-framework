package com.byteasy.util;

import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.dto.Column;
import com.byteasy.entity.LogicDeleteEntity;
import com.byteasy.enums.ColumnType;
import com.byteasy.enums.Delete;
import com.byteasy.enums.YesNo;
import com.byteasy.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static com.byteasy.util.ColumnUtil.convertToDbValue;

public class SqlUtil {
    private static Set<String> auditEntityPropSet = ColumnUtil.getFieldNames(LogicDeleteEntity.class);

    public static String getSingleStringValue(Map<String, ?> multiMap, String key, String defaultValue) {
        Object value = multiMap.get(key);
        if (value == null) return defaultValue;
        return value.getClass().isArray() ? ((String[]) value)[0] : value.toString();
    }

    public static Pair<String, List<Object>> buildWhereSql(Map<String, Column> columnMap, Map<String, ?> multiMap) {
        String matchLogic = getSingleStringValue(multiMap, "matchLogic", null);
        if (StringUtils.isNotEmpty(matchLogic)) {
            return buildWhereSqlAdvance(columnMap, multiMap);
        }
        String oneKeySearch = getSingleStringValue(multiMap, "oneKeySearch", null);
        if (StringUtils.isNotEmpty(oneKeySearch)) {
            return buildWhereSqlOneWord(columnMap, multiMap);
        }
        return buildWhereSqlAdvance(columnMap, multiMap);
    }

    public static Pair<String, List<Object>> buildWhereSqlAdvance(Map<String, Column> columnMap, Map<String, ?> multiMap) {
        String matchLogic = getSingleStringValue(multiMap, "matchLogic", "AND");
        StringJoiner whereStr = new StringJoiner(" " + matchLogic + " ");

        // 保存查询参数，后面要传给 jdbcTemplate
        List<Object> params = new ArrayList<>();

        for (String name : multiMap.keySet()) {
            String fieldName = name;
            Object mapValue = multiMap.get(name);

            // 处理数组参数 name[]
            if (fieldName.contains("[]")) {
                fieldName = fieldName.replace("[]", "");
            }

            // 不在字段定义里，跳过
            if (!columnMap.containsKey(fieldName)) {
                continue;
            }

            String[] valueArr = null;
            if (mapValue.getClass().isArray()) {
                valueArr = (String[]) mapValue;
            } else if (mapValue instanceof Collection<?>) {
                Collection<?> collection = (Collection<?>) mapValue;
                valueArr = collection.stream().map(String::valueOf).toArray(String[]::new);
            } else if (mapValue instanceof String) {
                valueArr = new String[]{mapValue.toString()};
            } else {
                valueArr = new String[]{JacksonUtil.toJson(mapValue)};
            }
            if (valueArr == null || valueArr.length == 0) {
                continue;
            }

            Column column = columnMap.get(fieldName);
            String dbFieldName = column.getFieldName();
            if (StringUtils.isBlank(dbFieldName)) {
                continue;
            }

            // 查询类型
            String queryType = (String) (MapUtil.getSingleValue(multiMap, fieldName + "QueryType"));

            // 日期自动 between
            if (queryType == null && valueArr.length > 1) {
                if (column.getType() == ColumnType.LOCAL_DATE
                        || column.getType() == ColumnType.LOCAL_DATE_TIME) {
                    queryType = "between";
                }
            }

            // 字符串默认 like
            if (queryType == null && ColumnType.STRING.equals(column.getType())) {
                queryType = "like";
            }

            // 多值默认 in
            if (queryType == null && valueArr.length > 1) {
                queryType = "in";
            }

            // 默认 eq
            if (queryType == null) {
                queryType = "eq";
            }

            for (String v : valueArr) {
                if (SqlInjectionUtils.check(v)) {
                    throw new BizException("Discovering SQL injection column: " + column.getName() + ", value: " + v);
                }
            }
            // ==============================================
            // 拼接 SQL 条件
            // ==============================================
            String finalSql = null;
            List<Object> finalParams = new ArrayList<>();

            switch (queryType.toLowerCase()) {
                case "eq":
                    if (StringUtils.isNotBlank(valueArr[0])) {
                        finalSql = "\"" + dbFieldName + "\" = ?";
                        finalParams.add(convertToDbValue(valueArr[0], column));
                    }
                    break;

                case "ne":
                    if (StringUtils.isNotBlank(valueArr[0])) {
                        finalSql = "\"" + dbFieldName + "\" != ?";
                        finalParams.add(convertToDbValue(valueArr[0], column));
                    }
                    break;

                case "gt":
                    if (StringUtils.isNotBlank(valueArr[0])) {
                        finalSql = "\"" + dbFieldName + "\" > ?";
                        finalParams.add(convertToDbValue(valueArr[0], column));
                    }
                    break;

                case "ge":
                    if (StringUtils.isNotBlank(valueArr[0])) {
                        finalSql = "\"" + dbFieldName + "\" >= ?";
                        finalParams.add(convertToDbValue(valueArr[0], column));
                    }
                    break;

                case "lt":
                    if (StringUtils.isNotBlank(valueArr[0])) {
                        finalSql = "\"" + dbFieldName + "\" < ?";
                        finalParams.add(convertToDbValue(valueArr[0], column));
                    }
                    break;

                case "le":
                    if (StringUtils.isNotBlank(valueArr[0])) {
                        finalSql = "\"" + dbFieldName + "\" <= ?";
                        finalParams.add(convertToDbValue(valueArr[0], column));
                    }
                    break;

                case "like":
                    if (StringUtils.isNotBlank(valueArr[0])) {
                        finalSql = "\"" + dbFieldName + "\" LIKE ?";
                        finalParams.add("%" + valueArr[0] + "%");
                    }
                    break;

                case "likeright":
                    if (StringUtils.isNotBlank(valueArr[0])) {
                        finalSql = "\"" + dbFieldName + "\" LIKE ?";
                        finalParams.add("%" + valueArr[0]);
                    }
                    break;

                case "likeleft":
                    if (StringUtils.isNotBlank(valueArr[0])) {
                        finalSql = "\"" + dbFieldName + "\" LIKE ?";
                        finalParams.add(valueArr[0] + "%");
                    }
                    break;

                case "in":
                    List<?> inList = Arrays.stream(valueArr)
                            .map(v -> convertToDbValue(v, column))
                            .collect(Collectors.toList());
                    String placeholders = String.join(",", Collections.nCopies(inList.size(), "?"));
                    finalSql = "\"" + dbFieldName + "\" IN (" + placeholders + ")";
                    finalParams.addAll(inList);
                    break;

                case "between":
                    if (valueArr.length >= 2
                            && StringUtils.isNotBlank(valueArr[0])
                            && StringUtils.isNotBlank(valueArr[1])) {
                        finalSql = "\"" + dbFieldName + "\" BETWEEN ? AND ?";
                        finalParams.add(convertToDbValue(valueArr[0], column));
                        finalParams.add(convertToDbValue(valueArr[1], column));
                    }
                    break;

                case "isnull":
                    finalSql = "\"" + dbFieldName + "\" IS NULL";
                    break;

                case "isnotnull":
                    finalSql = "\"" + dbFieldName + "\" IS NOT NULL";
                    break;
            }

            if (finalSql != null) {
                whereStr.add(finalSql);
                params.addAll(finalParams);
            }
        }

        if (isLogicDeleteEntity(columnMap)) {
            whereStr.add("\"IS_DELETED\"=?");
            params.add(convertToDbValue(Delete.NORMAL, columnMap.get("isDeleted")));
        }

        String where = whereStr.length() > 0 ? " WHERE " + whereStr : "";
        return Pair.create(where, params);
    }

    public static Pair<String, List<Object>> buildWhereSqlOneWord(Map<String, Column> columnMap, Map<String, ?> multiMap) {
        StringJoiner whereStr = new StringJoiner(" OR ");
        String oneKeySearch = getSingleStringValue(multiMap, "oneKeySearch", null);

        // 保存查询参数，后面要传给 jdbcTemplate
        List<Object> params = new ArrayList<>();

        for (Column column : columnMap.values()) {
            if (!YesNo.YES.equals(column.getIsSearch())) {
                continue;
            }
            String dbFieldName = column.getFieldName();
            if (StringUtils.isBlank(dbFieldName)) {
                continue;
            }
            String queryType = "like";

            switch (column.getType()) {
                case ENUM, BOOL, INT, SHORT, BYTE, LONG:
                    queryType = "eq";
                    break;
                case FLOAT, DOUBLE:
                    // TODO
                    break;
            }

            String finalSql = null;
            List<Object> finalParams = new ArrayList<>();

            switch (queryType.toLowerCase()) {
                case "eq":
                    if (StringUtils.isNotBlank(oneKeySearch)) {
                        finalSql = "\"" + dbFieldName + "\" = ?";
                        finalParams.add(convertToDbValue(oneKeySearch, column));
                    }
                    break;

                case "like":
                    if (StringUtils.isNotBlank(oneKeySearch)) {
                        finalSql = "\"" + dbFieldName + "\" LIKE ?";
                        finalParams.add("%" + oneKeySearch + "%");
                    }
                    break;
            }

            if (finalSql != null) {
                whereStr.add(finalSql);
                params.addAll(finalParams);
            }
        }

        if (isLogicDeleteEntity(columnMap)) {
            whereStr.add("\"IS_DELETED\"=?");
            params.add(convertToDbValue(Delete.NORMAL, columnMap.get("isDeleted")));
        }

        String where = whereStr.length() > 0 ? " WHERE " + whereStr : "";
        return Pair.create(where, params);
    }

    public static boolean isLogicDeleteEntity(Map<String, Column> columnMap) {
        for (String s : auditEntityPropSet) {
            if (!columnMap.containsKey(s)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAuditEntityProps(String name) {
        return auditEntityPropSet.contains(name);
    }
}
