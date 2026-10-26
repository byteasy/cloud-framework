package com.byteasy.permission.util;

import com.byteasy.annotations.Openapi;
import org.apache.commons.lang3.StringUtils;

public class OpenapiUtil {

    public static String getEntityName(Openapi openapi) {
        if (openapi == null) {
            return null;
        }
        if (!Void.class.equals(openapi.entity())) {
            return openapi.entity().getName();
        }
        if (StringUtils.isNotEmpty(openapi.entityName())) {
            return openapi.entityName();
        }
        return null;
    }
}
