package com.byteasy.util;

import cn.hutool.extra.spring.SpringUtil;
import com.byteasy.component.JsScriptFactory;
import com.byteasy.script.JsFunctionFactory;
import com.byteasy.script.ProxyWrapper;
import lombok.extern.log4j.Log4j2;
import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.ProxyExecutable;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    public static Object runScriptWithTimeout(Map<String, Object> params, String jsScript, String jsName, long timeoutMillis) {
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
                return runScriptWithFunctions(params, map, jsScript, jsName);
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

    public static Object runScript(Map<String, Object> params, String jsScript, String jsName) {
        return runScriptWithFunctions(params, null, jsScript, jsName);
    }

    /**
     * 重载方法：支持注入 Java 自定义函数
     *
     * @param functions 要注入的 Java 函数映射，Key 为 JS 中的函数名，Value 为 Java 的 Function
     */
    public static Object runScriptWithFunctions(Map<String, Object> params, Map<String, Function<Object[], Object>> functions, String jsScript, String jsName) {
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
                .allowAllAccess(true)
                .allowHostAccess(HostAccess.ALL) // 允许显式授权的 Host 访问
                .allowCreateThread(false)
                .resourceLimits(limits)
                .build()) {

            Value jsBindings = context.getBindings("js");

            // 2. 注入业务参数
            for (Map.Entry<String, Object> entry : bindParams.entrySet()) {
                jsBindings.putMember(entry.getKey(), ProxyWrapper.wrap(entry.getValue()));
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
                                inputArgs[i++] = convertGraalValueToJavaObjectV1(argument);
                            }
                        }

                        // 2. 回调你编写的 Java 业务逻辑 Lambda
                        // TODO 还需要增加远程调用，用于永续通过js脚本实现跨领域多个微服务实例直接的function调用。
                        Object result = javaFunc.apply(inputArgs);

                        return ProxyWrapper.wrap(result);
                    });
                });
            }

            // 4. 执行脚本
            Value resultValue;
            try {
                Source cachedScript = com.byteasy.component.JsScriptFactory.getScriptSource(jsName);
                if (cachedScript == null) {
                    cachedScript = Source.newBuilder("js", jsScript, jsName + ".js")
                            .cached(true)
                            .build();
                    JsScriptFactory.setScriptSource(jsName, cachedScript);
                }

                resultValue = context.eval(cachedScript);
            } catch (Exception e) {
                if (e instanceof PolyglotException) {
                    throw new RuntimeException("JS脚本执行失败: " + jsName + " :" + formatJsError((PolyglotException) e), e);
                } else {
                    throw new RuntimeException(e);
                }
            }

            return extractFinalResult(resultValue);
        }
    }


    private static Object convertGraalValueToJavaObjectV1(Value val) {
        if (val == null || val.isNull()) {
            return null;
        }

        // 1. 【最核心优化】：如果这是 Java 传给 JS 然后又被 JS 传回来的对象，直接脱壳！
        // 比如你传进去的 ServiceInstance 或 ListProxy，这里直接 0 毫秒还原成 Java 原生对象。
        if (val.isHostObject()) {
            return val.asHostObject();
        }

        // 2. 日期和时间（保留你之前非常严谨的 JDK 8 时间 API 转换逻辑）
        if (val.isInstant()) {
            return LocalDateTime.ofInstant(val.asInstant(), ZoneId.systemDefault());
        } else if (val.isDate() && val.isTime()) {
            return LocalDateTime.of(val.asDate(), val.asTime());
        } else if (val.isDate()) {
            return val.asDate();
        } else if (val.isTime()) {
            return val.asTime();
        }

        // 3. 【干掉深拷贝】：处理纯纯的 JS Array 和 Object
        // 告别之前的 new ArrayList<>() 和 new HashMap<>() 递归！
        if (val.hasArrayElements()) {
            // GraalVM 会在底层生成一个实现了 java.util.List 的反向代理类。
            // Java 侧调用 list.get(0) 时，底层才会去读 JS 数组的第 0 个元素。实现懒加载！
            return val.as(List.class);
        }

        if (val.hasMembers()) {
            // 同理，自动生成实现了 java.util.Map 的反向代理类。
            return val.as(Map.class);
        }

        // 4. 基础数据类型 (String, Number, Boolean)
        // GraalVM 的 .as(Object.class) 会自动将 JS 的基础类型映射为 Java 的 String, Double, Integer, Boolean
        return val.as(Object.class);
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

    /**
     * 专门用于脚本执行结束时的最终结果提取。
     * 将 GraalVM 的 Value 深度拷贝为纯 Java 对象，防止 Context 关闭后抛出 IllegalStateException。
     */
    public static Object extractFinalResult(org.graalvm.polyglot.Value val) {
        if (val == null || val.isNull()) {
            return null;
        }

        // 1. 如果是原本就从 Java 传进去的对象，直接脱壳，安全且不用拷
        if (val.isHostObject()) {
            return val.asHostObject();
        }

        // 2. 基础类型（直接提取具体值）
        if (val.isString()) return val.asString();
        if (val.isBoolean()) return val.asBoolean();
        if (val.isNumber()) {
            return val.fitsInInt() ? val.asInt() : val.asDouble();
        }

        // 日期处理
        if (val.isInstant()) return java.time.LocalDateTime.ofInstant(val.asInstant(), java.time.ZoneId.systemDefault());

        // 3. 数组类型：必须遍历深拷贝为纯 ArrayList
        if (val.hasArrayElements()) {
            java.util.List<Object> list = new java.util.ArrayList<>((int) val.getArraySize());
            for (long i = 0; i < val.getArraySize(); i++) {
                list.add(extractFinalResult(val.getArrayElement(i)));
            }
            return list;
        }

        // 4. 对象类型：必须遍历深拷贝为纯 HashMap
        if (val.hasMembers()) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            for (String key : val.getMemberKeys()) {
                map.put(key, extractFinalResult(val.getMember(key)));
            }
            return map;
        }

        // 兜底
        return val.as(Object.class);
    }
}
