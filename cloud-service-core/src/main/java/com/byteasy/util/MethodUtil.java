package com.byteasy.util;

import cn.hutool.core.util.ReflectUtil;
import com.byteasy.common.controller.AbstractController;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.pulsar.shade.org.checkerframework.common.reflection.qual.GetMethod;
import org.springframework.aop.support.AopUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.*;

public class MethodUtil {

    public static List<Map<String, String>> listMappingMethod(AbstractController controller) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Method[] methods = ReflectUtil.getPublicMethods(AopUtils.getTargetClass(controller));
        List<Method> methodList = Arrays.stream(methods).filter(MethodUtil::isMappingMethod).toList();
        for (Method method : methodList) {
            list.add(toMap(controller, method));
        }
        return list;
    }

    private static boolean isMappingMethod(Method method) {
        return method.getAnnotation(GetMethod.class) != null || method.getAnnotation(PutMapping.class) != null || method.getAnnotation(PostMapping.class) != null || method.getAnnotation(DeleteMapping.class) != null;
    }

    private static Map<String, String> toMap(AbstractController controller, Method method) {
        Map<String, String> map = new HashMap<String, String>();

        GetMapping getMapping = MethodUtils.getAnnotation(method, GetMapping.class, true, true);
        if (getMapping != null) {
            map.put("httpMethod", HttpMethod.GET.name());
            map.put("uri", getUri(controller, method, getMapping.value()));
        }
        PostMapping postMapping = MethodUtils.getAnnotation(method, PostMapping.class, true, true);
        if (postMapping != null) {
            map.put("httpMethod", HttpMethod.POST.name());
            map.put("uri", getUri(controller, method, postMapping.value()));
        }
        PutMapping putMapping = MethodUtils.getAnnotation(method, PutMapping.class, true, true);
        if (putMapping != null) {
            map.put("httpMethod", HttpMethod.PUT.name());
            map.put("uri", getUri(controller, method, putMapping.value()));
        }
        DeleteMapping deleteMapping = MethodUtils.getAnnotation(method, DeleteMapping.class, true, true);
        if (deleteMapping != null) {
            map.put("httpMethod", HttpMethod.DELETE.name());
            map.put("uri", getUri(controller, method, deleteMapping.value()));
        }
        map.put("classMethod", getMethodName(controller, method));
        Operation operation = MethodUtils.getAnnotation(method, Operation.class, true, true);
        if (operation != null && StringUtils.isNotEmpty(operation.summary())) {
            map.put("name", operation.summary());
        } else {
            map.put("name", method.getName());
        }
        return map;
    }


    private static String getUri(AbstractController targetController, Method method, String[] path) {
        Class<?> cls = AopUtils.getTargetClass(targetController);
        RequestMapping requestMapping = cls.getAnnotation(RequestMapping.class);
        if (requestMapping == null)
            return null;
        return SpringContextUtil.getContextPath() + requestMapping.value()[0] + (path == null || path.length == 0 ? "" : path[0]);
    }

    private static String getMethodName(AbstractController controller, Method method) {
        Class<?> cls = AopUtils.getTargetClass(controller);
        return cls.getName() + "." + method.getName();
    }
}
