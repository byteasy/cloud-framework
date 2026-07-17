package com.byteasy.util;

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.ProxyExecutable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;

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

    public static Object runScriptWithTimeout(String bindParamName, Map<String, Object> params, String jsScript, long timeoutMillis) {
        // 提交到线程池异步执行
        Future<Object> future = JS_EXECUTOR_POOL.submit(() -> {
            // 这里执行原有的逻辑
            return runScript(bindParamName, params, jsScript);
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

    public static Object runScript(String bindParamName, Map<String, Object> params, String jsScript) {
        if (jsScript == null || jsScript.isBlank()) {
            throw new IllegalArgumentException("JS脚本不能为空");
        }
        Map<String, Object> bindParams = params == null ? new HashMap<>() : params;

        ResourceLimits limits = ResourceLimits.newBuilder()
                .statementLimit(10000, null)
                .build();
        // 2. 将 Engine 绑定到 Context，极大加速 Context 的创建（如果不用对象池，这也是底线优化）
        try (Context context = Context.newBuilder("js")
                .engine(SHARED_ENGINE)
                .allowAllAccess(false)
                .allowIO(false)
                .allowHostAccess(false)
                .allowCreateThread(false)
                .option("sandbox.MaxCPUTime", "3000ms")
                .resourceLimits(limits)
                .build()) {

            Value jsBindings = context.getBindings("js");

            // 3. 优化参数注入：直接利用现成的 jsBindings，避免在循环中调用 eval("{}")
            jsBindings.putMember(bindParamName, wrapJavaObject(context, bindParams));

            // 4. 执行脚本
            Value resultValue;
            try {
                resultValue = context.eval("js", jsScript);
            } catch (PolyglotException e) {
                throw new RuntimeException("JS脚本执行失败：" + formatJsError(e), e);
            }

            // 5. 转换结果
            return convertGraalValueToJavaObject(resultValue);
        }
    }

    /**
     * 重载方法：支持注入 Java 自定义函数
     *
     * @param functions 要注入的 Java 函数映射，Key 为 JS 中的函数名，Value 为 Java 的 Function
     */
    public static Object runScriptWithFunctions(
            String bindParamName,
            Map<String, Object> params,
            Map<String, Function<Object[], Object>> functions, // 接收 Java 函数
            String jsScript) {

        if (jsScript == null || jsScript.isBlank()) {
            throw new IllegalArgumentException("JS脚本不能为空");
        }
        Map<String, Object> bindParams = params == null ? new HashMap<>() : params;

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
            jsBindings.putMember(bindParamName, wrapJavaObject(context, bindParams));

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
        // 基础类型直接包装
        return context.asValue(obj);
    }

    private static Object convertGraalValueToJavaObject(Value val) {
        // 处理 JS 的 null 和 undefined
        if (val == null || val.isNull()) {
            return null;
        }

        // 处理 Date
        if (val.isDate() || val.isTime() || val.isInstant()) {
            Value toIsoFn = val.getMember("getTime");
            return toIsoFn.execute().asLong();
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
