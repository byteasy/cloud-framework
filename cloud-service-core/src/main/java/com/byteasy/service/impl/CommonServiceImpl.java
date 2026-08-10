package com.byteasy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.common.util.CastUtil;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.common.util.RequestUtil;
import com.byteasy.component.EntityDefineUtil;
import com.byteasy.component.EntityServiceFactory;
import com.byteasy.component.I18nUtil;
import com.byteasy.component.SchemaDefineUtil;
import com.byteasy.dto.Column;
import com.byteasy.dto.EntityDefineDTO;
import com.byteasy.dto.FormatMap;
import com.byteasy.entity.LogicDeleteEntity;
import com.byteasy.enums.*;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.EntityChangeSource;
import com.byteasy.exception.BizException;
import com.byteasy.service.ICommonService;
import com.byteasy.service.IDatabaseMetaService;
import com.byteasy.util.ColumnUtil;
import com.byteasy.util.EventPublishUtil;
import com.byteasy.util.MapUtil;
import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CommonServiceImpl implements ICommonService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SchemaDefineUtil schemaDefineUtil;
    @Autowired
    private EntityDefineUtil entityDefineUtil;
    @Autowired
    private EntityServiceFactory entityServiceFactory;
    @Autowired
    private IDatabaseMetaService databaseMetaService;
    @Autowired
    private EventPublishUtil eventPublishUtil;

    private Set<String> auditEntityPropSet = ColumnUtil.getFieldNames(LogicDeleteEntity.class);

    protected void publicEvent(EntityChangeEvent event) {
        eventPublishUtil.publishEvent(event);
    }

    /**
     * 通用 新增/更新
     *
     * @param className
     * @param entity    字段Map
     * @return 保存后的实体
     */
    @Override
    public Map<String, Object> saveOrUpdate(String className, Map<String, Object> entity) {
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        List<Column> columDefList = schemaDefineUtil.getColumns(entityDefineDTO.getCode(), false);
        Map<String, Column> columnMap = CastUtil.listToMap(columDefList, Column::getName);

        Object id = entity.get("id");
        if (ObjectUtils.isEmpty(id)) {
            insert(entityDefineDTO, entity, columnMap);
            id = entity.get("id");
            publicEvent(new EntityChangeEvent(new EntityChangeSource(EntityChangeSource.ChangeType.CREATE, null, entity, className)));
        } else {
            publicEvent(new EntityChangeEvent(new EntityChangeSource(EntityChangeSource.ChangeType.UPDATE, getById(className, id), entity, className)));
            update(entityDefineDTO, entity, columnMap);
        }
        return getById(entityDefineDTO, id, columnMap);
    }

    @Override
    @Transactional
    public boolean saveOrUpdateBatch(String className, List<Map<String, Object>> entities) {
        if (CollectionUtil.isEmpty(entities)) {
            return false;
        }
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        List<Column> columDefList = schemaDefineUtil.getColumns(entityDefineDTO.getCode(), false);
        Map<String, Column> columnMap = CastUtil.listToMap(columDefList, Column::getName);
        return batchInsert(entityDefineDTO, entities, columnMap) > 0;
    }

    @Override
    public Map<String, Object> getById(String className, Object id) {
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        List<Column> columDefList = schemaDefineUtil.getColumns(entityDefineDTO.getCode(), false);
        Map<String, Column> columnMap = CastUtil.listToMap(columDefList, Column::getName);
        return getById(entityDefineDTO, id, columnMap);
    }

    @Override
    public Page<Map<String, Object>> page(String className) {
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        List<Column> columDefList = schemaDefineUtil.getColumns(entityDefineDTO.getCode(), false);
        Map<String, Column> columnMap = CastUtil.listToMap(columDefList, Column::getName);

        Set<Column> externalKeys = columnMap.values().stream().filter(f -> f.getExternalClass() != null).collect(Collectors.toSet());

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String[]> multiMap = request.getParameterMap();

        Pair<String, List<Object>> whereSql = buildWhereSql(columnMap, multiMap);

        String fromSql = String.format("FROM %s %s", entityDefineDTO.getTableName(), whereSql.getFirst());
        String selectSql = String.format("SELECT %s %s %s %s", getSelectSql(columnMap), fromSql, buildOrderBySql(columnMap), buildLimitSql());
        List list = jdbcTemplate.query(selectSql, whereSql.getValue().toArray(new Object[0]), (rs, rowNum) -> {
            FormatMap map = new FormatMap();
            ResultSetMetaData meta = rs.getMetaData();
            int count = meta.getColumnCount();

            for (int i = 1; i <= count; i++) {
                // 拿到你 AS 定义的驼峰别名（区分大小写）
                String key = meta.getColumnLabel(i);
                if (columnMap.get(key).getSerialPattern() != null) {
                    map.rule(key, columnMap.get(key).getSerialPattern());
                }

                Object value = rs.getObject(i);
                map.put(key, convertToEntityValue(value, columnMap.get(key)));
            }
            return map;
        });
        if (!externalKeys.isEmpty()) {
            entityServiceFactory.updateExternalEntity(externalKeys, list);
        }

        String countSql = "SELECT COUNT(*) " + fromSql;
        Long total = jdbcTemplate.queryForObject(countSql, whereSql.getValue().toArray(new Object[0]), Long.class);

        Page<Map<String, Object>> page = RequestUtil.getPage();
        if (total != null) {
            page.setTotal(total);
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public List<Map<String, Object>> list(String className) {
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        List<Column> columDefList = schemaDefineUtil.getColumns(entityDefineDTO.getCode(), false);
        Map<String, Column> columnMap = CastUtil.listToMap(columDefList, Column::getName);

        Set<Column> externalKeys = columnMap.values().stream().filter(f -> f.getExternalClass() != null).collect(Collectors.toSet());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String[]> multiMap = request.getParameterMap();

        Pair<String, List<Object>> whereSql = buildWhereSql(columnMap, multiMap);

        String fromSql = String.format("FROM %s %s", entityDefineDTO.getTableName(), whereSql.getFirst());
        String selectSql = String.format("SELECT %s %s %s %s", getSelectSql(columnMap), fromSql, buildOrderBySql(columnMap), buildLimitSql());
        List list = jdbcTemplate.query(selectSql, whereSql.getValue().toArray(new Object[0]), (rs, rowNum) -> {
            FormatMap map = new FormatMap();
            ResultSetMetaData meta = rs.getMetaData();
            int count = meta.getColumnCount();

            for (int i = 1; i <= count; i++) {
                // 拿到你 AS 定义的驼峰别名（区分大小写）
                String key = meta.getColumnLabel(i);
                if (columnMap.get(key).getSerialPattern() != null) {
                    map.rule(key, columnMap.get(key).getSerialPattern());
                }

                Object value = rs.getObject(i);
                map.put(key, convertToEntityValue(value, columnMap.get(key)));
            }
            return map;
        });
        if (!externalKeys.isEmpty()) {
            entityServiceFactory.updateExternalEntity(externalKeys, list);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> list(String className, Map<String, Object> args) {
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        List<Column> columDefList = schemaDefineUtil.getColumns(entityDefineDTO.getCode(), false);
        Map<String, Column> columnMap = CastUtil.listToMap(columDefList, Column::getName);

        Set<Column> externalKeys = columnMap.values().stream().filter(f -> f.getExternalClass() != null).collect(Collectors.toSet());

        Pair<String, List<Object>> whereSql = buildWhereSql(columnMap, args);

        String fromSql = String.format("FROM %s %s", entityDefineDTO.getTableName(), whereSql.getFirst());
        String selectSql = String.format("SELECT %s %s %s %s", getSelectSql(columnMap), fromSql, buildOrderBySql(columnMap, args), buildLimitSql(args));
        List list = jdbcTemplate.query(selectSql, whereSql.getValue().toArray(new Object[0]), (rs, rowNum) -> {
            FormatMap map = new FormatMap();
            ResultSetMetaData meta = rs.getMetaData();
            int count = meta.getColumnCount();

            for (int i = 1; i <= count; i++) {
                // 拿到你 AS 定义的驼峰别名（区分大小写）
                String key = meta.getColumnLabel(i);
                if (columnMap.get(key).getSerialPattern() != null) {
                    map.rule(key, columnMap.get(key).getSerialPattern());
                }

                Object value = rs.getObject(i);
                map.put(key, convertToEntityValue(value, columnMap.get(key)));
            }
            return map;
        });
        if (!externalKeys.isEmpty()) {
            entityServiceFactory.updateExternalEntity(externalKeys, list);
        }
        return list;
    }

    @Override
    public Map<String, Object> getOne(String className, Map<String, Object> args) {
        List<Map<String, Object>> list = list(className, args);
        if (list.size() == 1) {
            return list.getFirst();
        } else if (list.size() > 1) {
            throw new BizException("Expect one row, but actual: " + list.size());
        }
        return null;
    }

    @Override
    public boolean removeById(String className, Object id) {
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        List<Column> columDefList = schemaDefineUtil.getColumns(entityDefineDTO.getCode(), false);
        Map<String, Column> columnMap = CastUtil.listToMap(columDefList, Column::getName);

        publicEvent(new EntityChangeEvent(new EntityChangeSource(EntityChangeSource.ChangeType.DELETE, getById(className, id), null, className)));
        if (isLogicDeleteEntity(columnMap)) {
            StringJoiner setJoiner = new StringJoiner(",");
            List<Object> params = new ArrayList<>();

            setJoiner.add("IS_DELETED=1");
            // 处理审计相关信息
            UserInfo userInfo = TokenUtil.getUserinfo();
            if (columnMap.containsKey("deleterId")) {
                setJoiner.add(columnMap.get("deleterId").getFieldName() + "=?");
                params.add(userInfo.getUserId());
            }
            if (columnMap.containsKey("deletedBy")) {
                setJoiner.add(columnMap.get("deletedBy").getFieldName() + "=?");
                params.add(StringUtils.deleteWhitespace(userInfo.getName()));
            }
            if (columnMap.containsKey("deletedTime")) {
                setJoiner.add(columnMap.get("deletedTime").getFieldName() + "=?");
                params.add(convertToDbValue(LocalDateTime.now(), columnMap.get("deletedTime")));
            }

            // 处理乐观锁
            setJoiner.add("VERSION=VERSION + 1");

            params.add(convertToDbValue(id, columnMap.get("id")));
            String sql = String.format(
                    "UPDATE %s SET %s WHERE ID=?",
                    entityDefineDTO.getTableName(),
                    setJoiner
            );
            int rows = jdbcTemplate.update(sql, params.toArray());
            if (rows < 1) {
                return false;
            }
        } else {
            String sql = "DELETE FROM " + entityDefineDTO.getTableName();
            sql += " WHERE ID=?";
            int rows = jdbcTemplate.update(sql, convertToDbValue(id, columnMap.get("id")));
            if (rows < 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Collection<Column> getColumns(String className) {
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        String schema = databaseMetaService.getCurrentSchema();
        return databaseMetaService.getColumns(className, schema, entityDefineDTO.getTableName());
    }

    @Override
    public Collection<Column> getColumnsByTableName(String tableName) {
        String schema = databaseMetaService.getCurrentSchema();
        return databaseMetaService.getColumns(null, schema, tableName);
    }

    private boolean isLogicDeleteEntity(Map<String, Column> columnMap) {
        for (String s : auditEntityPropSet) {
            if (!columnMap.containsKey(s)) {
                return false;
            }
        }
        return true;
    }

    private String buildLimitSql(Map<String, Object> args) {
        int size = MapUtils.getIntValue(args, "size", 20);
        int current = MapUtils.getIntValue(args, "current", 1);
        return String.format("limit %s offset %s", size, (current - 1) * size);
    }

    private String buildLimitSql() {
        Page page = RequestUtil.getPage();
        return String.format("limit %s offset %s", page.getSize(), (page.getCurrent() - 1) * page.getSize());
    }

    private String buildOrderBySql(Map<String, Column> columnMap, Map<String, Object> args) {
        StringJoiner sql = new StringJoiner(",");

        List descColumns = (List) args.get("descColumns");
        if (!CollectionUtils.isEmpty(descColumns)) {
            List<String> colNames = descColumns.stream().filter(f -> columnMap.containsKey(f) && StringUtils.isNotEmpty(columnMap.get(f).getFieldName()))
                    .map(k -> columnMap.get(k).getFieldName()).toList();
            for (String colName : colNames) {
                if (SqlInjectionUtils.check(colName)) {
                    throw new BizException("Discovering SQL injection column: " + colName);
                }
                sql.add(colName + " desc");
            }
        }

        List ascColumns = (List) args.get("ascColumns");
        if (!CollectionUtils.isEmpty(ascColumns)) {
            List<String> colNames = ascColumns.stream().filter(f -> columnMap.containsKey(f) && StringUtils.isNotEmpty(columnMap.get(f).getFieldName()))
                    .map(k -> columnMap.get(k).getFieldName()).toList();
            for (String colName : colNames) {
                if (SqlInjectionUtils.check(colName)) {
                    throw new BizException("Discovering SQL injection column: " + colName);
                }
                sql.add(colName + " asc");
            }
        }
        if (sql.length() > 0) {
            return String.format("order by %s", sql);
        } else {
            return "";
        }
    }

    private String buildOrderBySql(Map<String, Column> columnMap) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String[]> multiMap = request.getParameterMap();
        StringJoiner sql = new StringJoiner(",");

        String[] descColumns = multiMap.get("descColumns");
        if (!(descColumns == null || descColumns.length < 1)) {
            List<String> colNames = Arrays.stream(descColumns).filter(f -> columnMap.containsKey(f) && StringUtils.isNotEmpty(columnMap.get(f).getFieldName()))
                    .map(k -> columnMap.get(k).getFieldName()).toList();
            for (String colName : colNames) {
                if (SqlInjectionUtils.check(colName)) {
                    throw new BizException("Discovering SQL injection column: " + colName);
                }
                sql.add(colName + " desc");
            }
        }

        String[] ascColumns = multiMap.get("ascColumns");
        if (!(ascColumns == null || ascColumns.length < 1)) {
            List<String> colNames = Arrays.stream(ascColumns).filter(f -> columnMap.containsKey(f) && StringUtils.isNotEmpty(columnMap.get(f).getFieldName()))
                    .map(k -> columnMap.get(k).getFieldName()).toList();
            for (String colName : colNames) {
                if (SqlInjectionUtils.check(colName)) {
                    throw new BizException("Discovering SQL injection column: " + colName);
                }
                sql.add(colName + " asc");
            }
        }
        if (sql.length() > 0) {
            return String.format("order by %s", sql);
        } else {
            return "";
        }
    }

    private Pair<String, List<Object>> buildWhereSql(Map<String, Column> columnMap, Map<String, ?> multiMap) {
        StringJoiner whereStr = new StringJoiner(" AND ");

        // 保存查询参数，后面要传给 jdbcTemplate
        List<Object> params = new ArrayList<>();

        for (String name : multiMap.keySet()) {
            String fieldName = name;
            String defaultQueryType = null;
            Object mapValue = multiMap.get(name);

            // 处理数组参数 name[]
            if (fieldName.contains("[]")) {
                fieldName = fieldName.replace("[]", "");
                defaultQueryType = "in";
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
                        finalParams.add(valueArr[0] + "%");
                    }
                    break;

                case "likeleft":
                    if (StringUtils.isNotBlank(valueArr[0])) {
                        finalSql = "\"" + dbFieldName + "\" LIKE ?";
                        finalParams.add("%" + valueArr[0]);
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

    private void insert(EntityDefineDTO entityDefineDTO, Map<String, Object> entity, Map<String, Column> columnMap) {
        Set<String> columns = entity.keySet();
        if (columns.isEmpty()) return;

        StringJoiner colJoiner = new StringJoiner(",");
        StringJoiner valJoiner = new StringJoiner(",");
        List<Object> params = new ArrayList<>();

        for (String col : columns) {
            Column column = columnMap.get(col);
            if (column == null) continue;
            if (auditEntityPropSet.contains(col)) continue;
            if (RelationType.DETAIL_TABLE.equals(column.getRelationType())) continue;

            colJoiner.add(column.getFieldName());
            valJoiner.add("?");
            if (SqlInjectionUtils.check(String.valueOf(entity.get(col)))) {
                throw new BizException("Discovering SQL injection column: " + column.getName() + ", value: " + entity.get(col));
            }
            params.add(convertToDbValue(entity.get(col), column));
        }

        // 处理创建人信息
        UserInfo userInfo = TokenUtil.getUserinfo();
        if (columnMap.containsKey("creatorId")) {
            colJoiner.add(columnMap.get("creatorId").getFieldName());
            valJoiner.add("?");
            params.add(convertToDbValue(userInfo.getUserId(), columnMap.get("creatorId")));
        }
        if (columnMap.containsKey("createdBy")) {
            colJoiner.add(columnMap.get("createdBy").getFieldName());
            valJoiner.add("?");
            params.add(convertToDbValue(userInfo.getUserId(), columnMap.get("createdBy")));
        }
        if (columnMap.containsKey("createdTime")) {
            colJoiner.add(columnMap.get("createdTime").getFieldName());
            valJoiner.add("?");
            params.add(convertToDbValue(LocalDateTime.now(), columnMap.get("createdTime")));
        }

        String sql = String.format(
                "INSERT INTO %s (%s) VALUES (%s)",
                entityDefineDTO.getTableName(),
                colJoiner,
                valJoiner
        );

        // ===================== 【关键：返回主键 ID】=====================
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            return ps;
        }, keyHolder);
        if (!CollectionUtil.isEmpty(keyHolder.getKeys()) && columnMap.containsKey("id")) {
            entity.put("id", keyHolder.getKeys().get("id"));
        }
    }

    private int batchInsert(EntityDefineDTO entityDefineDTO, List<Map<String, Object>> entityList, Map<String, Column> columnMap) {
        if (CollectionUtil.isEmpty(entityList)) return 0;

        // 取第一个实体，获取需要插入的字段（所有实体共用相同字段结构）
        Map<String, Object> firstEntity = entityList.get(0);
        Set<String> columns = firstEntity.keySet();
        if (columns.isEmpty()) return 0;

        // 构建字段部分（所有行共用）
        StringJoiner colJoiner = new StringJoiner(",");
        List<String> fieldList = new ArrayList<>();

        for (String col : columns) {
            Column column = columnMap.get(col);
            if (column == null) continue;
            if (auditEntityPropSet.contains(col)) continue;
            if (RelationType.DETAIL_TABLE.equals(column.getRelationType())) continue;
            fieldList.add(col);
            colJoiner.add(column.getFieldName());
        }

        // 处理审计字段（创建人、时间）
        UserInfo userInfo = TokenUtil.getUserinfo();
        if (columnMap.containsKey("creatorId")) {
            fieldList.add("creatorId");
            colJoiner.add(columnMap.get("creatorId").getFieldName());
        }
        if (columnMap.containsKey("createdBy")) {
            fieldList.add("createdBy");
            colJoiner.add(columnMap.get("createdBy").getFieldName());
        }
        if (columnMap.containsKey("createdTime")) {
            fieldList.add("createdTime");
            colJoiner.add(columnMap.get("createdTime").getFieldName());
        }

        // 批量参数集合
        List<Object> totalParams = new ArrayList<>();
        String valuesPlaceholder;

        // 构建单条记录的 ? 占位符
        StringJoiner valSingleJoiner = new StringJoiner(",");
        for (int i = 0; i < fieldList.size(); i++) {
            valSingleJoiner.add("?");
        }
        valuesPlaceholder = "(" + valSingleJoiner + ")";

        // 构建所有行的占位符
        StringJoiner allValues = new StringJoiner(",");
        for (Map<String, Object> entity : entityList) {
            allValues.add(valuesPlaceholder);

            // 逐个字段加入参数
            for (String field : fieldList) {
                Column column = columnMap.get(field);

                // 审计字段自动填充
                Object value;
                if ("creatorId".equals(field)) {
                    value = userInfo.getUserId();
                } else if ("createdBy".equals(field)) {
                    value = userInfo.getUserId();
                } else if ("createdTime".equals(field)) {
                    value = LocalDateTime.now();
                } else {
                    value = entity.get(field);
                }

                // SQL注入检查,脚本字段不做检查
                if (!YesNo.NO.equals(column.getSqlInjectionCheck())) {
                    if (SqlInjectionUtils.check(String.valueOf(value))) {
                        throw new BizException("Discovering SQL injection column: " + column.getName() + ", value: " + value);
                    }
                }

                // 转换数据库类型
                totalParams.add(convertToDbValue(value, column));
            }
        }

        // 最终批量插入SQL
        String sql = String.format(
                "INSERT INTO %s (%s) VALUES %s",
                entityDefineDTO.getTableName(),
                colJoiner,
                allValues
        );

        // 执行批量
        return jdbcTemplate.update(sql, totalParams.toArray());
    }

    // ============= 通用更新 =============
    private void update(EntityDefineDTO entityDefineDTO, Map<String, Object> entity, Map<String, Column> columnMap) {
        Set<String> columns = entity.keySet();
        if (columns.isEmpty()) return;

        StringJoiner setJoiner = new StringJoiner(",");
        List<Object> params = new ArrayList<>();

        for (String col : columns) {
            // 审计信息不允许修改
            if (auditEntityPropSet.contains(col)) {
                continue;
            }
            Column column = columnMap.get(col);
            // 无明确字段定义
            if (column == null) {
                continue;
            }
            // 明细表，数据库无此字段
            if (RelationType.DETAIL_TABLE.equals(column.getRelationType())) {
                continue;
            }
            // 数据库无此字段
            if (StringUtils.isEmpty(column.getFieldName())) {
                continue;
            }
            setJoiner.add(column.getFieldName() + "=?");
            if (!YesNo.NO.equals(column.getSqlInjectionCheck())) {
                if (SqlInjectionUtils.check(String.valueOf(entity.get(col)))) {
                    throw new BizException("Discovering SQL injection column: " + column.getName() + ", value: " + entity.get(col));
                }
            }
            params.add(convertToDbValue(entity.get(col), columnMap.get(col)));
        }
        // 处理审计相关信息
        UserInfo userInfo = TokenUtil.getUserinfo();
        if (columnMap.containsKey("updaterId")) {
            setJoiner.add(columnMap.get("updaterId").getFieldName() + "=?");
            params.add(userInfo.getUserId());
        }
        if (columnMap.containsKey("updatedBy")) {
            setJoiner.add(columnMap.get("updatedBy").getFieldName() + "=?");
            params.add(StringUtils.deleteWhitespace(userInfo.getName()));
        }
        if (columnMap.containsKey("updatedTime")) {
            setJoiner.add(columnMap.get("updatedTime").getFieldName() + "=?");
            params.add(convertToDbValue(LocalDateTime.now(), columnMap.get("updatedTime")));
        }

        // 处理乐观锁
        setJoiner.add("VERSION=VERSION + 1");

        params.add(convertToDbValue(entity.get("id"), columnMap.get("id")));
        params.add(convertToDbValue(entity.get("version"), columnMap.get("version")));
        String sql = String.format(
                "UPDATE %s SET %s WHERE ID=? AND VERSION=?",
                entityDefineDTO.getTableName(),
                setJoiner
        );
        int rows = jdbcTemplate.update(sql, params.toArray());
        if (rows < 1) {
            throw new BizException(I18nUtil.getMessage("saveRecordFailed"));
        }
    }

    // ============= 根据ID查询（返回保存后的数据） =============
    private Map<String, Object> getById(EntityDefineDTO entityDefineDTO, Object id, Map<String, Column> columnMap) {
        Set<Column> externalKeys = columnMap.values().stream().filter(f -> f.getExternalClass() != null).collect(Collectors.toSet());

        String sql = String.format("SELECT %s FROM %s WHERE ID=?", getSelectSql(columnMap), entityDefineDTO.getTableName());

        List args = Lists.newArrayList();
        args.add(convertToDbValue(id, columnMap.get("id")));

        if (isLogicDeleteEntity(columnMap)) {
            sql += "AND \"IS_DELETED\"=?";
            args.add(convertToDbValue(Delete.NORMAL, columnMap.get("isDeleted")));
        }
        try {

            FormatMap formatMap = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                FormatMap map = new FormatMap();
                ResultSetMetaData meta = rs.getMetaData();
                int count = meta.getColumnCount();

                for (int i = 1; i <= count; i++) {
                    // 拿到你 AS 定义的驼峰别名（区分大小写）
                    String key = meta.getColumnLabel(i);
                    if (columnMap.get(key).getSerialPattern() != null) {
                        map.rule(key, columnMap.get(key).getSerialPattern());
                    }

                    Object value = rs.getObject(i);
                    map.put(key, convertToEntityValue(value, columnMap.get(key)));
                }
                if (!externalKeys.isEmpty()) {
                    entityServiceFactory.updateExternalEntity(externalKeys, List.of(map));
                }
                return map;
            }, args.toArray());
            return formatMap;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private String getSelectSql(Map<String, Column> columnMap) {
        StringJoiner selJoiner = new StringJoiner(",");
        for (Column column : columnMap.values()) {
            if (RelationType.DETAIL_TABLE.equals(column.getRelationType())) {
                continue;
            }
            // 数据库无此字段
            if (StringUtils.isEmpty(column.getFieldName())) {
                continue;
            }
            selJoiner.add(String.format("%s as \"%s\"", column.getFieldName(), column.getName()));
        }
        return selJoiner.toString();
    }

    public Object convertToEntityValue(Object value, Column column) {
        switch (column.getType()) {
            case ENUM -> {
                String className = column.getTypeClassName();
                if (className == null) return value;
                try {
                    Class<?> clazz = Class.forName(className);
                    // 必须是枚举，且实现 IBaseEnum
                    if (!clazz.isEnum() || !IBaseEnum.class.isAssignableFrom(clazz)) {
                        return value;
                    }
                    // 拿到所有枚举实例
                    Object[] enums = clazz.getEnumConstants();
                    for (Object e : enums) {
                        if (e instanceof IBaseEnum<?>) {
                            IBaseEnum<?> enumObj = (IBaseEnum<?>) e;
                            if (enumObj.getValue().equals(value)) {
                                return enumObj;
                            }
                        }
                    }
                    return value;
                } catch (Exception e) {
                    // 枚举从数据库中定义的1、2、3...等转为枚举字母单词
                    List<Column> enumDefineList = schemaDefineUtil.getColumns(className, false);
                    for (Column enumDef : enumDefineList) {
                        if (enumDef.getFieldName().equals(String.valueOf(value))) {
                            return enumDef.getName();
                        }
                    }
                    return value;
                }
            }
        }
        return value;
    }

    public Object convertToDbValue(Object value, Column column) {
        if (value == null) {
            return null;
        }
        if (!(value instanceof String valStr)) {
            return value;
        }
        if (valStr.isEmpty()) {
            return null;
        }

        try {
            return switch (column.getType()) {
                case BYTE, SHORT, INT, LONG -> Long.valueOf(valStr);
                case FLOAT, DOUBLE -> Double.valueOf(valStr);
                case BOOL -> Boolean.valueOf(valStr);
                case LOCAL_DATE_TIME ->
                        Timestamp.valueOf(LocalDateTime.parse(valStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                case LOCAL_DATE ->
                        Timestamp.valueOf(LocalDateTime.parse(valStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                case ENUM -> {
                    String className = column.getTypeClassName();
                    if (className == null) yield null;

                    try {
                        Class<?> clazz = Class.forName(className);

                        // 必须是枚举，且实现 IBaseEnum
                        if (!clazz.isEnum() || !IBaseEnum.class.isAssignableFrom(clazz)) {
                            yield null;
                        }

                        // 拿到所有枚举实例
                        Object[] enums = clazz.getEnumConstants();
                        for (Object e : enums) {
                            if (e instanceof IBaseEnum<?>) {
                                IBaseEnum<?> enumObj = (IBaseEnum<?>) e;
                                // 把枚举值转成字符串比较，匹配传入的 valStr
                                if (String.valueOf(enumObj.getEnumName()).equals(valStr)) {
                                    yield enumObj.getValue();
                                }
                            }
                        }
                        yield null;
                    } catch (Exception e) {
                        // 枚举从数据库中定义的1、2、3...等转为枚举字母单词
                        Object resultValue = null;
                        List<Column> enumDefineList = schemaDefineUtil.getColumns(className, false);
                        for (Column enumDef : enumDefineList) {
                            if (enumDef.getName().equals(valStr)) {
                                switch (enumDef.getType()) {
                                    case BYTE, SHORT, INT, LONG -> resultValue = Long.valueOf(enumDef.getFieldName());
                                    case BOOL -> resultValue = Boolean.valueOf(enumDef.getFieldName());
                                    case FLOAT, DOUBLE -> resultValue = Double.valueOf(enumDef.getFieldName());
                                    case STRING -> resultValue = enumDef.getFieldName();
                                }
                            }
                        }
                        yield resultValue;
                    }
                }
                default -> valStr;
            };
        } catch (Exception e) {
            return value;
        }
    }
}
