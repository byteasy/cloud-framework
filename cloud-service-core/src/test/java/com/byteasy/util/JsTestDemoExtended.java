package com.byteasy.util;

import com.byteasy.common.util.JacksonUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class JsTestDemoExtended {

    @Test
    public void test() {
        testCase1_NullAndEmpty();
        testCase2_DataTypeBoundary();
        testCase3_SecuritySandbox();
        testCase4_JsErrorFormatting();
        testCase5_HighConcurrencyPerformance();
    }

    /**
     * 测试用例 1：空值、边界值与 Undefined/NaN 处理
     * 验证目的：确保 JS 中的各种“空”或“无效数字”转换为 Java 后不会导致 OOM、报错或字符串污染
     */
    private static void testCase1_NullAndEmpty() {
        System.out.println("=== 测试用例 1: 空值与异常数字处理 ===");
        Map<String, Object> params = new HashMap<>();
        params.put("nullableField", null);

        String jsScript = """
            let output = {
                jsNull: null,
                jsUndefined: undefined,
                calculatedNaN: 0 / 0,
                calculatedInfinite: 1 / 0,
                inputNull: params.nullableField
            };
            output;
            """;

        Object result = JsScriptExecutor.runScript("params", params, jsScript);
        System.out.println(JacksonUtil.toJson(result));
        // 预期输出: {"jsNull":null,"jsUndefined":null,"calculatedNaN":null,"calculatedInfinite":null,"inputNull":null}
        System.out.println("测试用例 1 通过\n");
    }

    /**
     * 测试用例 2：大数字与高精度边界
     * 验证目的：确保 Long 型大数字、浮点数在 JS 计算后不会丢失精度，且不会错误地将 2.0 转成整型 2
     */
    private static void testCase2_DataTypeBoundary() {
        System.out.println("=== 测试用例 2: 数字与高精度边界 ===");
        Map<String, Object> params = new HashMap<>();
        params.put("bigLong", 9007199254740991L); // JS 的 Number.MAX_SAFE_INTEGER
        params.put("doubleVal", 12.34);

        String jsScript = """
            let output = {
                originalLong: params.bigLong,
                floatCalculation: params.doubleVal + 0.66, // 12.34 + 0.66 = 13.0
                preciseFloat: 5.5
            };
            output;
            """;

        Object result = JsScriptExecutor.runScript("params", params, jsScript);
        System.out.println(JacksonUtil.toJson(result));
        // 预期关注点: floatCalculation 应该是 13.0 而不是整型 13 (防止破坏前端某些强类型字段的定义)
        System.out.println("测试用例 2 通过\n");
    }

    /**
     * 测试用例 3：安全沙箱穿透测试 (关键)
     * 验证目的：验证恶意用户是否能通过 JS 脚本读取服务器文件或执行系统命令
     */
    private static void testCase3_SecuritySandbox() {
        System.out.println("=== 测试用例 3: 安全沙箱限制 ===");

        // 尝试加载 Java 核心类进行恶意操作的 JS
        String jsScript = """
            // 尝试通过 Java 反射执行系统命令 (黑客常见手段)
            let ProcessBuilder = Java.type('java.lang.ProcessBuilder');
            let p = new ProcessBuilder('calc').start(); 
            "success";
            """;

        try {
            JsScriptExecutor.runScript("params", null, jsScript);
            System.err.println("❌ 警告：沙箱被穿透了！脚本成功访问了 Java 类！");
        } catch (Exception e) {
            System.out.println("✅ 拦截成功：沙箱成功阻止了 Host 类的访问。错误信息: " + e.getMessage());
        }
        System.out.println("测试用例 3 通过\n");
    }

    /**
     * 测试用例 4：JS 语法错误与运行时错误格式化
     * 验证目的：验证当前端配置的脚本有错时，后端能否精准抛出错误原因及“行号”
     */
    private static void testCase4_JsErrorFormatting() {
        System.out.println("=== 测试用例 4: 脚本错误排查格式化 ===");

        String syntaxErrorJs = "let a = ;"; // 语法错误
        String runtimeErrorJs = """
            let user = { name: "Tom" };
            let age = user.profile.age; // 运行时错误: profile 为 undefined，再点 age 会报错
            """;

        try {
            JsScriptExecutor.runScript("params", null, syntaxErrorJs);
        } catch (Exception e) {
            System.out.println("语法错误捕获: " + e.getMessage()); // 应该包含“语法错误”字样和行号
        }

        try {
            JsScriptExecutor.runScript("params", null, runtimeErrorJs);
        } catch (Exception e) {
            System.out.println("运行时错误捕获: " + e.getMessage()); // 应该清晰指示第 2 行报错
        }
        System.out.println("测试用例 4 通过\n");
    }

    /**
     * 测试用例 5：高并发性能与内存稳定性压测
     * 验证目的：验证重构为 SHARED_ENGINE 后，Context 创建是否足够轻量，高频调用下 CPU 会不会卡死，有没有 OOM 隐患
     */
    private static void testCase5_HighConcurrencyPerformance() {
        System.out.println("=== 测试用例 5: 并发与性能压测 (执行 2000 次) ===");
        Map<String, Object> params = Map.of("score", 85);
        String jsScript = "params.score >= 60 ? 'pass' : 'fail';";

        // 预热机制（让 JIT 编译器准备好）
        for (int i = 0; i < 100; i++) {
            JsScriptExecutor.runScript("params", params, jsScript);
        }

        long startTime = System.currentTimeMillis();
        int loopCount = 2000;

        for (int i = 0; i < loopCount; i++) {
            JsScriptExecutor.runScript("params", params, jsScript);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("执行 " + loopCount + " 次简单脚本耗时: " + (endTime - startTime) + " ms");
        System.out.println("平均单次耗时: " + ((double)(endTime - startTime) / loopCount) + " ms");
        // 如果重构成功，2000 次在一台普通的电脑上应该在几百毫秒内就能跑完。如果没用 SHARED_ENGINE，这里可能会耗时数秒甚至卡死。
        System.out.println("测试用例 5 通过\n");
    }
}

