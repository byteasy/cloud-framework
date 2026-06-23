package com.byteasy.util;

import cn.hutool.extra.spring.SpringUtil;
import jakarta.servlet.ServletContext;

public class SpringContextUtil {

    public static String getContextPath() {
        return SpringUtil.getApplicationContext().getBean(ServletContext.class).getContextPath();
    }
}
