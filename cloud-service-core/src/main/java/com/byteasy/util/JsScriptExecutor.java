package com.byteasy.util;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.script.JsFunctionFactory;
import lombok.extern.log4j.Log4j2;
import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.ProxyExecutable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

@Log4j2
public class JsScriptExecutor {

    // 1. 提取全局唯一的共享 Engine，避免重复初始化 JS 编译器内核
    private static final Engine SHARED_ENGINE = Engine.newBuilder().build();

    // 创建一个专用于执行 JS 脚本的隔离线程池（防止主业务线程被卡死）
    private static final ExecutorService JS_EXECUTOR_POOL = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 2, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadFactory() {
                private int count = 1;

                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "js-runner-" + count++);
                    t.setDaemon(true); // 设为守护线程
                    return t;
                }
            }
    );

    public static Object runScriptWithTimeout(Map<String, Object> params, String jsScript, long timeoutMillis) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        // 提交到线程池异步执行
        Future<Object> future = JS_EXECUTOR_POOL.submit(() -> {
            RequestContextHolder.setRequestAttributes(attributes);
            try {
                Map<String, Function<Object[], Object>> map = null;
                try {
                    JsFunctionFactory jsFunctionFactory = SpringUtil.getBean(JsFunctionFactory.class);
                    map = jsFunctionFactory.getJsFunctionMap();
                } catch (Exception e) {
                }
                // 这里执行原有的逻辑
                return runScriptWithFunctions(params, map, jsScript);
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        });

        try {
            // 在 Java 侧强制控制最大等待时间
            return future.get(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true); // 终断正在执行 JS 的线程
            throw new RuntimeException("JS脚本执行超时，已被系统强制拦截！", e);
        } catch (ExecutionException e) {
            // 重新抛出内部的业务/语法异常
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw new RuntimeException("JS脚本执行异常: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("脚本执行被系统中断", e);
        }
    }

    public static Object runScript(Map<String, Object> params, String jsScript) {
        return runScriptWithFunctions(params, null, jsScript);
    }

    /**
     * 重载方法：支持注入 Java 自定义函数
     *
     * @param functions 要注入的 Java 函数映射，Key 为 JS 中的函数名，Value 为 Java 的 Function
     */
    public static Object runScriptWithFunctions(Map<String, Object> params, Map<String, Function<Object[], Object>> functions, String jsScript) {
        if (jsScript == null || jsScript.isBlank()) {
            throw new IllegalArgumentException("JS脚本不能为空");
        }
        Map<String, Object> bindParams = params == null ? Collections.EMPTY_MAP : params;

        ResourceLimits limits = ResourceLimits.newBuilder()
                .statementLimit(10000, null)
                .build();
        // 1. 关键修改：配置 HostAccess.EXPLICIT，只允许 JS 调用带有 @HostAccess.Export 注解的方法
        // 这样既安全，又开放了函数回调能力
        try (Context context = Context.newBuilder("js")
                .engine(SHARED_ENGINE)
                .allowAllAccess(false)
                .allowIO(false)
                .allowHostAccess(HostAccess.EXPLICIT) // 允许显式授权的 Host 访问
                .allowCreateThread(false)
                .resourceLimits(limits)
                .build()) {

            Value jsBindings = context.getBindings("js");

            // 2. 注入业务参数
            for (Map.Entry<String, Object> entry : bindParams.entrySet()) {
                jsBindings.putMember(entry.getKey(), wrapJavaObject(context, entry.getValue()));
            }

            // 3. 注入 Java 函数
            if (functions != null) {
                functions.forEach((funcName, javaFunc) -> {
                    // 使用 ProxyExecutable 将 Java 函数包装为标准的 JS 可执行函数
                    jsBindings.putMember(funcName, (ProxyExecutable) (Value[] arguments) -> {
                        // 1. 获取 JS 传过来的第一个参数（因为 Function 只有一个入参）
                        Object[] inputArgs = null;
                        if (arguments.length > 0) {
                            // 利用现有的 convertGraalValueToJavaObject 把 Value 转回 Java 原生类型
                            inputArgs = new Object[arguments.length];
                            int i = 0;
                            for (Value argument : arguments) {
                                inputArgs[i++] = convertGraalValueToJavaObject(argument);
                            }
                        }

                        // 2. 回调你编写的 Java 业务逻辑 Lambda
                        Object result = javaFunc.apply(inputArgs);

                        // 3. 将 Java 返回值安全包装回 Value 塞给 JS（如果你的方法叫 javaObjectToGraalValue，请对应替换方法名）
                        return wrapJavaObject(context, result);
                    });
                });
            }

            // 4. 执行脚本
            Value resultValue;
            try {
                resultValue = context.eval("js", jsScript);
            } catch (PolyglotException e) {
                throw new RuntimeException("JS脚本执行失败：" + formatJsError(e), e);
            }

            return convertGraalValueToJavaObject(resultValue);
        }
    }

    /**
     * 优化后的参数包装：不再频繁调用 eval()
     */
    private static Value wrapJavaObject(Context context, Object obj) {
        if (obj == null) {
            return context.asValue(null);
        }
        if (isBasicType(obj.getClass())) {
            return context.asValue(obj);
        }
        if (obj instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) obj;
            // 获取 JS 的内置 Object 构造器，直接创建空对象，性能比 eval("{}") 高数倍
            Value jsObj = context.getBindings("js").getMember("Object").newInstance();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (entry.getKey() != null) {
                    jsObj.putMember(entry.getKey().toString(), wrapJavaObject(context, entry.getValue()));
                }
            }
            return jsObj;
        }
        if (obj instanceof Iterable<?>) {
            Iterable<?> list = (Iterable<?>) obj;
            // 获取 JS 的内置 Array 构造器
            Value jsArr = context.getBindings("js").getMember("Array").newInstance();
            long idx = 0;
            for (Object item : list) {
                jsArr.setArrayElement(idx++, wrapJavaObject(context, item));
            }
            return jsArr;
        }
        if (obj instanceof IPage<?>) {
            IPage<?> page = (IPage<?>) obj;
            Value jsPage = context.getBindings("js").getMember("Object").newInstance();

            // 1. 递归转换核心数据列表 records (它是一个 List，会走到下面的 Iterable 分支)
            jsPage.putMember("records", wrapJavaObject(context, page.getRecords()));

            // 2. 注入分页基础属性。基础数据类型直接使用 asValue 即可
            jsPage.putMember("total", context.asValue(page.getTotal()));
            jsPage.putMember("size", context.asValue(page.getSize()));
            jsPage.putMember("current", context.asValue(page.getCurrent()));
            jsPage.putMember("pages", context.asValue(page.getPages()));

            return jsPage;
        }
        // 4. 数组类型处理 (包含 Object[] 和 基本类型数组如 int[], double[])
        if (obj.getClass().isArray()) {
            Value jsArr = context.getBindings("js").getMember("Array").newInstance();
            // 使用反射 Array 工具类统一处理，避免强转 Object[] 时基本类型数组报错
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                Object item = Array.get(obj, i);
                jsArr.setArrayElement(i, wrapJavaObject(context, item));
            }
            return jsArr;
        }
        log.info("wrapJavaObject class: {}", obj.getClass().getName());
        Map<String, Object> map = JacksonUtil.obj2Map(obj);
        return wrapJavaObject(context, map);
    }

    /**
     * 判断是否为可以被直接传递给 JS 的基本类型
     */
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

    private static Object convertGraalValueToJavaObject(Value val) {
        // 处理 JS 的 null 和 undefined
        if (val == null || val.isNull()) {
            return null;
        }

        // 处理 Date 和 Time
        if (val.isInstant()) {
            // 1. 包含时区的完整时间（如 JS Date 或 Java Timestamp）
            // 推荐转为 LocalDateTime，这样 Jackson/Fastjson 就能走你配置好的全局时间格式化器
            return LocalDateTime.ofInstant(val.asInstant(), ZoneId.systemDefault());

        } else if (val.isDate() && val.isTime()) {
            // 2. 包含日期和时间，但没有时区信息
            // 直接合并成 java.time.LocalDateTime
            return LocalDateTime.of(val.asDate(), val.asTime());

        } else if (val.isDate()) {
            // 3. 只有日期 (比如 "2023-10-01")
            // val.asDate() 底层直接返回 java.time.LocalDate，序列化框架能完美识别
            return val.asDate();

        } else if (val.isTime()) {
            // 4. 只有时间 (比如 "14:30:00")
            // val.asTime() 底层直接返回 java.time.LocalTime
            return val.asTime();
        }

        // 处理数组
        if (val.hasArrayElements()) {
            List<Object> list = new ArrayList<>();
            long arraySize = val.getArraySize();
            for (long i = 0; i < arraySize; i++) {
                list.add(convertGraalValueToJavaObject(val.getArrayElement(i)));
            }
            return list;
        }

        // 处理普通 JS 对象
        if (val.hasMembers() && !val.isHostObject()) {
            Map<String, Object> map = new HashMap<>();
            val.getMemberKeys().forEach(key -> {
                Value childVal = val.getMember(key);
                map.put(key, convertGraalValueToJavaObject(childVal));
            });
            return map;
        }

        // 处理基础数据类型
        if (val.isString()) {
            return val.asString();
        }
        if (val.isBoolean()) {
            return val.asBoolean();
        }
        if (val.isNumber()) {
            double doubleVal = val.asDouble();
            // 处理 JS 计算产生的 NaN 或无穷大
            if (Double.isNaN(doubleVal) || Double.isInfinite(doubleVal)) {
                return null;
            }
            // 修复点：只有当它确实“适合”转为整数，且在数学上等于整数，同时排除掉原本就是浮点计算的情况
            // 或者更安全的做法：检查原始 JS Value 是否有浮点特征
            if (val.fitsInInt() && doubleVal == (int) doubleVal) {
                // 如果你的业务强依赖浮点输出，这里可以加一层判断
                // 为了绝对安全，建议业务上如果是金额、计量等浮点数，直接返回 Double
                return val.asInt();
            } else if (val.fitsInLong() && doubleVal == (long) doubleVal) {
                return val.asLong();
            }
            return doubleVal;
        }

        return val.toString();
    }

    private static String formatJsError(PolyglotException e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getMessage());
        if (e.getSourceLocation() != null) {
            sb.append("，行号：").append(e.getSourceLocation().getStartLine());
        }
        if (e.isSyntaxError()) {
            sb.append("（语法错误）");
        }
        return sb.toString();
    }
}
