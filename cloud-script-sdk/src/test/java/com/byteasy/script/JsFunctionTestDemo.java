package com.byteasy.script;

import com.byteasy.util.JacksonUtil;
import com.byteasy.platform.dto.ScriptFunctionDTO;
import com.byteasy.script.util.JsScriptExecutor;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class JsFunctionTestDemo {

    @Test
    public void test() {
        System.out.println("=== 测试：JS 调用 Java 函数 ===");

        // 1. 模拟前端参数（包含商品价格和用户ID）
        Map<String, Object> frontParams = new HashMap<>();
        frontParams.put("price", 100.0);
        frontParams.put("userId", "USER_12345");

        // 2. 准备要传递给 JS 的 Java 函数
        Map<String, ScriptFunctionDTO> functions = new HashMap<>();

        // 函数 A：根据用户 ID 获取折扣（模拟查库或调用远程 RPC）
        ScriptFunctionDTO scriptFunction = new ScriptFunctionDTO();
        scriptFunction.setName("getDiscountByUserId");
        scriptFunction.setFunction((userIdObj) -> {
            String userId = String.valueOf(userIdObj[0]);
            System.out.println("[Java 收到回调] 正在为用户 " + userId + " 查询折扣...");

            if ("USER_12345".equals(userId)) {
                return 0.85; // 85 折
            }
            return 1.0; // 原价
        });
        functions.put("getDiscountByUserId", scriptFunction);

        // 函数 B：记录日志工具
        ScriptFunctionDTO scriptFunction1 = new ScriptFunctionDTO();
        scriptFunction1.setName("logFromJs");
        scriptFunction1.setFunction((msg) -> {
            System.out.println("[JS Log]: " + JacksonUtil.toJson(msg));
            return null; // JS 不需要返回值时，Java 返回 null 即可
        });
        functions.put("logFromJs", scriptFunction1);

        // 3. 编写 JS 脚本
        // 脚本内部可以直接像调用普通 JS 函数一样调用 `getDiscountByUserId` 和 `logFromJs`
        String jsScript = """
                logFromJs("开始计算最终价格...");
                
                // 调用 Java 函数获取折扣
                let discount = getDiscountByUserId(userId); 
                
                logFromJs("获取到的折扣是: " + discount);
                
                let finalPrice = price * discount;
                
                // 返回结果
                ({
                    originalPrice: price,
                    discount: discount,
                    payPrice: finalPrice
                });
                """;

        // 4. 使用新方法执行
        Object resultObj = JsScriptExecutor.runScriptWithFunctions(frontParams, functions, jsScript, "test");

        // 5. 输出结果
        System.out.println("\n返回前端JSON：");
        System.out.println(JacksonUtil.toJson(resultObj));
    }
}

