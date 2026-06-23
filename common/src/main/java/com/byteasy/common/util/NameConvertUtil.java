package com.byteasy.common.util;

public class NameConvertUtil {

    /**
     * 驼峰命名 转 下划线命名 (小驼峰 → 下划线小写)
     * 例如: userName → user_name
     * userId   → user_id
     * UserName → user_name (兼容大驼峰)
     */
    public static String camelToUnderline(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(camelCase.charAt(0)));

        for (int i = 1; i < camelCase.length(); i++) {
            char c = camelCase.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append('_');
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线命名 转 驼峰命名 (自动首字母小写)
     * 例如: user_name → userName
     * user_id   → userId
     */
    public static String underlineToCamel(String underline) {
        if (underline == null || underline.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean nextUpper = false;

        for (int i = 0; i < underline.length(); i++) {
            char c = underline.charAt(i);
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    sb.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
        }
        return sb.toString();
    }

    // 测试
    public static void main(String[] args) {
        System.out.println(camelToUnderline("user"));    // user_name
        System.out.println(camelToUnderline("userName"));    // user_name
        System.out.println(camelToUnderline("userId"));      // user_id
        System.out.println(camelToUnderline("showCreateTime"));   // create_time

        System.out.println(underlineToCamel("create"));    // userName
        System.out.println(underlineToCamel("create_user_name"));    // userName
        System.out.println(underlineToCamel("user_id"));      // userId
        System.out.println(underlineToCamel("create_time"));  // createTime
    }
}
