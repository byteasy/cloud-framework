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
import com.byteasy.common.util.JacksonUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.net.HttpHeaders;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
        JacksonTypeHandler.setObjectMapper(JacksonUtil.copyMapperWithClass());
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

//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer customizer() {
//        return builder -> {
//            builder.configure(JacksonUtil.copy());
//            builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, DeserializationFeature.READ_ENUMS_USING_TO_STRING);
//            builder.serializerByType(Long.class, ToStringSerializer.instance);
//            builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
//        };
//    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = JacksonUtil.copy();
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        SimpleModule longModule = new SimpleModule();
        longModule.addSerializer(Long.class, ToStringSerializer.instance);
        longModule.addSerializer(long.class, ToStringSerializer.instance);
        mapper.registerModule(longModule);
        return mapper;
    }

    @Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(() -> new HttpMessageConverters(new MappingJackson2HttpMessageConverter(JacksonUtil.copy()))));
    }

    @Bean
    public Encoder feignEncoder() {
        return new SpringEncoder(() -> new HttpMessageConverters(
                new MappingJackson2HttpMessageConverter(JacksonUtil.copy())
        ));
    }


    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return resolver;
    }

    @Bean
    public RequestInterceptor localeRequestInterceptor() {
        return template -> {
            // 从 RequestContextHolder 获取当前请求的 Header
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                String locale = request.getParameter(Constant.PARAM_LANG);
                if (!StringUtils.hasText(locale)) {
                    locale = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
                }
                if (!StringUtils.hasText(locale)) {
                    locale = LocaleContextHolder.getLocale().getLanguage();
                }
                template.header(HttpHeaders.ACCEPT_LANGUAGE, locale);
                // 传递其他业务 Header
                String traceId = request.getHeader(Constant.TRACE_ID);
                if (StringUtils.hasText(traceId)) {
                    template.header(Constant.TRACE_ID, traceId);
                }

                String authorization = request.getHeader(Constant.USER_INFO);
                if (StringUtils.hasText(authorization)) {
                    template.header(Constant.USER_INFO, authorization);
                }
            }
        };
    }
}
