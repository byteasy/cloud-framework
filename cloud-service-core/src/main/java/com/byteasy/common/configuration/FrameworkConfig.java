package com.byteasy.common.configuration;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.byteasy.Constant;
import com.byteasy.common.token.UserInfoBuilder;
import com.byteasy.common.util.JacksonUtilOld;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.net.HttpHeaders;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
@Log4j2
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
        JacksonTypeHandler.setObjectMapper(JacksonUtilOld.copy());
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

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = JacksonUtilOld.copy();
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        SimpleModule longModule = new SimpleModule();
        longModule.addSerializer(Long.class, ToStringSerializer.instance);
        longModule.addSerializer(long.class, ToStringSerializer.instance);
        mapper.registerModule(longModule);
        return mapper;
    }


    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return resolver;
    }

    @Bean
    public ClientHttpRequestInterceptor localeClientRequestInterceptor() {
        return (httpRequest, body, execution) -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                // 1. 语言传递
                String locale = request.getParameter(Constant.PARAM_LANG);
                if (!StringUtils.hasText(locale)) {
                    locale = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
                }
                if (!StringUtils.hasText(locale)) {
                    Locale current = LocaleContextHolder.getLocale();
                    locale = (current != null) ? current.getLanguage() : null;
                }
                if (StringUtils.hasText(locale)) {
                    httpRequest.getHeaders().set(HttpHeaders.ACCEPT_LANGUAGE, locale);
                }

                // 2. TraceId 传递
                String traceId = request.getHeader(Constant.TRACE_ID);
                if (StringUtils.hasText(traceId)) {
                    httpRequest.getHeaders().set(Constant.TRACE_ID, traceId);
                }

                // 3. UserInfo 传递
                String authorization = request.getHeader(Constant.USER_INFO);
                if (StringUtils.hasText(authorization)) {
                    httpRequest.getHeaders().set(Constant.USER_INFO, authorization);
                } else {
                    httpRequest.getHeaders().set(Constant.USER_INFO, UserInfoBuilder.builderByMicroService(SpringUtil.getApplicationName(), SpringUtil.getApplicationName()));
                }
            }

            return execution.execute(httpRequest, body);
        };
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }
}
