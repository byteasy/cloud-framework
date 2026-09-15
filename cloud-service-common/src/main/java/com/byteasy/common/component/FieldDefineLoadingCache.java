package com.byteasy.common.component;

import cn.hutool.core.util.ArrayUtil;
import com.byteasy.Constant;
import com.byteasy.common.util.ColumnUtil;
import com.byteasy.component.ICacheLoader;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IPlatformEntityChangeHandler;
import com.byteasy.platform.api.IFieldDefineService;
import com.byteasy.platform.entity.FieldDefine;
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
public class FieldDefineLoadingCache implements IPlatformEntityChangeHandler, ICacheLoader<String, List<FieldDefine>> {

    @Autowired
    private IFieldDefineService fieldDefineService;

    private LoadingCache<String, List<FieldDefine>> columnsCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(6)).maximumSize(5000).build(new CacheLoader<>() {
        @Override
        public List<FieldDefine> load(String key) {
            try {
                List<FieldDefine> list = fieldDefineService.listEntityFieldDefines(key).getIfPresent();
                if (list != null && !list.isEmpty()) {
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
                return ColumnUtil.getColumns(cls).values().stream().sorted(Comparator.comparingInt(FieldDefine::getShowOrder)).toList();
            } catch (Exception e) {
                log.error("getSchemaMetaInfo: " + key, e);
            }
            return List.of();
        }
    });


    public List<FieldDefine> getColumns(String className, boolean skipRpc) {
        if (!skipRpc) {
            /**
             * 1. 优先从系统中的手工配置加载
             */
            try {
                List<FieldDefine> list = columnsCacheLoader.get(className);
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
            return ColumnUtil.getColumns(cls).values().stream().sorted(Comparator.comparingInt(FieldDefine::getShowOrder)).toList();
        } catch (Exception e) {
            log.error("getSchemaMetaInfo: " + className, e);
        }
        return null;
    }


    @Override
    public List<FieldDefine> load(String key) {
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
