package com.byteasy.monitor;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = MonitorAutoConfiguration.class)
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "monitor.http.enabled", havingValue = "true", matchIfMissing = true)
public class HttpMetricsAutoConfiguration {

    @Bean
    public ControllerMetricsAspect controllerMetricsAspect(MonitorFacade monitorFacade) {
        return new ControllerMetricsAspect(monitorFacade);
    }
}
