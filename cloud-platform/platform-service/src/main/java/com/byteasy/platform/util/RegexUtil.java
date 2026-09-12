package com.byteasy.platform.util;

import org.apache.commons.lang3.StringUtils;

public class RegexUtil {

    public static boolean match(String pattern, String uri) {
        if (StringUtils.isEmpty(uri)) {
            return false;
        }
        return uri.matches(pattern);
    }
}
