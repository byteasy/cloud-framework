package com.byteasy.common.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型类型解析工具类
 */
public class GenericTypeUtil {

    /**
     * 获取抽象父类Entity的泛型ID具体类型
     *
     * @param entityClass 子类Class（如User.class）
     * @return 泛型ID的具体类型（如Long.class）；若未指定泛型，返回null
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getEntityIdType(Class<?> entityClass) {
        // 1. 防御性判空
        if (entityClass == null) {
            return null;
        }

        // 2. 获取泛型父类（处理多层继承场景）
        Type genericSuperclass = entityClass.getGenericSuperclass();
        // 递归解析：若父类不是ParameterizedType（泛型类型），继续向上找Entity父类
        while (!(genericSuperclass instanceof ParameterizedType)) {
            // 退出条件：已找到Object类，无泛型
            if (genericSuperclass == Object.class) {
                return null;
            }
            // 处理非泛型父类，继续向上获取
            genericSuperclass = ((Class<?>) genericSuperclass).getGenericSuperclass();
        }

        // 3. 解析泛型参数（Entity<ID>只有1个泛型参数，索引为0）
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments.length == 0) {
            return null;
        }

        // 4. 转换为具体Class类型并返回
        Type idType = actualTypeArguments[0];
        if (idType instanceof Class<?>) {
            return (Class<T>) idType;
        }
        return null;
    }
}
