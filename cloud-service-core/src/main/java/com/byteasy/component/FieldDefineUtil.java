package com.byteasy.component;

import cn.hutool.core.util.ArrayUtil;
import com.byteasy.Constant;
import com.byteasy.api.IFieldDefineService;
import com.byteasy.dto.Column;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IPlatformEntityChangeHandler;
import com.byteasy.util.ColumnUtil;
import com.byteasy.util.FieldValueUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

@Component
@Log4j2
public class FieldDefineUtil implements IPlatformEntityChangeHandler, ICacheLoader<String, List<Column>> {

    @Autowired
    private IFieldDefineService fieldDefineService;

    private LoadingCache<String, List<Column>> columnsCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(6)).maximumSize(5000).build(new CacheLoader<>() {
        @Override
        public List<Column> load(String key) {
            try {
                List<Column> list = fieldDefineService.getSchemaMetaInfo(key);
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
                List<Column> list = columnsCacheLoader.get(className);
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
    public List<Column> load(String key) {
        return getColumns(key, false);
    }

    @Override
    public String type() {
        return ArrayUtil.join(new String[]{Constant.CLS_FIELD_DEFINE, Constant.CLS_FIELD_GROUP}, ",");
    }

    @Override
    public void handle(EntityChangeEvent event) {
        if (event.getSource().getClassName().equals(Constant.CLS_FIELD_GROUP)) {
            columnsCacheLoader.invalidateAll();
        } else {
            switch (event.getSource().getChangeType()) {
                case DELETE ->
                        columnsCacheLoader.invalidate(FieldValueUtil.getFieldValue(event.getSource().getBefore(), "className", String.class));
                case CREATE, UPDATE ->
                        columnsCacheLoader.invalidate(FieldValueUtil.getFieldValue(event.getSource().getAfter(), "className", String.class));
            }
        }
    }
}
