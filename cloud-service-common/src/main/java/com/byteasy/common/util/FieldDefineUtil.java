package com.byteasy.common.util;

import cn.hutool.core.util.ReflectUtil;
import com.byteasy.enums.YesNo;
import com.byteasy.platform.entity.FieldDefine;
import com.byteasy.util.I18nUtil;

import java.util.List;
import java.util.Map;

public class FieldDefineUtil {

    public static <T> void encodeToI18nFormat(List entities, List<FieldDefine> columns) {
        for (FieldDefine column : columns) {
            if (YesNo.YES.equals(column.getIsI18n())) {
                for (Object entity : entities) {
                    if (entity instanceof Map) {
                        Object raw = ((Map) entity).get(column.getName());
                        ((Map) entity).put(column.getName(), I18nUtil.genLanguageValue(String.valueOf(raw)));
                    } else {
                        Object raw = ReflectUtil.getFieldValue(entity, column.getName());
                        ReflectUtil.setFieldValue(entity, column.getName(), I18nUtil.genLanguageValue(String.valueOf(raw)));
                    }
                }
            }
        }
    }
}
