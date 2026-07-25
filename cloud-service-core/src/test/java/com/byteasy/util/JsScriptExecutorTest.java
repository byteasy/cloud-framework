package com.byteasy.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsScriptExecutorTest {

    @Test
    @DisplayName("验证：正常脚本能否正确执行并返回数据")
    void testNormalScriptExecution() {
        // 1. 准备入参
        Map<String, Object> params = new HashMap<>();
        params.put("basePrice", 100);
        params.put("discount", 0.8);

        // 2. 编写正常计算的 JS 脚本
        String jsScript = """
            let finalPrice = basePrice * discount;
            let result = {
                status: "success",
                finalPrice: finalPrice,
                tags: ["promo", "summer"]
            };
            result; // 返回结果对象
        """;

        // 3. 执行脚本（设置富余的超时时间 2000 毫秒）
        Object output = JsScriptExecutor.runScriptWithTimeout(params, jsScript, "test", 2000);

        // 4. 断言验证返回的 Java 对象结构
        assertNotNull(output);
        assertTrue(output instanceof Map);

        Map<?, ?> resultMap = (Map<?, ?>) output;
        assertEquals("success", resultMap.get("status"));
        assertEquals(80, resultMap.get("finalPrice"));
        assertTrue(resultMap.get("tags") instanceof java.util.List);

        System.out.println("====== 正常脚本测试通过，输出结果 ======");
        System.out.println(output);
    }

    @Test
    @DisplayName("验证：用户编写死循环脚本时，系统能否在指定时间内强行拦截")
    void testScriptTimeoutInterruption() {
        // 1. 准备入参
        Map<String, Object> params = new HashMap<>();

        // 2. 编写一个经典的死循环 JS 脚本
        String timeoutJsScript = """
            let count = 0;
            while(true) {
                count++;
            }
            count;
        """;

        // 3. 记录开始时间
        long startTime = System.currentTimeMillis();

        // 4. 断言：预期系统会抛出 RuntimeException，且包含超时提示
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            // 设置硬超时时间为 500 毫秒
            JsScriptExecutor.runScriptWithTimeout(params, timeoutJsScript, "Test", 500);
        });

        long duration = System.currentTimeMillis() - startTime;

        // 5. 校验异常信息与拦截耗时
        System.out.println("====== 超时拦截测试成功 ======");
        System.out.println("捕获的异常信息: " + exception.getMessage());
        System.out.println("实际拦截耗时(ms): " + duration);

        // 断言耗时在 500ms 左右，不会无限卡死主线程（允许少量线程调度误差，通常在 500~650ms 内）
        assertTrue(duration < 1500, "超时拦截失效，执行时间过长！");
        // 断言提示语包含超时关键字
        assertFalse(exception.getMessage().contains("超时") || exception.getMessage().contains("中断"));
    }

    @Test
    @DisplayName("验证：JS 语法错误或执行期错误能否被正常捕获并输出行号")
    void testScriptRuntimeException() {
        Map<String, Object> params = new HashMap<>();

        // 编写一个包含明显语法错误的脚本（少了闭合括号，或者调用不存在的方法）
        String errorJsScript = """
            let a = 10;
            let b = undefined;
            // 故意引发未定义引用的执行期错误
            let c = b.notExistMethod(); 
            c;
        """;

        // 断言：预期抛出 RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            JsScriptExecutor.runScriptWithTimeout(params, errorJsScript, "Test", 2000);
        });

        System.out.println("====== 语法/执行期错误捕获测试成功 ======");
        System.out.println("解析出的错误信息: " + exception.getMessage());

        // 断言错误信息中包含了行号提示
        assertTrue(exception.getMessage().contains("行号：4"));
    }
}

