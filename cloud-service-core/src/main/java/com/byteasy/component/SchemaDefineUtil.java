package com.byteasy.component;

import com.byteasy.api.IEntityDefineService;
import com.byteasy.api.ISchemaDefineService;
import com.byteasy.dto.Column;
import com.byteasy.dto.EntityDefineDTO;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.EntityChangeSource;
import com.byteasy.event.IPlatformEntityChangeHandler;
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
public class SchemaDefineUtil implements IPlatformEntityChangeHandler {

    @Autowired
    private Environment env;
    @Autowired
    private ISchemaDefineService schemaDefineService;

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


    @Override
    public String className() {
        return "com.byteasy.platform.entity.SchemaDefine";
    }

    @Override
    public void handle(EntityChangeEvent event) {
        columnsCacheLoader.invalidate(event.getSource().getClassName());
    }
}
