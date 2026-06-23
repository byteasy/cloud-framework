package com.byteasy.component;

import com.byteasy.api.IEntityDefineService;
import com.byteasy.api.ISchemaDefineService;
import com.byteasy.dto.Column;
import com.byteasy.dto.EntityDefineDTO;
import com.byteasy.event.EntityDefineChangeEvent;
import com.byteasy.event.IPlatformEventHandler;
import com.byteasy.util.ColumnUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@Log4j2
public class SchemaDefineUtil implements IPlatformEventHandler<EntityDefineChangeEvent> {

    @Autowired
    private Environment env;
    @Autowired
    private ISchemaDefineService schemaDefineService;
    @Autowired
    private IEntityDefineService entityDefineService;

    private LoadingCache<String, List<Column>> columnsCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(6)).maximumSize(5000).build(new CacheLoader<>() {
        @Override
        public List<Column> load(String key) {
            try {
                List<Column> list = schemaDefineService.getSchemaMetaInfo(key);
                if (!list.isEmpty()) {
                    return list;
                }
            } catch (Exception e1) {
                log.error(e1);
            }
            /**
             * 其次从源代码定义加载
             */
            try {
                Class<?> cls = Class.forName(key);
                return ColumnUtil.getColumns(cls).values().stream().sorted(Comparator.comparingInt(Column::getShowOrder)).toList();
            } catch (Exception e) {
                log.error("getSchemaMetaInfo: " + key, e);
            }
            return List.of();
        }
    });

    private LoadingCache<String, EntityDefineDTO> entityDefineCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(6)).maximumSize(5000).build(new CacheLoader<>() {
        @Override
        public EntityDefineDTO load(String key) {
            return entityDefineService.getEntityDefine(key);
        }
    });

    public List<Column> getColumns(String className, boolean skipRpc) {
        if (!skipRpc) {
            /**
             * 1. 优先从系统中的手工配置加载
             */
            try {
                List<Column> list = env.matchesProfiles("dev") ? schemaDefineService.getSchemaMetaInfo(className) : columnsCacheLoader.get(className);
                if (!list.isEmpty()) {
                    return list;
                }
            } catch (Exception e1) {
                log.error(e1);
            }
        }

        /**
         * 其次从源代码定义加载
         */
        try {
            Class<?> cls = Class.forName(className);
            return ColumnUtil.getColumns(cls).values().stream().sorted(Comparator.comparingInt(Column::getShowOrder)).toList();
        } catch (Exception e) {
            log.error("getSchemaMetaInfo: " + className, e);
        }
        return null;
    }

    public String getEntityName(String className) {
        EntityDefineDTO entityDefine = null;
        try {
            entityDefine = entityDefineCacheLoader.get(className);
        } catch (ExecutionException e) {
            log.error(e);
        }
        if (entityDefine != null) {
            return entityDefine.getName();
        }
        return null;
    }

    public EntityDefineDTO getEntityDefine(String className) {
        EntityDefineDTO entityDefine = null;
        try {
            entityDefine = entityDefineCacheLoader.get(className);
        } catch (ExecutionException e) {
            log.error(e);
        }
        return entityDefine;
    }

    @Override
    public void handle(EntityDefineChangeEvent event) {
        entityDefineCacheLoader.invalidate(event.getSource().getClassName());
        columnsCacheLoader.invalidate(event.getSource().getClassName());
        log.info("EntityDefine and Column Meta cache invalidate, className: {}", event.getSource().getClassName());
    }

    @Override
    public Class<EntityDefineChangeEvent> getEventType() {
        return EntityDefineChangeEvent.class;
    }
}
