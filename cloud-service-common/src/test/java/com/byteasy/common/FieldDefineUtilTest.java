package com.byteasy.common;


import com.byteasy.common.util.FieldDefineUtil;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.enums.YesNo;
import com.byteasy.platform.entity.FieldDefine;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class FieldDefineUtilTest {

    @Test
    public void testEnum() {
        Map<String, FieldDefine> map = FieldDefineUtil.getColumns(YesNo.class);
        System.out.println(JacksonUtil.toJson(map));
    }
}