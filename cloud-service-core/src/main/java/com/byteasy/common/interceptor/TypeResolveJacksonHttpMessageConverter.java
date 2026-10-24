package com.byteasy.common.interceptor;

import com.byteasy.api.IBaseEntityService;
import org.jspecify.annotations.Nullable;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支持透传上下文具体泛型类型的 Jackson 转换器
 * 确保在收到 HTTP 响应的第一时间，直接流式反序列化为目标实体，避免二次转换
 */
public class TypeResolveJacksonHttpMessageConverter extends JacksonJsonHttpMessageConverter {
    // 使用 Java 9+ 高性能栈遍历器（只保留 Class 引用）
    private static final StackWalker STACK_WALKER =
            StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    // 缓存 Client 接口对应的实体类型 Class，避免重复反射计算
    private static final Map<Class<?>, Class<?>> ENTITY_TYPE_CACHE = new ConcurrentHashMap<>();

    public TypeResolveJacksonHttpMessageConverter(JsonMapper jsonMapper) {
        super(jsonMapper);
    }

    @Override
    public Object read(ResolvableType type, HttpInputMessage inputMessage, @Nullable Map<String, Object> hints)
            throws IOException, HttpMessageNotReadableException {

        // 1. 如果类型完全已知（如 getByCode 返回具体类型，或 removeById 返回 Boolean），直接走原生反序列化，零性能损耗
        if (!type.hasUnresolvableGenerics()) {
            return super.read(type, inputMessage, hints);
        }

        // 2. 存在未解析泛型变量（如 Result<ENTITY>、Result<Page<ENTITY>>），从调用栈回溯查找具体的 Client 接口
        Class<?> clientInterface = findClientInterfaceFromStack();
        Class<?> entityClass = null;
        if (clientInterface != null) {
            entityClass = ENTITY_TYPE_CACHE.computeIfAbsent(clientInterface, this::resolveEntityType);
        }

        JsonMapper jsonMapper = defaultMapper;
        // 3. 将泛型中的未绑定变量替换为具体的实体类（如 Openapi.class），并直接流式反序列化
        JavaType javaType = toJavaType(type, jsonMapper.getTypeFactory(), entityClass);
        return jsonMapper.readValue(inputMessage.getBody(), javaType);
    }

    /**
     * 回溯调用栈：寻找栈中由 Spring 生成的、实现了 IBaseEntityService 的 $Proxy 代理对象
     */
    private Class<?> findClientInterfaceFromStack() {
        return STACK_WALKER.walk(frames ->
                frames.map(StackWalker.StackFrame::getDeclaringClass)
                        .filter(Proxy::isProxyClass) // 仅筛选动态代理类
                        .flatMap(proxyClass -> Arrays.stream(proxyClass.getInterfaces()))
                        .filter(IBaseEntityService.class::isAssignableFrom) // 找到继承了 IBaseEntityService 的接口（如 OpenapiClient）
                        .findFirst()
                        .orElse(null)
        );
    }

    /**
     * 从子接口（如 OpenapiClient）中提取 IBaseEntityService<ID, ENTITY> 的 ENTITY 具体类型
     */
    private Class<?> resolveEntityType(Class<?> clientInterface) {
        ResolvableType baseType = ResolvableType.forClass(clientInterface).as(IBaseEntityService.class);
        // index 1 对应 IBaseEntityService<ID, ENTITY> 中的 ENTITY
        return baseType.getGeneric(1).resolve();
    }

    /**
     * 递归构造 Jackson 3 的 JavaType，将 TypeVariable 替换为真实的实体类
     */
    private JavaType toJavaType(ResolvableType type, TypeFactory typeFactory, @Nullable Class<?> concreteEntityClass) {
        // 如果是未绑定的泛型变量（例如 ENTITY），且我们成功从调用栈拿到了具体类型，直接替换
        if (type.getType() instanceof TypeVariable && concreteEntityClass != null) {
            return typeFactory.constructType(concreteEntityClass);
        }

        Class<?> rawClass = type.toClass();
        ResolvableType[] generics = type.getGenerics();

        if (generics.length == 0) {
            return typeFactory.constructType(type.getType());
        }

        // 递归处理嵌套泛型，例如 Result<Page<ENTITY>> 或 Result<List<ENTITY>>
        JavaType[] genericJavaTypes = new JavaType[generics.length];
        for (int i = 0; i < generics.length; i++) {
            genericJavaTypes[i] = toJavaType(generics[i], typeFactory, concreteEntityClass);
        }

        return typeFactory.constructParametricType(rawClass, genericJavaTypes);
    }
}
