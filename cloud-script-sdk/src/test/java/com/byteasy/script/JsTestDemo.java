package com.byteasy.script;

import com.byteasy.util.JacksonUtil;
import com.byteasy.script.util.JsScriptExecutor;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class JsTestDemo {

    @Test
    public void test() {
        // 1. 模拟前端传入参数
        String frontJson = "{\"name\":\"测试用户\",\"age\":25,\"birth\":\"2000-01-01\"}";
        Map<String, Object> frontParams;
        try {
            frontParams = JacksonUtil.parse(frontJson, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("前端参数解析失败", e);
        }

        // 2. 后台配置的JS脚本
        String jsScript = """
                let res = {
                  success: true,
                  username: name,
                  realAge: age + 1,
                  now: new Date(),
                  tags: ["normal", "user"],
                  extra: {
                    createTime: new Date()
                  }
                };
                res.success = false;
                age = 99;
                ;[res]
                """;

        // 3. 执行脚本，拿到可直接序列化的Java对象
        Object resultObj = JsScriptExecutor.runScript(frontParams, jsScript, "test");

        // 4. 序列化为JSON返回前端
        String resultJson;
        try {
            resultJson = JacksonUtil.toJson(resultObj);
        } catch (Exception e) {
            throw new RuntimeException("结果序列化失败", e);
        }

        System.out.println("返回前端JSON：");
        System.out.println(resultJson);
    }
}
