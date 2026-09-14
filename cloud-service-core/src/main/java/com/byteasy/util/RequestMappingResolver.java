package com.byteasy.util;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RequestMappingResolver {

    /**
     * 根据 Controller 类及其实例方法，获取完整的 URI 路径列表
     *
     * @param targetClass Controller 类（如 ResourceController.class）
     * @param method      Controller 中的具体方法（如 selectByRouterPath）
     * @return 完整的标准 URI 路径（如 ["/sapi/resources/leftMenu"]）
     */
    public static List<String> getFullMethodUris(Class<?> targetClass, Method method) {
        // 1. 解析类上的路径（如 @RequestMapping(Constant.SAPI + "/resources")）
        List<String> classPaths = getClassPaths(targetClass);

        // 2. 解析方法上的路径（支持 @GetMapping, @PostMapping, @RequestMapping 等）
        List<String> methodPaths = getMethodPaths(method);

        // 3. 组合类路径和方法路径
        return combinePaths(classPaths, methodPaths);
    }

    /**
     * 获取方法上完整的单条主 URI（如果配置了多个路径，默认取第一个，绝大部分接口只有一个路径）
     */
    public static String getFullMethodUri(Class<?> targetClass, Method method) {
        List<String> fullUris = getFullMethodUris(targetClass, method);
        return fullUris.isEmpty() ? "" : fullUris.get(0);
    }

    /**
     * 解析方法支持的 HTTP Method（GET, POST 等）
     */
    public static List<RequestMethod> getHttpMethods(Method method) {
        RequestMapping mapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        if (mapping != null && mapping.method().length > 0) {
            return Arrays.asList(mapping.method());
        }
        return Collections.emptyList();
    }

    // ======================== 私有内部辅助方法 ========================

    private static List<String> getClassPaths(Class<?> targetClass) {
        // 使用 findMergedAnnotation 自动合并继承关系与别名
        RequestMapping classMapping = AnnotatedElementUtils.findMergedAnnotation(targetClass, RequestMapping.class);
        if (classMapping == null) {
            return Collections.singletonList("");
        }
        String[] paths = classMapping.path().length > 0 ? classMapping.path() : classMapping.value();
        return paths.length > 0 ? Arrays.asList(paths) : Collections.singletonList("");
    }

    private static List<String> getMethodPaths(Method method) {
        // 核心点：无论是 @GetMapping、@PostMapping 还是 @RequestMapping，底层都会被识别为 RequestMapping
        RequestMapping methodMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        if (methodMapping == null) {
            return Collections.emptyList();
        }
        String[] paths = methodMapping.path().length > 0 ? methodMapping.path() : methodMapping.value();
        return paths.length > 0 ? Arrays.asList(paths) : Collections.singletonList("");
    }

    private static List<String> combinePaths(List<String> classPaths, List<String> methodPaths) {
        if (methodPaths.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> results = new java.util.ArrayList<>();
        for (String cPath : classPaths) {
            for (String mPath : methodPaths) {
                results.add(formatPath(cPath, mPath));
            }
        }
        return results;
    }

    /**
     * 规范化并拼接路径，处理多余的斜杠或缺失的斜杠
     */
    private static String formatPath(String classPath, String methodPath) {
        String combined = (classPath == null ? "" : classPath.trim()) + "/" + (methodPath == null ? "" : methodPath.trim());
        // 将多个连续斜杠替换为单斜杠：//sapi///resources -> /sapi/resources
        String cleanPath = combined.replaceAll("/+", "/");
        // 去除尾部斜杠（根路径除外）
        if (cleanPath.length() > 1 && cleanPath.endsWith("/")) {
            cleanPath = cleanPath.substring(0, cleanPath.length() - 1);
        }
        // 保证以 / 开头
        if (!cleanPath.startsWith("/")) {
            cleanPath = "/" + cleanPath;
        }
        return cleanPath;
    }

    /**
     * 将包含路径变量的 URI 切割为静态片段数组
     * 例如: "/api/test/{id}/123/{code}" -> ["/api/test/", "/123/"]
     */
    public static String[] splitByPathVariable(String path) {
        if (path == null || path.trim().isEmpty()) {
            return new String[0];
        }

        List<String> segments = new ArrayList<>();
        int length = path.length();
        int cursor = 0;

        while (cursor < length) {
            int openIdx = path.indexOf('{', cursor);
            if (openIdx == -1) {
                // 后面再无任何变量括号，将剩余静态字符全部收集
                String tail = path.substring(cursor);
                if (!tail.isEmpty()) {
                    segments.add(tail);
                }
                break;
            }

            int closeIdx = path.indexOf('}', openIdx);
            if (closeIdx == -1) {
                // 🔥 关键修复点：如果只找到了 '{' 但找不到闭合的 '}'，
                // 说明从当前 cursor 开始都是未闭合的普通字符串，整体加入后直接退出！
                String tail = path.substring(cursor);
                if (!tail.isEmpty()) {
                    segments.add(tail);
                }
                break;
            }

            // 提取 { 前面的静态片段
            if (openIdx > cursor) {
                segments.add(path.substring(cursor, openIdx));
            }

            // 光标跳过路径变量 {...}
            cursor = closeIdx + 1;
        }

        return segments.toArray(new String[0]);
    }
}
