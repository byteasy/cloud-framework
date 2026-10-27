package com.byteasy.common.component;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class EntityResolver {

    private static final Map<String, Class<?>> STATEMENT_ENTITY_CACHE = new ConcurrentHashMap<>();

    /**
     * 根据 MyBatis MappedStatement Id 解析出对应的实体 Class
     */
    public static Class<?> resolveEntityClass(String statementId) {
        if (statementId == null || statementId.isEmpty()) {
            return null;
        }

        return STATEMENT_ENTITY_CACHE.computeIfAbsent(statementId, id -> {
            try {
                int lastDot = id.lastIndexOf('.');
                if (lastDot == -1) {
                    return null;
                }
                String mapperClassName = id.substring(0, lastDot);
                Class<?> mapperClass = Class.forName(mapperClassName);

                // 使用 Spring 的 ResolvableType 解析 BaseMapper<T> 的第 0 个泛型
                // 天然支持多级继承，例如: UserMapper -> MyBaseMapper<User> -> BaseMapper<User>
                ResolvableType resolvableType = ResolvableType.forClass(mapperClass).as(BaseMapper.class);
                return resolvableType.getGeneric(0).resolve();
            } catch (ClassNotFoundException e) {
                // 自定义 statementId 或没有映射 mapperClass 的情况
                return null;
            } catch (Exception e) {
                log.debug("Resolve entity class failed for statement: {}", id, e);
                return null;
            }
        });
    }
}
