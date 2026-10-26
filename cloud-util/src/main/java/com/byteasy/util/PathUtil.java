package com.byteasy.util;

import org.apache.commons.lang3.StringUtils;

public final class PathUtil {

    public static boolean isMatch(String src, String target) {
        if (StringUtils.isEmpty(src) || StringUtils.isEmpty(target)) {
            return false;
        }
        String url = target.contains("?") ? target.substring(0, target.indexOf("?")) : target;
        String[] srcArrs = src.split("/");
        String[] tarArrs = url.split("/");
        if (srcArrs.length != tarArrs.length) {
            return false;
        }
        for (int i = 0; i < srcArrs.length; i++) {
            if (!srcArrs[i].contains("{")) {
                if (!srcArrs[i].equalsIgnoreCase(tarArrs[i])) {
                    return false;
                }
            }
        }
        return true;
    }
}
