package com.byteasy.common.component;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.byteasy.event.EntityChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.BatchExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class EntityChangeInterceptor implements Interceptor {

    /**
     * 防重入标识，防止当前拦截器内部执行 queryBatchByIds 时再次被拦截
     */
    private static final ThreadLocal<Boolean> REENTRY_GUARD = ThreadLocal.withInitial(() -> Boolean.FALSE);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (Boolean.TRUE.equals(REENTRY_GUARD.get())) {
            return invocation.proceed();
        }

        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        SqlCommandType sqlCommandType = ms.getSqlCommandType();

        // 只拦截 INSERT, UPDATE, DELETE
        if (sqlCommandType != SqlCommandType.INSERT &&
                sqlCommandType != SqlCommandType.UPDATE &&
                sqlCommandType != SqlCommandType.DELETE) {
            return invocation.proceed();
        }

        Executor executor = (Executor) invocation.getTarget();
        Class<?> entityClass = EntityResolver.resolveEntityClass(ms.getId());
        EntityParamParser.ParseResult parseResult = EntityParamParser.parse(parameter, entityClass);

        // 如果解析不到对应的实体信息，直接放行
        if (parseResult.getEntityClass() == null) {
            return invocation.proceed();
        }

        List<Object> beforeEntities = Collections.emptyList();

        // 1. 执行前状态抓取（仅针对 UPDATE / DELETE 且持有主键 ID 的情况）
        if ((sqlCommandType == SqlCommandType.DELETE || sqlCommandType == SqlCommandType.UPDATE)
                && !parseResult.getIds().isEmpty()
                && parseResult.getTableInfo() != null) {
            try {
                REENTRY_GUARD.set(Boolean.TRUE);
                beforeEntities = queryBatchByIds(executor, parseResult.getTableInfo(), parseResult.getIds());
            } finally {
                REENTRY_GUARD.remove();
            }
        }

        // 2. 执行原始数据库操作
        Object result = invocation.proceed();

        // 3. 判断是否执行成功
        int rows = (result instanceof Integer) ? (Integer) result : 0;
        if (!isSuccess(rows)) {
            return result;
        }

        // 4. 构建并发布变更事件
        try {
            EntityChangeEvent event = buildEvent(sqlCommandType, parseResult, beforeEntities);
            // 发布 Spring 事件（可在 Listener 中去 evict Infinispan 缓存或发送 MQ）
            // TODO
//            eventPublisher.publishEvent(event);
        } catch (Exception e) {
            log.error("Failed to publish EntityChangeEvent for statement: {}", ms.getId(), e);
        }

        return result;
    }

    /**
     * 判断受影响行数是否有效：
     * 1. 普通操作：rows > 0
     * 2. IService 批处理 (BatchExecutor)：rows == BATCH_UPDATE_RETURN_VALUE (-2147482646)
     */
    private boolean isSuccess(int rows) {
        return rows > 0 || rows == BatchExecutor.BATCH_UPDATE_RETURN_VALUE;
    }

    /**
     * 批量查询变更前的数据 (通过 MP 注入的 selectBatchIds 实现单条 IN 查询)
     */
    @SuppressWarnings("unchecked")
    private List<Object> queryBatchByIds(Executor executor, TableInfo tableInfo, List<Serializable> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            String statementId = tableInfo.getCurrentNamespace() + ".selectBatchIds";
            Configuration configuration = tableInfo.getConfiguration();
            if (!configuration.hasStatement(statementId)) {
                return Collections.emptyList();
            }
            MappedStatement ms = configuration.getMappedStatement(statementId);
            return executor.query(ms, ids, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER);
        } catch (Exception e) {
            log.warn("Query beforeEntities failed, table: {}, ids: {}", tableInfo.getTableName(), ids, e);
            return Collections.emptyList();
        }
    }

    private EntityChangeEvent buildEvent(SqlCommandType sqlCommandType,
                                         EntityParamParser.ParseResult parseResult,
                                         List<Object> beforeEntities) {
        EntityChangeEvent.ChangeType changeType;
        switch (sqlCommandType) {
            case INSERT:
                changeType = EntityChangeEvent.ChangeType.INSERT;
                break;
            case UPDATE:
                changeType = EntityChangeEvent.ChangeType.UPDATE;
                break;
            case DELETE:
                changeType = EntityChangeEvent.ChangeType.DELETE;
                break;
            default:
                throw new IllegalArgumentException("Unsupported sqlCommandType: " + sqlCommandType);
        }

        return EntityChangeEvent.builder()
                .changeType(changeType)
                .entityClass(parseResult.getEntityClass())
                .ids(parseResult.getIds())
                .beforeEntities(beforeEntities)
                .afterEntities(parseResult.getEntities())
                .wrapperOperation(parseResult.isWrapperOperation())
                .build();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 配置属性扩展
    }
}