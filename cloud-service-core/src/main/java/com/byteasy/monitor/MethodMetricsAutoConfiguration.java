package com.byteasy.monitor;


import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@AutoConfiguration(after = MonitorAutoConfiguration.class)
@ConditionalOnProperty(name = "monitor.method.enabled", havingValue = "true", matchIfMissing = true)
@EnableAspectJAutoProxy
public class MethodMetricsAutoConfiguration {

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public MonitoredAspect monitoredAspect(MonitorFacade facade) {
        return new MonitoredAspect(facade);
    }
}
