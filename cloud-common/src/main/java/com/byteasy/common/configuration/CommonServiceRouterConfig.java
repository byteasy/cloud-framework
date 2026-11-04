package com.byteasy.common.configuration;

import com.byteasy.common.component.EntityDefineLoadingCache;
import com.byteasy.common.service.ICommonService;
import com.byteasy.dto.MethodInvokeDTO;
import com.byteasy.platform.entity.EntityDefine;
import com.byteasy.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class CommonServiceRouterConfig {

    @Value("${spring.application.name}")
    private String currentServiceName;

    @Autowired
    @Qualifier("localCommonServiceImpl")
    private ICommonService localCommonService;

    @Autowired
    private EntityDefineLoadingCache entityDefineLoadingCache;

    @Autowired
    private RestClient.Builder restClientBuilder;

    @Bean
    @Primary // 关键：标记为首选 Bean，CommonServiceFunction 会自动注入这个代理对象！
    public ICommonService commonServiceRouter() {
        return (ICommonService) Proxy.newProxyInstance(
                ICommonService.class.getClassLoader(),
                new Class<?>[]{ICommonService.class},
                new CommonServiceInvocationHandler()
        );
    }

    private class CommonServiceInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 处理 Object 基础方法 (toString, hashCode, equals 等)
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }

            // 1. 获取 className (约定第一个参数为 className)
            String className = null;
            if (args != null && args.length > 0 && args[0] instanceof String) {
                className = (String) args[0];
            }

            // 2. 如果方法不含 className（如特殊方法），走本地降级
            if (className == null) {
                return method.invoke(localCommonService, args);
            }

            // 3. 从缓存/数据库中读取 EntityDefine 获取归属微服务
            EntityDefine define = entityDefineLoadingCache.load(className);
            String targetService = (define != null) ? define.getCode() : null;

            // 4. 判断是否是本地服务
            // 如果未配置所属微服务，或者所属微服务就是当前应用，走本地执行
            if (targetService == null || targetService.equalsIgnoreCase(currentServiceName)) {
                log.debug("本地处理实体 [{}] 的调用: {}", className, method.getName());
                return method.invoke(localCommonService, args);
            }

            // 5. 跨微服务远程调用
            log.info("路由实体 [{}] 到远程微服务 [{}] 执行: {}", className, targetService, method.getName());
            return invokeRemoteService(targetService, method, className, args);
        }

        /**
         * 借助 Spring Cloud RestClient + Nacos 进行远程泛化调用
         */
        private Object invokeRemoteService(String targetService, Method method, String className, Object[] args) {
            List<Object> argList = (args != null) ? Arrays.asList(args) : List.of();
            MethodInvokeDTO requestDTO = new MethodInvokeDTO(method.getName(), className, argList);

            // 借助 Nacos 服务名直接发起负载均衡调用
            String url = "http://" + targetService + "/sapi/inner/service/invoke";

            Class<?> returnType = method.getReturnType();
            if (void.class.equals(returnType)) {
                restClientBuilder.build()
                        .post()
                        .uri(url)
                        .body(JacksonUtil.toJson(requestDTO, true))
                        .retrieve()
                        .toBodilessEntity();
                return null;
            } else {
                String json = restClientBuilder.build()
                        .post()
                        .uri(url)
                        .body(JacksonUtil.toJson(requestDTO, true))
                        .retrieve()
                        .body(String.class);
                return JacksonUtil.parseObj(json);
            }
        }
    }
}
