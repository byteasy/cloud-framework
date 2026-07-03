package com.byteasy.xxljob;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        name = "xxlJob.enable",
        havingValue = "true",
        matchIfMissing = false
)
public class XxlJobConfiguration {

    private Logger logger = LogManager.getLogger();
    @Value("${xxl.job.accessToken:default_token}")
    private String accessToken;
    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;
    @Value("${xxl.job.executor.appName}")
    private String appName;
    @Value("${xxl.job.executor.port:9999}")
    private Integer port;
    @Value("${xxl.job.executor.logPath}")
    private String logPath;
    @Value("${xxl.job.executor.logRetentionDays:15}")
    private Integer logRetentionDays;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        logger.info("XxlJobExecutor init");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appName);
//    xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        return xxlJobSpringExecutor;
    }
}
