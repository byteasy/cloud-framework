package com.byteasy.script.core;

import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;

import java.util.List;
import java.util.Map;

public class MapProxy implements ProxyObject {
    private final Map<String, Object> targetMap;

    public MapProxy(Map<String, Object> targetMap) {
        this.targetMap = targetMap;
    }

    // JS 访问 obj.key 时触发
    @Override
    public Object getMember(String key) {
        Object value = targetMap.get(key);
        // 按需懒包装子属性
        return ProxyWrapper.wrap(value);
    }

    // JS 获取对象所有 key 时触发（例如 Object.keys(obj) 或 for...in）
    @Override
    public Object getMemberKeys() {
        // GraalVM 允许直接返回一个包含 key 的 ProxyArray 或 Java 数组
        return new ListProxy(List.copyOf(targetMap.keySet()));
    }

    // JS 检查属性是否存在时触发（例如 'key' in obj）
    @Override
    public boolean hasMember(String key) {
        return targetMap.containsKey(key);
    }

    @Override
    public void putMember(String key, Value value) {
        // 当 JS 执行 service.children = [...] 时，会触发这里
        // 注意：要把 JS 传过来的 Value 解包成 Java 原生对象再 put 进去
        // 这里可以直接借用我们刚才在 JsScriptExecutor 里写好的脱壳方法
        Object javaObj = com.byteasy.util.JsScriptExecutor.extractFinalResult(value);
        targetMap.put(key, javaObj);
    }

    /**
     * 可选：支持在 JS 中执行 delete obj.xxx
     */
    @Override
    public boolean removeMember(String key) {
        if (targetMap.containsKey(key)) {
            targetMap.remove(key);
            return true;
        }
        return false;
    }
}
