package com.byteasy.common.component;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import com.byteasy.component.EntityChangeHandlerFactory;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.platform.entity.EntityDefine;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.Serializable;
import java.util.*;

/**
 * Spring 动态代码增强切面：全面拦截 IRepository 及其实现类（AbstractRepository / CrudRepository）的所有变更操作
 */
@Slf4j
@Aspect
@Component
@Order(1)
@RequiredArgsConstructor
public class RepositoryMutationAspect {


    @Autowired
    private EntityChangeHandlerFactory entityChangeHandlerFactory;
    @Autowired
    private EntityDefineLoadingCache entityDefineLoadingCache;

    /**
     * 防重入线程标记：防止 saveOrUpdateBatch / saveOrUpdate 内部自调用 save 或 updateById 时触发多余重复事件
     */
    private static final ThreadLocal<Boolean> REENTRANT_GUARD = ThreadLocal.withInitial(() -> Boolean.FALSE);

    /**
     * 切入点：拦截目标为 IRepository 及其所有子类（AbstractRepository、CrudRepository）的增删改方法
     */
    @Pointcut("target(com.baomidou.mybatisplus.extension.repository.IRepository) && " +
            "(execution(* save*(..)) || execution(* update*(..)) || execution(* remove*(..)))")
    public void repositoryMutationPointcut() {
    }

    @Around("repositoryMutationPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 防嵌套重入检查
        if (Boolean.TRUE.equals(REENTRANT_GUARD.get())) {
            return joinPoint.proceed();
        }

        REENTRANT_GUARD.set(Boolean.TRUE);
        try {
            IRepository<?> repository = (IRepository<?>) joinPoint.getTarget();
            Class<?> entityClass = repository.getEntityClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
            String methodName = joinPoint.getSignature().getName();
            Object[] args = joinPoint.getArgs();

            // 2. 执行前置提取（保存变更前状态、判断 saveOrUpdate 真实意图等）
            PreContext preContext = null;
            EntityDefine entityDefine = entityDefineLoadingCache.load(entityClass.getName());
            if (entityDefine == null) {
                log.warn("className: {} is not define", entityClass.getName());
                return joinPoint.proceed();
            } else {
                if (entityDefine.getTrackable() != null && entityDefine.getTrackable()) {
                    preContext = preparePreContext(repository, entityClass, tableInfo, methodName, args);
                }

                // 3. 执行目标持久化操作
                Object result = joinPoint.proceed();

                // 若数据库操作返回 false，代表未发生实际变更，跳过事件发布
                if (result instanceof Boolean && !((Boolean) result)) {
                    return result;
                }

                if (entityDefine.getTrackable() != null && entityDefine.getTrackable()) {
                    // 4. 执行后置处理并组装 EntityChangeEvent（获取自增主键、变更后实体等）
                    EntityChangeEvent event = buildEvent(entityClass, methodName, args, preContext);
                    if (event != null) {
                        publishEventSafely(event);
                    }
                }
                return result;
            }
        } finally {
            MutationContextHolder.clear();
            REENTRANT_GUARD.remove();
        }
    }

    /**
     * 前置状态准备与快照捕获
     */
    @SuppressWarnings("unchecked")
    private PreContext preparePreContext(IRepository<?> repo, Class<?> entityClass, TableInfo tableInfo, String methodName, Object[] args) {
        PreContext ctx = new PreContext();
        if (args == null || args.length == 0) {
            return ctx;
        }
        // ------------------ A. 删除操作前置分析 ------------------
        if (methodName.startsWith("remove")) {
            // removeById(Serializable id) / removeById(Serializable id, boolean useFill)
            if (args[0] instanceof Serializable && !entityClass.isInstance(args[0])) {
                Serializable id = (Serializable) args[0];
                Object before = repo.getById(id);
                if (before != null) ctx.beforeEntities.add(before);
            }
            // removeById(T entity)
            else if (entityClass.isInstance(args[0])) {
                Object entity = args[0];
                ctx.beforeEntities.add(entity);
            }
            // removeByIds(Collection<?> list) / removeByIds(Collection<?> list, boolean useFill)
            // 源码定义：list 可以是主键ID列表，也可以是实体列表！
            else if (args[0] instanceof Collection) {
                Collection<?> collection = (Collection<?>) args[0];
                Set<Serializable> ids = Sets.newHashSet();
                for (Object item : collection) {
                    if (item == null) continue;
                    if (entityClass.isInstance(item)) {
                        ids.add(extractId(item, tableInfo));
                        // 元素为实体
                    } else if (item instanceof Serializable) {
                        // 元素为主键 ID
                        ids.add((Serializable) item);
                    }
                }
                // 若需要变更前数据且集合全为主键，则按需批量拉取一次
                if (!ids.isEmpty()) {
                    ctx.beforeEntities.addAll(repo.listByIds(ids));
                }
            }
        } else if ("removeByMap".equals(methodName)) {
            Map<String, Object> columnMap = (Map<String, Object>) args[0];
            // 1. 执行前预查：通过 IRepository 自带的 listByMap 查出将被删除的旧实体
            // 利用 repository.listByMap(columnMap) 获取待删除的实体集合
            List<?> toDeleteList = repo.listByMap(columnMap);
            if (toDeleteList != null && !toDeleteList.isEmpty()) {
                ctx.beforeEntities.addAll(toDeleteList);
            }
        }
        // ------------------ B. saveOrUpdate 前置分析 ------------------
        else if ("saveOrUpdate".equals(methodName) && entityClass.isInstance(args[0])) {
            Object entity = args[0];
            Serializable id = extractId(entity, tableInfo);
            if (id != null) {
                // 判断数据库中是否已有该记录
                Object existing = repo.getById(id);
                if (existing != null) {
                    ctx.isUpdate = true;
                    ctx.beforeEntities.add(existing);
                } else {
                    ctx.isUpdate = false;
                }
            } else {
                // 没有主键，一定是 INSERT
                ctx.isUpdate = false;
            }
        }

        // ------------------ C. updateById 前置快照 (可选) ------------------
        else if ("updateById".equals(methodName) && entityClass.isInstance(args[0])) {
            Serializable id = extractId(args[0], tableInfo);
            if (id != null) {
                Object before = repo.getById(id);
                if (before != null) ctx.beforeEntities.add(before);
            }
        }
        return ctx;
    }

    /**
     * 后置构建完整的实体变更事件
     */
    @SuppressWarnings("unchecked")
    private EntityChangeEvent buildEvent(Class<?> entityClass, String methodName, Object[] args, PreContext ctx) {
        EntityChangeEvent.EntityChangeEventBuilder builder = EntityChangeEvent.builder()
                .entityClass(entityClass.getName());

        // ==================== 1. DELETE 操作 ====================
        if (methodName.startsWith("remove")) {
            builder.changeType(EntityChangeEvent.ChangeType.DELETE)
                    .beforeEntities(ctx.beforeEntities);

            // remove(Wrapper) 或 removeByMap
            if ("remove".equals(methodName) || "removeByMap".equals(methodName)) {
                builder.wrapperOperation(true);
            }
            return builder.build();
        }

        // ==================== 2. INSERT 操作 ====================
        // save(T entity)
        if ("save".equals(methodName) && entityClass.isInstance(args[0])) {
            Object entity = args[0];
            return builder.changeType(EntityChangeEvent.ChangeType.INSERT)
                    .afterEntities(Collections.singletonList(entity))
                    .build();
        }

        // saveBatch(Collection<T> entityList, int batchSize)
        if ("saveBatch".equals(methodName) && args[0] instanceof Collection) {
            Collection<?> entities = (Collection<?>) args[0];
            return builder.changeType(EntityChangeEvent.ChangeType.INSERT)
                    .afterEntities(new ArrayList<>(entities))
                    .build();
        }

        // ==================== 3. UPDATE 操作 ====================
        // updateById(T entity)
        if ("updateById".equals(methodName) && entityClass.isInstance(args[0])) {
            Object entity = args[0];
            return builder.changeType(EntityChangeEvent.ChangeType.UPDATE)
                    .beforeEntities(ctx.beforeEntities)
                    .afterEntities(Collections.singletonList(entity))
                    .build();
        }

        // updateBatchById(Collection<T> entityList, int batchSize)
        if ("updateBatchById".equals(methodName) && args[0] instanceof Collection) {
            Collection<?> entities = (Collection<?>) args[0];
            return builder.changeType(EntityChangeEvent.ChangeType.UPDATE)
                    .afterEntities(new ArrayList<>(entities))
                    .build();
        }

        // update(Wrapper) 或 update(T entity, Wrapper)
        if ("update".equals(methodName)) {
            builder.changeType(EntityChangeEvent.ChangeType.UPDATE)
                    .wrapperOperation(true);
            // update(T entity, Wrapper)
            if (args.length >= 1 && entityClass.isInstance(args[0])) {
                builder.afterEntities(Collections.singletonList(args[0]));
            }
            return builder.build();
        }

        // ==================== 4. SAVE OR UPDATE 混合操作 ====================
        // saveOrUpdate(T entity)
        if ("saveOrUpdate".equals(methodName) && entityClass.isInstance(args[0])) {
            Object entity = args[0];
            EntityChangeEvent.ChangeType type = ctx.isUpdate
                    ? EntityChangeEvent.ChangeType.UPDATE
                    : EntityChangeEvent.ChangeType.INSERT;
            return builder.changeType(type)
                    .beforeEntities(ctx.beforeEntities)
                    .afterEntities(Collections.singletonList(entity))
                    .build();
        }

        // saveOrUpdateBatch(Collection<T> entityList, int batchSize)
        if ("saveOrUpdateBatch".equals(methodName) && args[0] instanceof Collection) {
            Collection<?> entities = (Collection<?>) args[0];
            return builder.changeType(EntityChangeEvent.ChangeType.UPDATE)
                    .afterEntities(new ArrayList<>(entities))
                    .build();
        }

        log.error("RepositoryMutationAspect method: {} is not implement", methodName);
        return null;
    }

    /**
     * 从实体中提取主键属性值
     */
    private Serializable extractId(Object entity, TableInfo tableInfo) {
        if (entity == null || tableInfo == null || tableInfo.getKeyProperty() == null) {
            return null;
        }
        Object idVal = tableInfo.getPropertyValue(entity, tableInfo.getKeyProperty());
        return idVal instanceof Serializable ? (Serializable) idVal : null;
    }

    /**
     * 从实体集合中提取主键列表
     */
    private List<Serializable> extractIdsFromCollection(Collection<?> entities, TableInfo tableInfo) {
        if (entities == null || entities.isEmpty()) return Collections.emptyList();
        List<Serializable> ids = new ArrayList<>(entities.size());
        for (Object item : entities) {
            Serializable id = extractId(item, tableInfo);
            if (id != null) ids.add(id);
        }
        return ids;
    }

    /**
     * 事务安全发布：在 @Transactional 场景下，必须在当前事务真正 Commit 后发布
     * 避免发生并发脏读与回滚导致的“假失效”
     */
    private void publishEventSafely(EntityChangeEvent event) {
        if (MutationContextHolder.hasMutation()) {
            if (TransactionSynchronizationManager.isActualTransactionActive()) {
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        entityChangeHandlerFactory.handle(RepositoryMutationAspect.class.getName(), event);
                    }
                });
            } else {
                entityChangeHandlerFactory.handle(RepositoryMutationAspect.class.getName(), event);
            }
        }
    }

    /**
     * 临时上下文
     */
    private static class PreContext {
        List<Object> beforeEntities = new ArrayList<>();
        boolean isUpdate = false;
    }
}