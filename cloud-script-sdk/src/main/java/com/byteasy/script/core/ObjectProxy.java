package com.byteasy.script.core;

import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectProxy implements ProxyObject {

    private final Object target;
    private final ClassPropertyMeta meta;

    public ObjectProxy(Object target) {
        this.target = target;
        this.meta = ClassPropertyMeta.getMeta(target.getClass());
    }

    public Object getTarget() {
        return target;
    }

    @Override
    public Object getMember(String key) {
        // 🔥 1. 防御：如果访问的是 class 属性，直接返回 null，拒绝反射 Class 对象
        if ("class".equals(key)) {
            return null;
        }

        PropertyAccessor accessor = meta.accessors.get(key);
        if (accessor == null) {
            return null;
        }
        try {
            Object val = accessor.getValue(target);
            return ProxyWrapper.wrap(val);
        } catch (Exception e) {
            // 打印警告而不是直接抛出破坏性异常，增强运行时容错性
            return null;
        }
    }

    @Override
    public Object getMemberKeys() {
        return meta.memberKeys;
    }

    @Override
    public boolean hasMember(String key) {
        if ("class".equals(key)) {
            return false;
        }
        return meta.accessors.containsKey(key);
    }

    @Override
    public void putMember(String key, Value value) {
        if ("class".equals(key)) {
            return;
        }
        PropertyAccessor accessor = meta.accessors.get(key);
        if (accessor == null) {
            return;
        }
        try {
            Object rawValue = (value == null || value.isNull()) ? null : value.as(accessor.type);
            accessor.setValue(target, rawValue);
        } catch (Exception e) {
            throw new RuntimeException("修改实体属性失败 [" + key + "]: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean removeMember(String key) {
        putMember(key, null);
        return true;
    }

    // ====================== 反射与方法缓存核心组件 ======================

    private static class ClassPropertyMeta {
        private static final Map<Class<?>, ClassPropertyMeta> CACHE = new ConcurrentHashMap<>();

        final Map<String, PropertyAccessor> accessors;
        final String[] memberKeys;

        static ClassPropertyMeta getMeta(Class<?> clazz) {
            return CACHE.computeIfAbsent(clazz, ClassPropertyMeta::buildMeta);
        }

        private static ClassPropertyMeta buildMeta(Class<?> clazz) {
            Map<String, PropertyAccessor> map = new HashMap<>();

            // 1. 扫描字段（递归直到 Object.class）
            Class<?> current = clazz;
            while (current != null && current != Object.class) {
                for (Field field : current.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers())) continue;
                    // 🔥 忽略 class 字段
                    if ("class".equals(field.getName())) continue;

                    try {
                        field.setAccessible(true);
                        map.putIfAbsent(field.getName(), new PropertyAccessor(field.getType(), field));
                    } catch (Exception ignored) {
                        // 忽略某些 JDK 模块无法 setAccessible 的受保护字段
                    }
                }
                current = current.getSuperclass();
            }

            // 2. 匹配并优先绑定 Getter / Setter 方法
            for (Method method : clazz.getMethods()) {
                if (Modifier.isStatic(method.getModifiers())) continue;
                String methodName = method.getName();

                // 识别 Getter
                if ((methodName.startsWith("get") && methodName.length() > 3) ||
                        (methodName.startsWith("is") && methodName.length() > 2)) {
                    if (method.getParameterCount() == 0) {
                        String prop = decapitalize(methodName.startsWith("get") ? methodName.substring(3) : methodName.substring(2));

                        // 🔥 核心关键点：坚决排除 getClass() 以及任何名为 "class" 的属性！
                        if ("class".equals(prop) || method.getReturnType().equals(Class.class)) {
                            continue;
                        }

                        PropertyAccessor acc = map.computeIfAbsent(prop, k -> new PropertyAccessor(method.getReturnType(), null));
                        acc.getter = method;
                    }
                }
                // 识别 Setter
                else if (methodName.startsWith("set") && methodName.length() > 3) {
                    if (method.getParameterCount() == 1) {
                        String prop = decapitalize(methodName.substring(3));
                        if ("class".equals(prop)) {
                            continue;
                        }
                        PropertyAccessor acc = map.computeIfAbsent(prop, k -> new PropertyAccessor(method.getParameterTypes()[0], null));
                        acc.setter = method;
                    }
                }
            }

            return new ClassPropertyMeta(map, map.keySet().toArray(new String[0]));
        }

        private static String decapitalize(String name) {
            if (name == null || name.isEmpty()) return name;
            char[] chars = name.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            return new String(chars);
        }

        private ClassPropertyMeta(Map<String, PropertyAccessor> accessors, String[] memberKeys) {
            this.accessors = accessors;
            this.memberKeys = memberKeys;
        }
    }

    private static class PropertyAccessor {
        final Class<?> type;
        final Field field;
        Method getter;
        Method setter;

        PropertyAccessor(Class<?> type, Field field) {
            this.type = type;
            this.field = field;
        }

        Object getValue(Object target) throws Exception {
            if (getter != null) {
                return getter.invoke(target);
            }
            if (field != null) {
                return field.get(target);
            }
            return null;
        }

        void setValue(Object target, Object value) throws Exception {
            if (setter != null) {
                setter.invoke(target, value);
            } else if (field != null) {
                field.set(target, value);
            }
        }
    }
}