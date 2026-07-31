package com.byteasy.script;

import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.baomidou.mybatisplus.core.metadata.IPage;

public class ProxyWrapper {

    /**
     * 替代你原来的 wrapJavaObject
     */
    public static Object wrap(Object obj) {
        if (obj == null) {
            return null; // GraalVM 会自动识别为 JS 的 null
        }

        // 1. 基本类型：GraalVM 原生支持，直接返回，无需处理
        if (isBasicType(obj.getClass())) {
            return obj;
        }

        // 2. 包装 List：O(1) 瞬间完成
        if (obj instanceof List) {
            return new ListProxy((List<?>) obj);
        }

        // 3. 包装 Map：O(1) 瞬间完成
        if (obj instanceof Map) {
            return new MapProxy((Map<String, Object>) obj);
        }

        // 4. 特殊处理 MyBatis-Plus 的 IPage
        if (obj instanceof IPage) {
            IPage<?> page = (IPage<?>) obj;
            // 组装成一个临时的 Map 扔给 MapProxy 即可
            return new MapProxy(Map.of(
                    "records", new ListProxy(page.getRecords()),
                    "total", page.getTotal(),
                    "size", page.getSize(),
                    "current", page.getCurrent(),
                    "pages", page.getPages()
            ));
        }

        /**
         * HostAccess.ALL，性能优先，直接返回obj
         */
        return obj;
        // 5. 其他普通 POJO 实体类
        // 如果你的实体类上有 @HostAccess.Export，直接 return obj 即可。
        // 如果没有，转成 Map 再用 MapProxy 包装（这里可以引入 BeanMap 等轻量级反射工具，避免序列化）
//        return new MapProxy(com.byteasy.common.util.JacksonUtil.obj2Map(obj));
    }

    private static boolean isBasicType(Class<?> clazz) {
        return clazz.isPrimitive() ||                              // 基本数据类型 (int, boolean, double 等)
                Number.class.isAssignableFrom(clazz) ||             // 包装数字类及其子类 (Integer, Long, BigDecimal, BigInteger 等)
                CharSequence.class.isAssignableFrom(clazz) ||       // 字符串相关 (String, StringBuilder, StringBuffer 等)
                Boolean.class.equals(clazz) ||                      // 包装类 Boolean
                Character.class.equals(clazz) ||                    // 包装类 Character
                Date.class.isAssignableFrom(clazz) ||               // 老版本日期类型 (java.util.Date, java.sql.Date 等)
                TemporalAccessor.class.isAssignableFrom(clazz) ||   // JDK 8+ 现代时间类型 (LocalDate, LocalDateTime, Instant, ZonedDateTime 等统一接口)
                UUID.class.isAssignableFrom(clazz) ||               // UUID 通常也视作基础类型(字符串)处理
                clazz.isEnum();                                     // 枚举类型 (GraalVM 原生支持将 Java 枚举映射到 JS)
    }
}
