package com.byteasy.monitor;

import com.byteasy.MetricConstant;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.byteasy.MetricConstant.HTTP_ERRORS_TOTAL;
import static com.byteasy.MetricConstant.HTTP_LATENCY;

@Aspect
@Component
@ConditionalOnBean(HttpMetricsAutoConfiguration.class) // 确保配置类已加载
public class ControllerMetricsAspect {

    private final MonitorFacade monitorFacade;

    // 支持的RequestMapping注解类型
    private static final List<Class<? extends Annotation>> MAPPING_ANNOTATIONS = Arrays.asList(
            GetMapping.class, PostMapping.class, PutMapping.class,
            DeleteMapping.class, PatchMapping.class, RequestMapping.class
    );

    public ControllerMetricsAspect(MonitorFacade monitorFacade) {
        this.monitorFacade = monitorFacade;
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void mappingAnnotationPointcut() {
    }

    // 环绕通知
    @Around("mappingAnnotationPointcut()")
    public Object measureMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取请求路径和方法
        String path = getRequestPath(joinPoint, method);
        String httpMethod = getHttpMethod(method);

        monitorFacade.counter(MetricConstant.HTTP_REQUESTS_TOTAL, "method", httpMethod, "path", path).increment();

        // 计时器
        Timer.Sample sample = Timer.start();
        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            // 记录错误数
            monitorFacade.counter(HTTP_ERRORS_TOTAL, "method", httpMethod, "path", path).increment();
            throw e;
        } finally {
            // 获取响应状态（需要从HttpServletResponse中获取）
            // 注意：这里简化处理，实际可能需要更复杂的逻辑获取状态码
            sample.stop(monitorFacade.timer(HTTP_LATENCY,
                    "method", httpMethod,
                    "path", path));
        }
    }

    // 获取请求路径
    private String getRequestPath(ProceedingJoinPoint joinPoint, Method method) {
        // 获取类级别的路径
        String classPath = "";
        RequestMapping classMapping = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
        if (classMapping != null) {
            if (classMapping.value().length > 0) {
                classPath = normalizePath(classMapping.value()[0]);
            } else if (classMapping.path().length > 0) {
                classPath = normalizePath(classMapping.path()[0]);
            }
        }

        // 获取方法级别的路径
        String methodPath = getMethodPath(method);

        // 组合路径
        return combinePaths(classPath, methodPath);
    }

    /**
     * 获取方法级别的路径
     */
    private String getMethodPath(Method method) {
        // 检查所有映射注解
        for (Class<? extends Annotation> annotationClass : MAPPING_ANNOTATIONS) {
            Annotation annotation = method.getAnnotation(annotationClass);
            if (annotation != null) {
                try {
                    // 动态获取value或path属性
                    Method valueMethod = annotation.getClass().getMethod("value");
                    String[] values = (String[]) valueMethod.invoke(annotation);
                    if (values.length > 0) {
                        return normalizePath(values[0]);
                    }

                    Method pathMethod = annotation.getClass().getMethod("path");
                    String[] paths = (String[]) pathMethod.invoke(annotation);
                    if (paths.length > 0) {
                        return normalizePath(paths[0]);
                    }
                } catch (Exception e) {
                    // 忽略异常，继续检查其他注解
                }
            }
        }

        // 默认返回空路径
        return "";
    }

    /**
     * 规范化路径 - 确保路径以/开头且不以/结尾
     */
    private String normalizePath(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }

        // 去除前后空格
        path = path.trim();

        // 确保以/开头
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        // 去除结尾的/
        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }

    /**
     * 组合类路径和方法路径
     */
    private String combinePaths(String classPath, String methodPath) {
        if (classPath.isEmpty()) {
            return methodPath.isEmpty() ? "/" : methodPath;
        }

        if (methodPath.isEmpty()) {
            return classPath;
        }

        // 处理重复的斜杠
        if (classPath.endsWith("/") && methodPath.startsWith("/")) {
            return classPath + methodPath.substring(1);
        } else if (!classPath.endsWith("/") && !methodPath.startsWith("/")) {
            return classPath + "/" + methodPath;
        } else {
            return classPath + methodPath;
        }
    }

    // 获取HTTP方法
    private String getHttpMethod(Method method) {
        // 检查特定HTTP方法注解
        if (method.isAnnotationPresent(GetMapping.class)) {
            return "GET";
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            return "POST";
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            return "PUT";
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            return "DELETE";
        } else if (method.isAnnotationPresent(PatchMapping.class)) {
            return "PATCH";
        }

        // 检查通用的RequestMapping注解
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping != null && requestMapping.method().length > 0) {
            return requestMapping.method()[0].name();
        }

        // 默认返回GET
        return "GET";
    }
}
