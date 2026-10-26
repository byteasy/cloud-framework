package com.byteasy.common.component;

import cn.hutool.core.util.ArrayUtil;
import com.byteasy.component.ICacheLoader;
import com.byteasy.platform.api.FieldDefineClient;
import com.byteasy.platform.entity.FieldDefine;
import com.byteasy.platform.entity.FieldGroup;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class FieldDefineLoadingCache implements ICacheLoader<String, List<FieldDefine>> {

    @Autowired
    private FieldDefineClient fieldDefineService;


    @Override
    @Cacheable(cacheNames = "dict", key = "'FieldDefine:' + #className")
    public List<FieldDefine> load(String className) {
        List<FieldDefine> list = fieldDefineService.listEntityFieldDefines(className).getIfPresent();
        if (list != null && !list.isEmpty()) {
            return list;
        }
        return List.of();
    }

    @Override
    public String type() {
        return ArrayUtil.join(new String[]{FieldDefine.class.getName(), FieldGroup.class.getName()}, ",");
    }
}
