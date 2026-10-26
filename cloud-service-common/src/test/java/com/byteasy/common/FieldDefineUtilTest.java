package com.byteasy.common;


import com.byteasy.util.FieldDefineUtil;
import com.byteasy.util.JacksonUtil2;
import com.byteasy.enums.YesNo;
import com.byteasy.platform.entity.FieldDefine;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class FieldDefineUtilTest {

    @Test
    public void testEnum() {
        Map<String, FieldDefine> map = FieldDefineUtil.getColumns(YesNo.class);
        System.out.println(JacksonUtil2.toJson(map));
    }
}