package com.byteasy.monitor;

import com.google.common.collect.Lists;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map.Entry;

@AutoConfiguration
@ConditionalOnClass(MeterRegistry.class)
@EnableConfigurationProperties(MonitorProperties.class)
public class MonitorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MeterRegistryCustomizer<MeterRegistry> metricsCustomizer(MonitorProperties props, Environment environment) {
        return registry -> {
            // 1. 设置通用标签
            List<Tag> tags = Lists.newArrayList();
            for (Entry<String, String> entry : props.getTags().entrySet()) {
                tags.add(Tag.of(entry.getKey(), entry.getValue()));
            }
            registry.config().commonTags(tags);

            // 2. 添加过滤器
            registry.config()
                    // 忽略敏感标签
                    .meterFilter(MeterFilter.ignoreTags("password", "secret"))
                    // 限制每个指标的标签数量
                    .meterFilter(new MeterFilter() {
                        @Override
                        public Meter.Id map(Meter.Id id) {
                            if (id.getTags().size() > props.getMaxTagsPerMetric()) {
                                throw new IllegalArgumentException(
                                        "Too many tags for metric '" + id.getName() +
                                                "'. Maximum is " + props.getMaxTagsPerMetric());
                            }
                            return id;
                        }
                    })
                    // 限制标签值的基数
                    .meterFilter(MeterFilter.maximumAllowableTags(
                            props.getMetricNameForCardinalityLimit(),
                            props.getTagKeyForCardinalityLimit(),
                            props.getMaxTagCardinality(),
                            MeterFilter.deny()
                    ));
        };
    }

    @Bean
    public MonitorFacade monitorFacade(MeterRegistry registry) {
        return new MonitorFacade(registry);
    }
}
