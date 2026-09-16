package com.byteasy.common.util;

import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.byteasy.entity.LogicDeleteEntity;
import com.byteasy.enums.ColumnType;
import com.byteasy.enums.Delete;
import com.byteasy.enums.YesNo;
import com.byteasy.exception.BizException;
import com.byteasy.platform.entity.FieldDefine;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static com.byteasy.common.util.FieldDefineUtil.convertToDbValue;

public class SqlUtil {
    private static final Set<String> auditEntityPropSet = FieldDefineUtil.getFieldNames(LogicDeleteEntity.class);

    public static String getSingleStringValue(Map<String, ?> multiMap, String key, String defaultValue) {
        if (multiMap == null) return defaultValue;
        Object value = multiMap.get(key);
        if (value == null) {
            // 兼容带下划线的前缀参数如 _matchLogic
            value = multiMap.get("_" + key);
        }
        if (value == null) return defaultValue;
        return value.getClass().isArray() ? ((String[]) value)[0] : value.toString();
    }

    /**
     * 统一入口：自动路由单框快速搜索与多条件高级搜索
     */
    public static Pair<String, List<Object>> buildWhereSql(Map<String, FieldDefine> columnMap, Map<String, ?> multiMap) {
        if (multiMap == null || multiMap.isEmpty()) {
            return buildLogicDeleteWhere(columnMap);
        }

        String oneKeySearch = getSingleStringValue(multiMap, "oneKeySearch", null);

        // 判断是否存在具体字段的高级检索参数
        boolean hasSpecificFieldQuery = multiMap.keySet().stream().anyMatch(key -> {
            String cleanKey = key.replace("[]", "");
            return columnMap.containsKey(cleanKey) && !key.endsWith("QueryType") && !key.endsWith("Readonly");
        });

        // 如果存在一键搜索且没有具体字段高级过滤，走一键快速搜索
        if (StringUtils.isNotBlank(oneKeySearch) && !hasSpecificFieldQuery) {
            return buildWhereSqlOneWord(columnMap, multiMap);
        }

        // 默认走高级复合查询
        if (StringUtils.isBlank(oneKeySearch)) {
            return buildWhereSqlAdvance(columnMap, multiMap);
        } else {
            Pair<String, List<Object>> advanceSql = buildWhereSqlAdvance(columnMap, multiMap);
            Pair<String, List<Object>> onekeySql = buildWhereSqlOneWord(columnMap, multiMap);
            advanceSql.getSecond().addAll(onekeySql.getSecond());
            return Pair.create(StringUtils.joinWith(" AND ", advanceSql.getFirst(), onekeySql.getFirst().replace(" WHERE", "")), advanceSql.getSecond());
        }
    }

    /**
     * 高级多条件复合查询
     */
    public static Pair<String, List<Object>> buildWhereSqlAdvance(Map<String, FieldDefine> columnMap, Map<String, ?> multiMap) {
        String matchLogic = getSingleStringValue(multiMap, "matchLogic", "AND").trim().toUpperCase();
        if (!"AND".equals(matchLogic) && !"OR".equals(matchLogic)) {
            matchLogic = "AND";
        }

        StringJoiner conditionJoiner = new StringJoiner(" " + matchLogic + " ");
        List<Object> params = new ArrayList<>();

        for (String name : multiMap.keySet()) {
            String fieldName = name.replace("[]", "");

            // 过滤系统保留字段和未在模型定义的字段
            if (!columnMap.containsKey(fieldName) || fieldName.endsWith("QueryType") || fieldName.endsWith("Readonly")) {
                if (fieldName.endsWith("QueryType")) {
                    String queryType = getSingleStringValue(multiMap, fieldName, null);
                    if (queryType.equalsIgnoreCase("isnull") || queryType.equalsIgnoreCase("isnotnull")) {
                        fieldName = fieldName.replace("QueryType", "");
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }

            FieldDefine column = columnMap.get(fieldName);
            String dbFieldName = column.getFieldName();
            if (StringUtils.isBlank(dbFieldName)) {
                continue;
            }

            // 查询类型
            String queryType = getSingleStringValue(multiMap, fieldName + "QueryType", null);
            Object mapValue = multiMap.get(name);

            // 解析参数数组
            String[] valueArr = parseStringArray(mapValue);

            // 智能推断缺省 QueryType
            if (StringUtils.isBlank(queryType)) {
                if (valueArr != null && valueArr.length > 1) {
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

            queryType = queryType.toLowerCase();

            // isNull / isNotNull 无需入参值
            if (!"isnull".equals(queryType) && !"isnotnull".equals(queryType)) {
                if (valueArr == null || valueArr.length == 0 || StringUtils.isBlank(valueArr[0])) {
                    continue;
                }
                // SQL 防注入检查
                for (String v : valueArr) {
                    if (StringUtils.isNotBlank(v) && SqlInjectionUtils.check(v)) {
                        throw new BizException("Discovering SQL injection in column: " + column.getName() + ", value: " + v);
                    }
                }
            }

            // 针对逗号分隔的 between / in 进行自动拆分
            if (("between".equals(queryType) || "in".equals(queryType)) && valueArr.length == 1 && valueArr[0].contains(",")) {
                valueArr = valueArr[0].split(",");
            }

            // 构建单字段 SQL 片段
            buildSingleFieldSql(column, dbFieldName, queryType, valueArr, conditionJoiner, params);
        }

        return formatFinalWhereSql(columnMap, conditionJoiner, params);
    }

    /**
     * 单框一键全文搜索（跨所有可搜索字段进行 OR 匹配）
     */
    public static Pair<String, List<Object>> buildWhereSqlOneWord(Map<String, FieldDefine> columnMap, Map<String, ?> multiMap) {
        String oneKeySearch = getSingleStringValue(multiMap, "oneKeySearch", "").trim();
        if (StringUtils.isBlank(oneKeySearch)) {
            return buildLogicDeleteWhere(columnMap);
        }

        if (SqlInjectionUtils.check(oneKeySearch)) {
            throw new BizException("Discovering SQL injection in oneKeySearch: " + oneKeySearch);
        }

        StringJoiner conditionJoiner = new StringJoiner(" OR ");
        List<Object> params = new ArrayList<>();

        for (FieldDefine column : columnMap.values()) {
            if (!YesNo.YES.equals(column.getIsSearch())) {
                continue;
            }
            String dbFieldName = column.getFieldName();
            if (StringUtils.isBlank(dbFieldName)) {
                continue;
            }

            // 字符串字段直接 LIKE
            if (ColumnType.STRING.equals(column.getType())) {
                conditionJoiner.add("\"" + dbFieldName + "\" LIKE ?");
                params.add("%" + oneKeySearch + "%");
            }
            // 数字类型：只有当输入能转为数字时才匹配，防止抛出 NumberFormatException
            else if (isNumericType(column.getType())) {
                if (StringUtils.isNumeric(oneKeySearch)) {
                    try {
                        Object numVal = convertToDbValue(oneKeySearch, column);
                        conditionJoiner.add("\"" + dbFieldName + "\" = ?");
                        params.add(numVal);
                    } catch (Exception ignored) {
                    }
                }
            }
            // 枚举 / 布尔类型：尝试匹配
            else if (column.getType() == ColumnType.ENUM || column.getType() == ColumnType.BOOL) {
                try {
                    Object enumVal = convertToDbValue(oneKeySearch, column);
                    if (enumVal != null) {
                        conditionJoiner.add("\"" + dbFieldName + "\" = ?");
                        params.add(enumVal);
                    }
                } catch (Exception ignored) {
                }
            }
        }

        return formatFinalWhereSql(columnMap, conditionJoiner, params);
    }

    /**
     * 内部单字段操作符 SQL 组装逻辑
     */
    private static void buildSingleFieldSql(FieldDefine column, String dbFieldName, String queryType, String[] valueArr, StringJoiner conditionJoiner, List<Object> params) {
        String finalSql = null;
        List<Object> finalParams = new ArrayList<>();

        switch (queryType) {
            case "eq":
                finalSql = "\"" + dbFieldName + "\" = ?";
                finalParams.add(convertToDbValue(valueArr[0], column));
                break;
            case "ne":
                finalSql = "\"" + dbFieldName + "\" != ?";
                finalParams.add(convertToDbValue(valueArr[0], column));
                break;
            case "gt":
                finalSql = "\"" + dbFieldName + "\" > ?";
                finalParams.add(convertToDbValue(valueArr[0], column));
                break;
            case "ge":
                finalSql = "\"" + dbFieldName + "\" >= ?";
                finalParams.add(convertToDbValue(valueArr[0], column));
                break;
            case "lt":
                finalSql = "\"" + dbFieldName + "\" < ?";
                finalParams.add(convertToDbValue(valueArr[0], column));
                break;
            case "le":
                finalSql = "\"" + dbFieldName + "\" <= ?";
                finalParams.add(convertToDbValue(valueArr[0], column));
                break;
            case "like":
                finalSql = "\"" + dbFieldName + "\" LIKE ?";
                finalParams.add("%" + valueArr[0] + "%");
                break;
            case "likeleft":
                finalSql = "\"" + dbFieldName + "\" LIKE ?";
                finalParams.add(valueArr[0] + "%");
                break;
            case "likeright":
                finalSql = "\"" + dbFieldName + "\" LIKE ?";
                finalParams.add("%" + valueArr[0]);
                break;
            case "in":
                List<?> inList = Arrays.stream(valueArr).filter(StringUtils::isNotBlank).map(v -> convertToDbValue(v.trim(), column)).collect(Collectors.toList());
                if (!inList.isEmpty()) {
                    String placeholders = String.join(",", Collections.nCopies(inList.size(), "?"));
                    finalSql = "\"" + dbFieldName + "\" IN (" + placeholders + ")";
                    finalParams.addAll(inList);
                }
                break;
            case "between":
                if (valueArr.length >= 2 && StringUtils.isNotBlank(valueArr[0]) && StringUtils.isNotBlank(valueArr[1])) {
                    finalSql = "\"" + dbFieldName + "\" BETWEEN ? AND ?";
                    finalParams.add(convertToDbValue(valueArr[0].trim(), column));
                    finalParams.add(convertToDbValue(valueArr[1].trim(), column));
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
            conditionJoiner.add(finalSql);
            params.addAll(finalParams);
        }
    }

    /**
     * 🔥 核心修复：安全包装业务条件，并强制使用 AND 拼接待逻辑删除
     */
    private static Pair<String, List<Object>> formatFinalWhereSql(Map<String, FieldDefine> columnMap, StringJoiner conditionJoiner, List<Object> params) {
        StringBuilder sqlBuilder = new StringBuilder();
        boolean hasLogicDelete = isLogicDeleteEntity(columnMap);

        if (conditionJoiner.length() > 0) {
            // 必须带括号，防止 OR 条件污染逻辑删除！
            sqlBuilder.append("(").append(conditionJoiner).append(")");
            if (hasLogicDelete) {
                sqlBuilder.append(" AND \"IS_DELETED\" = ?");
                params.add(convertToDbValue(Delete.NORMAL, columnMap.get("isDeleted")));
            }
        } else if (hasLogicDelete) {
            sqlBuilder.append("\"IS_DELETED\" = ?");
            params.add(convertToDbValue(Delete.NORMAL, columnMap.get("isDeleted")));
        }

        String where = sqlBuilder.length() > 0 ? " WHERE " + sqlBuilder : "";
        return Pair.create(where, params);
    }

    private static Pair<String, List<Object>> buildLogicDeleteWhere(Map<String, FieldDefine> columnMap) {
        return formatFinalWhereSql(columnMap, new StringJoiner(" AND "), new ArrayList<>());
    }

    private static boolean isNumericType(ColumnType type) {
        return type == ColumnType.INT || type == ColumnType.LONG || type == ColumnType.SHORT || type == ColumnType.BYTE || type == ColumnType.FLOAT || type == ColumnType.DOUBLE;
    }

    private static String[] parseStringArray(Object mapValue) {
        if (mapValue == null) return new String[0];
        if (mapValue.getClass().isArray()) {
            return (String[]) mapValue;
        } else if (mapValue instanceof Collection<?>) {
            return ((Collection<?>) mapValue).stream().map(String::valueOf).toArray(String[]::new);
        } else if (mapValue instanceof String) {
            return new String[]{(String) mapValue};
        } else {
            return new String[]{JacksonUtil.toJson(mapValue)};
        }
    }

    public static boolean isLogicDeleteEntity(Map<String, FieldDefine> columnMap) {
        if (columnMap == null) return false;
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