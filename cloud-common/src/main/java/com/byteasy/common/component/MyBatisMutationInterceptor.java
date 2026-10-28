package com.byteasy.common.component;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 探查Service是否真正执行了数据库操作，以帮助RepositoryMutationAspect实现实体变更的准确判断
 */
@Component
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class MyBatisMutationInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 执行原本的 SQL
        Object result = invocation.proceed();

        // 获取受影响行数
        int affectedRows = 0;
        if (result instanceof Number) {
            affectedRows = ((Number) result).intValue();
        }

        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        // 记录到当前线程的变更上下文
        MutationContextHolder.get().recordMutation(ms.getId(), affectedRows);

        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
