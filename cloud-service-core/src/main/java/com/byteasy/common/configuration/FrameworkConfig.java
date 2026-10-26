package com.byteasy.common.configuration;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import com.baomidou.mybatisplus.extension.handlers.Jackson3TypeHandler;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.byteasy.common.interceptor.TypeResolveJacksonHttpMessageConverter;
import com.byteasy.common.util.HttpUtil;
import com.byteasy.common.util.JacksonUtil;
import com.byteasy.common.util.JacksonUtil2;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.util.Locale;

@Configuration
@Log4j2
@EnableCaching
public class FrameworkConfig {
    @Bean
    public AuditEntityHandler auditEntityHandler() {
        return new AuditEntityHandler();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 如果有多数据源可以不配具体类型, 否则都建议配上具体的 DbType
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor()); // 如果配置多个插件, 切记分页最后添加
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor(true));
        DynamicTableNameInnerInterceptor tableNameInnerInterceptor = new DynamicTableNameInnerInterceptor(new TableNameHandler() {
            @Override
            public String dynamicTableName(String sql, String tableName) {
                return tableName;
            }
        });
        interceptor.addInnerInterceptor(tableNameInnerInterceptor);
        // 添加非法SQL拦截器
//    interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        // 数据权限
        try {
            DataPermissionHandler dataPermissionHandler = SpringUtil.getBean(DataPermissionHandler.class);
            if (dataPermissionHandler != null) {
                interceptor.addInnerInterceptor(new DataPermissionInterceptor(dataPermissionHandler));
            }
        } catch (Exception e) {
            log.warn("DataPermissionHandler not define! the DataPermissionInterceptor not enable.");
        }
        JacksonTypeHandler.setObjectMapper(JacksonUtil2.copyMapperWithClass());
        Jackson3TypeHandler.setObjectMapper(JacksonUtil.getObjectMapper());
        return interceptor;
    }

    @Bean
    public MybatisPlusPropertiesCustomizer mybatisPlusPropertiesCustomizer() {
        return properties -> {
            GlobalConfig globalConfig = properties.getGlobalConfig();
            globalConfig.setBanner(false);
            MybatisPlusProperties.CoreConfiguration configuration = properties.getConfiguration();
            if (configuration == null) {
                configuration = new MybatisPlusProperties.CoreConfiguration();
            }
            configuration.setDefaultEnumTypeHandler(MybatisEnumTypeHandler.class);
            properties.setConfiguration(configuration);
        };
    }


    /**
     * Sequence主键自增
     *
     * @return 返回oracle自增类
     * @author zhenggc
     * @date 2019/1/2
     */
    @Bean
    @ConditionalOnClass(name = "oracle.jdbc.driver.OracleDriver")
    public OracleKeyGenerator oracleKeyGenerator() {
        return new OracleKeyGenerator();
    }

    /**
     * 部分框架未完全切换至jackson3，如：springdoc，保留jackson2的配置
     */
    @Deprecated
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = JacksonUtil2.copy();
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        SimpleModule longModule = new SimpleModule();
        longModule.addSerializer(Long.class, ToStringSerializer.instance);
        longModule.addSerializer(long.class, ToStringSerializer.instance);
        mapper.registerModule(longModule);
        return mapper;
    }

    /**
     * 注册全局默认的 JsonMapper (Jackson 3)
     */
    @Bean
    @Primary
    public JsonMapper defaultJsonMapper() {
        return JacksonUtil.getObjectMapper();
    }

    /**
     * 注册 Spring 7 / Spring Boot 4 的 HttpMessageConverter
     * 确保 @RestController 接口入参和返回统一走该 JsonMapper
     */
    @Bean
    @Primary
    public JacksonJsonHttpMessageConverter jacksonJsonHttpMessageConverter(JsonMapper jsonMapper) {
        // 注意：Spring 7 弃用了旧的 MappingJackson2HttpMessageConverter
        // 全面改用 org.springframework.http.converter.json.JacksonJsonHttpMessageConverter
        return new JacksonJsonHttpMessageConverter(jsonMapper);
    }


    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return resolver;
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Bean
    public JdkClientHttpRequestFactory jdkClientHttpRequestFactory() {
        // 2. 包装为 Spring 客户端工厂
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(HttpUtil.getHttpClient());
        // 单次请求数据读取超时
        factory.setReadTimeout(Duration.ofSeconds(30));
        return factory;
    }

    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder(JdkClientHttpRequestFactory jdkClientHttpRequestFactory) {
        return RestClient.builder().requestFactory(jdkClientHttpRequestFactory).configureMessageConverters(c -> c.registerDefaults().withJsonConverter(new TypeResolveJacksonHttpMessageConverter(JacksonUtil.getObjectMapper())));
    }
}
