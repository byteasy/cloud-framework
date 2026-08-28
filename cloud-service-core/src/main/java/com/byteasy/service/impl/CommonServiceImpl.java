package com.byteasy.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.byteasy.common.token.TokenUtil;
import com.byteasy.common.token.UserInfo;
import com.byteasy.common.util.CastUtil;
import com.byteasy.common.util.RequestUtil;
import com.byteasy.component.EntityDefineUtil;
import com.byteasy.component.EntityServiceFactory;
import com.byteasy.component.FieldDefineUtil;
import com.byteasy.component.I18nUtil;
import com.byteasy.dto.Column;
import com.byteasy.dto.EntityDefineDTO;
import com.byteasy.dto.FormatMap;
import com.byteasy.enums.Delete;
import com.byteasy.enums.RelationType;
import com.byteasy.enums.YesNo;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.EntityChangeSource;
import com.byteasy.exception.BizException;
import com.byteasy.service.ICommonService;
import com.byteasy.service.IDatabaseMetaService;
import com.byteasy.util.ColumnUtil;
import com.byteasy.util.EventPublishUtil;
import com.byteasy.util.SqlUtil;
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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.byteasy.util.ColumnUtil.convertToDbValue;
import static com.byteasy.util.ColumnUtil.convertToEntityValue;

@Service
@Log4j2
public class CommonServiceImpl implements ICommonService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private FieldDefineUtil fieldDefineUtil;
    @Autowired
    private EntityDefineUtil entityDefineUtil;
    @Autowired
    private EntityServiceFactory entityServiceFactory;
    @Autowired
    private IDatabaseMetaService databaseMetaService;
    @Autowired
    private EventPublishUtil eventPublishUtil;

    protected void publicEvent(EntityChangeEvent event) {
        eventPublishUtil.publishEvent(event);
    }

    private Column getPrimaryColumn(Collection<Column> columnDefList) {
        if (CollectionUtil.isEmpty(columnDefList)) {
            throw new BizException("实体字段定义列表为空，无法获取主键");
        }
        return columnDefList.stream()
                .filter(s -> YesNo.YES.equals(s.getIsPrimary()))
                .findFirst()
                .orElseThrow(() -> {
                    String className = columnDefList.iterator().next().getClassName();
                    return new BizException("className: " + className + " 缺少主键定义");
                });
    }

    /**
     * 通用 新增/更新
     *
     * @param className 实体类名
     * @param entity    字段Map
     * @return 保存后的实体数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveOrUpdate(String className, Map<String, Object> entity) {
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        List<Column> columnDefList = fieldDefineUtil.getColumns(entityDefineDTO.getCode(), false);
        Map<String, Column> columnMap = CastUtil.listToMap(columnDefList, Column::getName);
        Column primaryColumn = getPrimaryColumn(columnDefList);

        Object primaryKeyValue = entity.get(primaryColumn.getName());
        if (ObjectUtils.isEmpty(primaryKeyValue)) {
            insert(entityDefineDTO, entity, columnMap, primaryColumn);
            primaryKeyValue = entity.get(primaryColumn.getName());
            publicEvent(new EntityChangeEvent(new EntityChangeSource(EntityChangeSource.ChangeType.CREATE, null, entity, className)));
        } else {
            Map<String, Object> oldEntity = getByPrimaryKey(className, primaryKeyValue);
            update(entityDefineDTO, entity, columnMap, primaryColumn);
            publicEvent(new EntityChangeEvent(new EntityChangeSource(EntityChangeSource.ChangeType.UPDATE, oldEntity, entity, className)));
        }
        return getByPrimaryKey(entityDefineDTO, primaryKeyValue, columnMap, primaryColumn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateBatch(String className, List<Map<String, Object>> entities) {
        if (CollectionUtil.isEmpty(entities)) {
            return false;
        }

        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        List<Column> columnDefList = fieldDefineUtil.getColumns(entityDefineDTO.getCode(), false);
        Map<String, Column> columnMap = CastUtil.listToMap(columnDefList, Column::getName);
        Column primaryColumn = getPrimaryColumn(columnDefList);

        // 拆分为新增和更新列表
        List<Map<String, Object>> insertList = new ArrayList<>();
        List<Map<String, Object>> updateList = new ArrayList<>();

        for (Map<String, Object> entity : entities) {
            if (ObjectUtils.isEmpty(entity.get(primaryColumn.getName()))) {
                insertList.add(entity);
            } else {
                updateList.add(entity);
            }
        }

        boolean success = true;

        // 1. 批量新增
        if (CollectionUtil.isNotEmpty(insertList)) {
            int rows = batchInsert(entityDefineDTO, insertList, columnMap);
            if (rows <= 0) {
                success = false;
            } else {
                for (Map<String, Object> entity : insertList) {
                    publicEvent(new EntityChangeEvent(new EntityChangeSource(EntityChangeSource.ChangeType.CREATE, null, entity, className)));
                }
            }
        }

        // 2. 批量更新（使用 IN 查询消除 N+1 查询旧数据）
        if (CollectionUtil.isNotEmpty(updateList)) {
            List<Object> ids = updateList.stream()
                    .map(e -> e.get(primaryColumn.getName()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            Map<Object, Map<String, Object>> oldDataMap = getEntitiesByIds(entityDefineDTO, ids, columnMap, primaryColumn);

            batchUpdate(entityDefineDTO, updateList, columnMap, primaryColumn);

            for (Map<String, Object> entity : updateList) {
                Object primaryKeyValue = entity.get(primaryColumn.getName());
                Map<String, Object> oldEntity = oldDataMap.get(primaryKeyValue);
                publicEvent(new EntityChangeEvent(new EntityChangeSource(EntityChangeSource.ChangeType.UPDATE, oldEntity, entity, className)));
            }
        }

        return success;
    }

    @Override
    public Map<String, Object> getByPrimaryKey(String className, Object primaryKeyValue) {
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        List<Column> columnDefList = fieldDefineUtil.getColumns(entityDefineDTO.getCode(), false);
        Map<String, Column> columnMap = CastUtil.listToMap(columnDefList, Column::getName);
        return getByPrimaryKey(entityDefineDTO, primaryKeyValue, columnMap, getPrimaryColumn(columnDefList));
    }

    @Override
    public Page<Map<String, Object>> page(String className) {
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        List<Column> columnDefList = fieldDefineUtil.getColumns(entityDefineDTO.getCode(), false);
        Map<String, Column> columnMap = CastUtil.listToMap(columnDefList, Column::getName);

        HttpServletRequest request = getHttpServletRequest();
        Map<String, String[]> multiMap = request != null ? request.getParameterMap() : Collections.emptyMap();

        Pair<String, List<Object>> whereSql = SqlUtil.buildWhereSql(columnMap, multiMap);
        String fromSql = String.format("FROM %s %s", entityDefineDTO.getTableName(), whereSql.getFirst());
        String selectSql = String.format("SELECT %s %s %s %s", getSelectSql(columnMap), fromSql, buildOrderBySqlByRequest(columnMap, multiMap), buildLimitSql());

        List<FormatMap> list = queryListInternal(selectSql, whereSql.getValue().toArray(new Object[0]), columnMap);

        String countSql = "SELECT COUNT(*) " + fromSql;
        Long total = jdbcTemplate.queryForObject(countSql, whereSql.getValue().toArray(new Object[0]), Long.class);

        Page<Map<String, Object>> page = RequestUtil.getPage();
        if (total != null) {
            page.setTotal(total);
        }
        page.setRecords((List) list);
        return page;
    }

    @Override
    public List<Map<String, Object>> list(String className) {
        HttpServletRequest request = getHttpServletRequest();
        Map<String, String[]> multiMap = request != null ? request.getParameterMap() : Collections.emptyMap();
        return (List) listInternal(className, multiMap);
    }

    @Override
    public List<Map<String, Object>> list(String className, Map<String, Object> args) {
        return (List) listInternal(className, args);
    }

    private List<FormatMap> listInternal(String className, Object queryArgs) {
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        List<Column> columnDefList = fieldDefineUtil.getColumns(entityDefineDTO.getCode(), false);
        Map<String, Column> columnMap = CastUtil.listToMap(columnDefList, Column::getName);

        Pair<String, List<Object>> whereSql;
        String orderBySql;
        String limitSql;

        if (queryArgs instanceof Map && !((Map<?, ?>) queryArgs).isEmpty() && ((Map<?, ?>) queryArgs).values().iterator().next() instanceof String[]) {
            // 来自 HTTP ParameterMap
            Map<String, String[]> multiMap = (Map<String, String[]>) queryArgs;
            whereSql = SqlUtil.buildWhereSql(columnMap, multiMap);
            orderBySql = buildOrderBySqlByRequest(columnMap, multiMap);
            limitSql = buildLimitSql();
        } else {
            // 来自业务 Map<String, Object>
            Map<String, Object> args = queryArgs != null ? (Map<String, Object>) queryArgs : Collections.emptyMap();
            whereSql = SqlUtil.buildWhereSql(columnMap, args);
            orderBySql = buildOrderBySql(columnMap, args);
            limitSql = buildLimitSql(args);
        }

        String fromSql = String.format("FROM %s %s", entityDefineDTO.getTableName(), whereSql.getFirst());
        String selectSql = String.format("SELECT %s %s %s %s", getSelectSql(columnMap), fromSql, orderBySql, limitSql);

        return queryListInternal(selectSql, whereSql.getValue().toArray(new Object[0]), columnMap);
    }

    @Override
    public Map<String, Object> getOne(String className, Map<String, Object> args) {
        List<Map<String, Object>> list = list(className, args);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        throw new BizException("Expect one row, but actual: " + list.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPrimaryKey(String className, Map<String, Object> entity) {
        if (MapUtils.isEmpty(entity)) {
            return false;
        }
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        List<Column> columnDefList = fieldDefineUtil.getColumns(entityDefineDTO.getCode(), false);
        Column primaryColumn = getPrimaryColumn(columnDefList);
        return removeByPrimaryKey(className, entity.get(primaryColumn.getName()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByPrimaryKey(String className, Object primaryKey) {
        if (ObjectUtils.isEmpty(primaryKey)) {
            throw new BizException("primary key is null");
        }
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        List<Column> columnDefList = fieldDefineUtil.getColumns(entityDefineDTO.getCode(), false);
        Map<String, Column> columnMap = CastUtil.listToMap(columnDefList, Column::getName);
        Column primaryColumn = getPrimaryColumn(columnDefList);

        Map<String, Object> oldEntity = getByPrimaryKey(className, primaryKey);
        publicEvent(new EntityChangeEvent(new EntityChangeSource(EntityChangeSource.ChangeType.DELETE, oldEntity, null, className)));

        if (SqlUtil.isLogicDeleteEntity(columnMap)) {
            StringJoiner setJoiner = new StringJoiner(",");
            List<Object> params = new ArrayList<>();

            // 动态逻辑删除字段与物理列名
            Column isDeletedCol = columnMap.get("isDeleted");
            setJoiner.add(String.format("%s=1", isDeletedCol != null ? isDeletedCol.getFieldName() : "is_deleted"));

            // 审计字段
            UserInfo userInfo = TokenUtil.getUserinfo();
            if (userInfo != null) {
                if (columnMap.containsKey("deleterId")) {
                    setJoiner.add(columnMap.get("deleterId").getFieldName() + "=?");
                    params.add(userInfo.getUserId());
                }
                if (columnMap.containsKey("deletedBy")) {
                    setJoiner.add(columnMap.get("deletedBy").getFieldName() + "=?");
                    params.add(StringUtils.deleteWhitespace(userInfo.getName()));
                }
            }
            if (columnMap.containsKey("deletedTime")) {
                setJoiner.add(columnMap.get("deletedTime").getFieldName() + "=?");
                params.add(convertToDbValue(LocalDateTime.now(), columnMap.get("deletedTime")));
            }

            // 处理乐观锁
            if (columnMap.containsKey("version")) {
                String versionColName = columnMap.get("version").getFieldName();
                setJoiner.add(String.format("%s=%s + 1", versionColName, versionColName));
            }

            params.add(convertToDbValue(primaryKey, primaryColumn));
            String sql = String.format("UPDATE %s SET %s WHERE %s=?",
                    entityDefineDTO.getTableName(),
                    setJoiner,
                    primaryColumn.getFieldName()
            );
            return jdbcTemplate.update(sql, params.toArray()) >= 1;
        } else {
            String sql = String.format("DELETE FROM %s WHERE %s=?", entityDefineDTO.getTableName(), primaryColumn.getFieldName());
            return jdbcTemplate.update(sql, convertToDbValue(primaryKey, primaryColumn)) >= 1;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchRemove(String className, List<Map<String, Object>> entities) {
        if (CollectionUtil.isEmpty(entities)) {
            return false;
        }
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        List<Column> columnDefList = fieldDefineUtil.getColumns(entityDefineDTO.getCode(), false);
        Map<String, Column> columnMap = CastUtil.listToMap(columnDefList, Column::getName);
        Column primaryColumn = getPrimaryColumn(columnDefList);

        publicEvent(new EntityChangeEvent(new EntityChangeSource(EntityChangeSource.ChangeType.BATCH_DELETE, entities, null, className)));

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        Set<Object> primaryKeys = ColumnUtil.getPrimaryKeys(primaryColumn, entities);
        paramSource.addValue("primaryKeys", primaryKeys);

        if (SqlUtil.isLogicDeleteEntity(columnMap)) {
            StringJoiner setJoiner = new StringJoiner(",");

            Column isDeletedCol = columnMap.get("isDeleted");
            setJoiner.add(String.format("%s=1", isDeletedCol != null ? isDeletedCol.getFieldName() : "is_deleted"));

            UserInfo userInfo = TokenUtil.getUserinfo();
            if (userInfo != null) {
                if (columnMap.containsKey("deleterId")) {
                    setJoiner.add(columnMap.get("deleterId").getFieldName() + "=:deleterId");
                    paramSource.addValue("deleterId", userInfo.getUserId());
                }
                if (columnMap.containsKey("deletedBy")) {
                    setJoiner.add(columnMap.get("deletedBy").getFieldName() + "=:deletedBy");
                    paramSource.addValue("deletedBy", StringUtils.deleteWhitespace(userInfo.getName()));
                }
            }
            if (columnMap.containsKey("deletedTime")) {
                setJoiner.add(columnMap.get("deletedTime").getFieldName() + "=:deletedTime");
                paramSource.addValue("deletedTime", convertToDbValue(LocalDateTime.now(), columnMap.get("deletedTime")));
            }

            if (columnMap.containsKey("version")) {
                String versionColName = columnMap.get("version").getFieldName();
                setJoiner.add(String.format("%s=%s + 1", versionColName, versionColName));
            }

            String sql = String.format("UPDATE %s SET %s WHERE %s IN (:primaryKeys)",
                    entityDefineDTO.getTableName(),
                    setJoiner,
                    primaryColumn.getFieldName()
            );
            return namedParameterJdbcTemplate.update(sql, paramSource) >= 1;
        } else {
            String sql = String.format("DELETE FROM %s WHERE %s IN (:primaryKeys)",
                    entityDefineDTO.getTableName(),
                    primaryColumn.getFieldName()
            );
            return namedParameterJdbcTemplate.update(sql, paramSource) >= 1;
        }
    }

    @Override
    public Collection<Column> getColumns(String className) {
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        String schema = databaseMetaService.getCurrentSchema();
        return databaseMetaService.getColumns(className, schema, entityDefineDTO.getTableName());
    }

    @Override
    public void syncColumnsToDb(String className) {
        EntityDefineDTO entityDefineDTO = entityDefineUtil.getEntityDefine(className);
        List<Column> columnDefList = fieldDefineUtil.getColumns(entityDefineDTO.getCode(), false);
        String schema = databaseMetaService.getCurrentSchema();
        databaseMetaService.syncColumnsToDb(columnDefList, schema, entityDefineDTO.getTableName());
    }

    @Override
    public Collection<Column> getColumnsByTableName(String tableName) {
        String schema = databaseMetaService.getCurrentSchema();
        return databaseMetaService.getColumns(null, schema, tableName);
    }

    // ======================== 私有通用查询与数据映射 ========================

    private List<FormatMap> queryListInternal(String selectSql, Object[] params, Map<String, Column> columnMap) {
        Set<Column> externalKeys = columnMap.values().stream()
                .filter(f -> f.getExternalClass() != null && Boolean.TRUE.equals(f.getExternalClassAutoFill()))
                .collect(Collectors.toSet());

        List<FormatMap> list = jdbcTemplate.query(selectSql, params, (rs, rowNum) -> mapRowToFormatMap(rs, columnMap));

        if (!externalKeys.isEmpty() && CollectionUtils.isNotEmpty(list)) {
            entityServiceFactory.updateExternalEntity(externalKeys, (List) list);
        }
        return list;
    }

    private FormatMap mapRowToFormatMap(ResultSet rs, Map<String, Column> columnMap) throws SQLException {
        FormatMap map = new FormatMap();
        ResultSetMetaData meta = rs.getMetaData();
        int count = meta.getColumnCount();

        for (int i = 1; i <= count; i++) {
            String key = meta.getColumnLabel(i);
            Column colDef = columnMap.get(key);
            if (colDef != null) {
                if (colDef.getSerialPattern() != null) {
                    map.rule(key, colDef.getSerialPattern());
                }
                Object value = rs.getObject(i);
                map.put(key, convertToEntityValue(value, colDef));
            } else {
                map.put(key, rs.getObject(i));
            }
        }
        return map;
    }

    private Map<Object, Map<String, Object>> getEntitiesByIds(EntityDefineDTO entityDefineDTO, List<Object> ids, Map<String, Column> columnMap, Column primaryColumn) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("ids", ids);

        String sql = String.format("SELECT %s FROM %s WHERE %s IN (:ids)",
                getSelectSql(columnMap),
                entityDefineDTO.getTableName(),
                primaryColumn.getFieldName()
        );

        List<FormatMap> list = namedParameterJdbcTemplate.query(sql, paramSource, (rs, rowNum) -> mapRowToFormatMap(rs, columnMap));
        Map<Object, Map<String, Object>> resultMap = new HashMap<>();
        for (FormatMap map : list) {
            resultMap.put(map.get(primaryColumn.getName()), map);
        }
        return resultMap;
    }

    private Map<String, Object> getByPrimaryKey(EntityDefineDTO entityDefineDTO, Object primaryKeyValue, Map<String, Column> columnMap, Column primaryKey) {
        Set<Column> externalKeys = columnMap.values().stream()
                .filter(f -> f.getExternalClass() != null && Boolean.TRUE.equals(f.getExternalClassAutoFill()))
                .collect(Collectors.toSet());

        StringBuilder sql = new StringBuilder(String.format("SELECT %s FROM %s WHERE %s=?",
                getSelectSql(columnMap), entityDefineDTO.getTableName(), primaryKey.getFieldName()));

        List<Object> args = Lists.newArrayList();
        args.add(convertToDbValue(primaryKeyValue, primaryKey));

        // 修复：补齐前置空格
        if (SqlUtil.isLogicDeleteEntity(columnMap)) {
            Column isDeletedCol = columnMap.get("isDeleted");
            String isDeletedFieldName = isDeletedCol != null ? isDeletedCol.getFieldName() : "IS_DELETED";
            sql.append(String.format(" AND %s=?", isDeletedFieldName));
            args.add(convertToDbValue(Delete.NORMAL, isDeletedCol));
        }

        try {
            FormatMap formatMap = jdbcTemplate.queryForObject(sql.toString(), (rs, rowNum) -> mapRowToFormatMap(rs, columnMap), args.toArray());
            if (formatMap != null && !externalKeys.isEmpty()) {
                entityServiceFactory.updateExternalEntity(externalKeys, List.of(formatMap));
            }
            return formatMap;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // ======================== 通用 Insert & Update ========================

    private void insert(EntityDefineDTO entityDefineDTO, Map<String, Object> entity, Map<String, Column> columnMap, Column primaryColumn) {
        Set<String> columns = entity.keySet();
        if (columns.isEmpty()) return;

        StringJoiner colJoiner = new StringJoiner(",");
        StringJoiner valJoiner = new StringJoiner(",");
        List<Object> params = new ArrayList<>();

        for (String col : columns) {
            Column column = columnMap.get(col);
            if (column == null || StringUtils.isEmpty(column.getFieldName())) continue;
            if (SqlUtil.isAuditEntityProps(col)) continue;
            if (RelationType.DETAIL_TABLE.equals(column.getRelationType())) continue;

            colJoiner.add(column.getFieldName());
            valJoiner.add("?");
            params.add(convertToDbValue(entity.get(col), column));
        }

        // 审计字段填充
        UserInfo userInfo = TokenUtil.getUserinfo();
        if (userInfo != null) {
            if (columnMap.containsKey("creatorId")) {
                colJoiner.add(columnMap.get("creatorId").getFieldName());
                valJoiner.add("?");
                params.add(convertToDbValue(userInfo.getUserId(), columnMap.get("creatorId")));
            }
            if (columnMap.containsKey("createdBy")) {
                colJoiner.add(columnMap.get("createdBy").getFieldName());
                valJoiner.add("?");
                params.add(convertToDbValue(userInfo.getName(), columnMap.get("createdBy")));
            }
        }
        if (columnMap.containsKey("createdTime")) {
            colJoiner.add(columnMap.get("createdTime").getFieldName());
            valJoiner.add("?");
            params.add(convertToDbValue(LocalDateTime.now(), columnMap.get("createdTime")));
        }

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)",
                entityDefineDTO.getTableName(),
                colJoiner,
                valJoiner
        );

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            return ps;
        }, keyHolder);

        // 修复：根据动态主键名回填主键
        if (keyHolder.getKey() != null) {
            entity.put(primaryColumn.getName(), keyHolder.getKey());
        } else if (MapUtils.isNotEmpty(keyHolder.getKeys())) {
            Object idVal = keyHolder.getKeys().get(primaryColumn.getFieldName());
            if (idVal == null) {
                idVal = keyHolder.getKeys().get(primaryColumn.getName());
            }
            if (idVal != null) {
                entity.put(primaryColumn.getName(), idVal);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    protected int batchInsert(EntityDefineDTO entityDefineDTO, List<Map<String, Object>> entityList, Map<String, Column> columnMap) {
        if (CollectionUtil.isEmpty(entityList)) return 0;

        // 计算所有实体的属性 Key 并集，避免异构 Map 丢失字段
        Set<String> allKeys = new LinkedHashSet<>();
        for (Map<String, Object> entity : entityList) {
            allKeys.addAll(entity.keySet());
        }
        if (allKeys.isEmpty()) return 0;

        StringJoiner colJoiner = new StringJoiner(",");
        List<String> fieldList = new ArrayList<>();

        for (String col : allKeys) {
            Column column = columnMap.get(col);
            if (column == null || StringUtils.isEmpty(column.getFieldName())) continue;
            if (SqlUtil.isAuditEntityProps(col)) continue;
            if (RelationType.DETAIL_TABLE.equals(column.getRelationType())) continue;
            fieldList.add(col);
            colJoiner.add(column.getFieldName());
        }

        UserInfo userInfo = TokenUtil.getUserinfo();
        if (userInfo != null) {
            if (columnMap.containsKey("creatorId")) {
                fieldList.add("creatorId");
                colJoiner.add(columnMap.get("creatorId").getFieldName());
            }
            if (columnMap.containsKey("createdBy")) {
                fieldList.add("createdBy");
                colJoiner.add(columnMap.get("createdBy").getFieldName());
            }
        }
        if (columnMap.containsKey("createdTime")) {
            fieldList.add("createdTime");
            colJoiner.add(columnMap.get("createdTime").getFieldName());
        }

        List<Object> totalParams = new ArrayList<>();
        StringJoiner valSingleJoiner = new StringJoiner(",");
        for (int i = 0; i < fieldList.size(); i++) {
            valSingleJoiner.add("?");
        }
        String valuesPlaceholder = "(" + valSingleJoiner + ")";

        StringJoiner allValues = new StringJoiner(",");
        for (Map<String, Object> entity : entityList) {
            allValues.add(valuesPlaceholder);

            for (String field : fieldList) {
                Column column = columnMap.get(field);
                Object value;
                if ("creatorId".equals(field)) {
                    value = userInfo != null ? userInfo.getUserId() : null;
                } else if ("createdBy".equals(field)) {
                    value = userInfo != null ? userInfo.getName() : null;
                } else if ("createdTime".equals(field)) {
                    value = LocalDateTime.now();
                } else {
                    value = entity.get(field);
                }
                totalParams.add(convertToDbValue(value, column));
            }
        }

        String sql = String.format("INSERT INTO %s (%s) VALUES %s",
                entityDefineDTO.getTableName(),
                colJoiner,
                allValues
        );

        return jdbcTemplate.update(sql, totalParams.toArray());
    }

    private void update(EntityDefineDTO entityDefineDTO, Map<String, Object> entity, Map<String, Column> columnMap, Column primaryColumn) {
        Set<String> columns = entity.keySet();
        if (columns.isEmpty()) return;

        StringJoiner setJoiner = new StringJoiner(",");
        List<Object> params = new ArrayList<>();

        for (String col : columns) {
            if (SqlUtil.isAuditEntityProps(col)) continue;
            Column column = columnMap.get(col);
            if (column == null || StringUtils.isEmpty(column.getFieldName())) continue;
            if (RelationType.DETAIL_TABLE.equals(column.getRelationType())) continue;
            if (column.getName().equals(primaryColumn.getName())) continue; // 主键不放入 SET 子句

            setJoiner.add(column.getFieldName() + "=?");
            params.add(convertToDbValue(entity.get(col), column));
        }

        // 审计字段
        UserInfo userInfo = TokenUtil.getUserinfo();
        if (userInfo != null) {
            if (columnMap.containsKey("updaterId")) {
                setJoiner.add(columnMap.get("updaterId").getFieldName() + "=?");
                params.add(userInfo.getUserId());
            }
            if (columnMap.containsKey("updatedBy")) {
                setJoiner.add(columnMap.get("updatedBy").getFieldName() + "=?");
                params.add(StringUtils.deleteWhitespace(userInfo.getName()));
            }
        }
        if (columnMap.containsKey("updatedTime")) {
            setJoiner.add(columnMap.get("updatedTime").getFieldName() + "=?");
            params.add(convertToDbValue(LocalDateTime.now(), columnMap.get("updatedTime")));
        }

        // 乐观锁：动态列名
        boolean hasVersion = columnMap.containsKey("version") && entity.get("version") != null;
        if (hasVersion) {
            String versionColName = columnMap.get("version").getFieldName();
            setJoiner.add(String.format("%s=%s + 1", versionColName, versionColName));
        }

        params.add(convertToDbValue(entity.get(primaryColumn.getName()), primaryColumn));
        String sql;
        if (hasVersion) {
            params.add(convertToDbValue(entity.get("version"), columnMap.get("version")));
            sql = String.format("UPDATE %s SET %s WHERE %s=? AND %s=?",
                    entityDefineDTO.getTableName(),
                    setJoiner,
                    primaryColumn.getFieldName(),
                    columnMap.get("version").getFieldName()
            );
        } else {
            sql = String.format("UPDATE %s SET %s WHERE %s=?",
                    entityDefineDTO.getTableName(),
                    setJoiner,
                    primaryColumn.getFieldName()
            );
        }

        int rows = jdbcTemplate.update(sql, params.toArray());
        if (rows < 1) {
            throw new BizException(I18nUtil.getMessage("saveRecordFailed"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    protected void batchUpdate(EntityDefineDTO entityDefineDTO, List<Map<String, Object>> entityList, Map<String, Column> columnMap, Column primaryColumn) {
        if (CollectionUtil.isEmpty(entityList)) return;

        Map<String, Object> firstEntity = entityList.get(0);
        Set<String> columns = firstEntity.keySet();
        if (columns.isEmpty()) return;

        StringJoiner setJoiner = new StringJoiner(",");
        List<String> validColumns = new ArrayList<>();

        for (String col : columns) {
            if (SqlUtil.isAuditEntityProps(col)) continue;
            Column column = columnMap.get(col);
            if (column == null || StringUtils.isEmpty(column.getFieldName())) continue;
            if (RelationType.DETAIL_TABLE.equals(column.getRelationType())) continue;
            if (column.getName().equals(primaryColumn.getName())) continue;

            setJoiner.add(column.getFieldName() + "=?");
            validColumns.add(col);
        }

        UserInfo userInfo = TokenUtil.getUserinfo();
        boolean hasUpdaterId = columnMap.containsKey("updaterId") && userInfo != null;
        boolean hasUpdatedBy = columnMap.containsKey("updatedBy") && userInfo != null;
        boolean hasUpdatedTime = columnMap.containsKey("updatedTime");

        if (hasUpdaterId) {
            setJoiner.add(columnMap.get("updaterId").getFieldName() + "=?");
        }
        if (hasUpdatedBy) {
            setJoiner.add(columnMap.get("updatedBy").getFieldName() + "=?");
        }
        if (hasUpdatedTime) {
            setJoiner.add(columnMap.get("updatedTime").getFieldName() + "=?");
        }

        boolean hasVersion = columnMap.containsKey("version");
        if (hasVersion) {
            String versionColName = columnMap.get("version").getFieldName();
            setJoiner.add(String.format("%s=%s + 1", versionColName, versionColName));
        }

        String sql;
        if (hasVersion) {
            sql = String.format("UPDATE %s SET %s WHERE %s=? AND %s=?",
                    entityDefineDTO.getTableName(),
                    setJoiner,
                    primaryColumn.getFieldName(),
                    columnMap.get("version").getFieldName()
            );
        } else {
            sql = String.format("UPDATE %s SET %s WHERE %s=?",
                    entityDefineDTO.getTableName(),
                    setJoiner,
                    primaryColumn.getFieldName()
            );
        }

        List<Object[]> batchParams = new ArrayList<>();
        for (Map<String, Object> entity : entityList) {
            List<Object> params = new ArrayList<>();

            for (String col : validColumns) {
                Column column = columnMap.get(col);
                params.add(convertToDbValue(entity.get(col), column));
            }

            if (hasUpdaterId) {
                params.add(userInfo.getUserId());
            }
            if (hasUpdatedBy) {
                params.add(StringUtils.deleteWhitespace(userInfo.getName()));
            }
            if (hasUpdatedTime) {
                params.add(convertToDbValue(LocalDateTime.now(), columnMap.get("updatedTime")));
            }

            params.add(convertToDbValue(entity.get(primaryColumn.getName()), primaryColumn));
            if (hasVersion) {
                params.add(convertToDbValue(entity.get("version"), columnMap.get("version")));
            }
            batchParams.add(params.toArray());
        }

        int[] updateCounts = jdbcTemplate.batchUpdate(sql, batchParams);
        for (int count : updateCounts) {
            if (count < 1) {
                throw new BizException(I18nUtil.getMessage("saveRecordFailed"));
            }
        }
    }

    // ======================== 辅助与 SQL 构建方法 ========================

    private HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    private String buildLimitSql(Map<String, Object> args) {
        int size = MapUtils.getIntValue(args, "size", 20);
        int current = MapUtils.getIntValue(args, "current", 1);
        return String.format("LIMIT %s OFFSET %s", size, (current - 1) * size);
    }

    private String buildLimitSql() {
        Page page = RequestUtil.getPage();
        return String.format("LIMIT %s OFFSET %s", page.getSize(), (page.getCurrent() - 1) * page.getSize());
    }
    private String buildOrderBySqlByRequest(Map<String, Column> columnMap, Map<String, String[]> multiMap) {
        if (MapUtils.isEmpty(multiMap)) {
            return "";
        }
        StringJoiner sql = new StringJoiner(",");

        String[] descColumns = multiMap.get("descColumns");
        if (descColumns != null && descColumns.length > 0) {
            for (String col : descColumns) {
                if (columnMap.containsKey(col) && StringUtils.isNotEmpty(columnMap.get(col).getFieldName())) {
                    String colName = columnMap.get(col).getFieldName();
                    if (SqlInjectionUtils.check(colName)) {
                        throw new BizException("Discovering SQL injection column: " + colName);
                    }
                    sql.add(colName + " DESC");
                }
            }
        }

        String[] ascColumns = multiMap.get("ascColumns");
        if (ascColumns != null && ascColumns.length > 0) {
            for (String col : ascColumns) {
                if (columnMap.containsKey(col) && StringUtils.isNotEmpty(columnMap.get(col).getFieldName())) {
                    String colName = columnMap.get(col).getFieldName();
                    if (SqlInjectionUtils.check(colName)) {
                        throw new BizException("Discovering SQL injection column: " + colName);
                    }
                    sql.add(colName + " ASC");
                }
            }
        }
        return sql.length() > 0 ? "ORDER BY " + sql : "";
    }

    private String buildOrderBySql(Map<String, Column> columnMap, Map<String, Object> args) {
        StringJoiner sql = new StringJoiner(",");

        List descColumns = (List) args.get("descColumns");
        if (!CollectionUtils.isEmpty(descColumns)) {
            for (Object col : descColumns) {
                String key = String.valueOf(col);
                if (columnMap.containsKey(key) && StringUtils.isNotEmpty(columnMap.get(key).getFieldName())) {
                    String colName = columnMap.get(key).getFieldName();
                    if (SqlInjectionUtils.check(colName)) {
                        throw new BizException("Discovering SQL injection column: " + colName);
                    }
                    sql.add(colName + " DESC");
                }
            }
        }

        List ascColumns = (List) args.get("ascColumns");
        if (!CollectionUtils.isEmpty(ascColumns)) {
            for (Object col : ascColumns) {
                String key = String.valueOf(col);
                if (columnMap.containsKey(key) && StringUtils.isNotEmpty(columnMap.get(key).getFieldName())) {
                    String colName = columnMap.get(key).getFieldName();
                    if (SqlInjectionUtils.check(colName)) {
                        throw new BizException("Discovering SQL injection column: " + colName);
                    }
                    sql.add(colName + " ASC");
                }
            }
        }
        return sql.length() > 0 ? "ORDER BY " + sql : "";
    }



    private String getSelectSql(Map<String, Column> columnMap) {
        StringJoiner selJoiner = new StringJoiner(",");
        for (Column column : columnMap.values()) {
            if (RelationType.DETAIL_TABLE.equals(column.getRelationType())) {
                continue;
            }
            if (StringUtils.isEmpty(column.getFieldName())) {
                continue;
            }
            selJoiner.add(String.format("%s AS \"%s\"", column.getFieldName(), column.getName()));
        }
        return selJoiner.toString();
    }
}