package com.byteasy.common.component;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.byteasy.component.ICacheLoader;
import com.byteasy.event.EntityChangeEvent;
import com.byteasy.event.IEntityChangeHandler;
import com.byteasy.platform.api.IFieldDefineService;
import com.byteasy.platform.entity.FieldDefine;
import com.byteasy.platform.entity.FieldGroup;
import com.byteasy.util.FieldValueUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@Log4j2
public class FieldDefineLoadingCache implements IEntityChangeHandler, ICacheLoader<String, List<FieldDefine>> {

    @Autowired
    private IFieldDefineService fieldDefineService;

    private LoadingCache<String, List<FieldDefine>> columnsCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofHours(6)).maximumSize(5000).build(new CacheLoader<>() {
        @Override
        public List<FieldDefine> load(String key) {
            ServiceImpl service = EntityServiceFactory.getService(FieldDefine.class.getName());
            if (service != null) {
                LambdaUpdateWrapper<FieldDefine> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(FieldDefine::getClassName, key);
                lambdaUpdateWrapper.orderByAsc(FieldDefine::getShowOrder);
                return service.list(lambdaUpdateWrapper);
            } else {
                List<FieldDefine> list = fieldDefineService.listEntityFieldDefines(key).getIfPresent();
                if (list != null && !list.isEmpty()) {
                    return list;
                }
                return List.of();
            }
        }
    });


    @Override
    public List<FieldDefine> load(String key) {
        return columnsCacheLoader.getUnchecked(key);
    }

    @Override
    public String type() {
        return ArrayUtil.join(new String[]{FieldDefine.class.getName(), FieldGroup.class.getName()}, ",");
    }

    @Override
    public void handle(EntityChangeEvent event) {
        if (event.getClassName().equals(FieldGroup.class.getName())) {
            columnsCacheLoader.invalidateAll();
        } else {
            switch (event.getChangeType()) {
                case DELETE ->
                        columnsCacheLoader.invalidate(FieldValueUtil.getFieldValue(event.getBefore(), "className", String.class));
                case CREATE, UPDATE ->
                        columnsCacheLoader.invalidate(FieldValueUtil.getFieldValue(event.getAfter(), "className", String.class));
            }
        }
    }
}
