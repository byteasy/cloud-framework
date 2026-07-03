package com.byteasy.monitor;

import io.micrometer.core.instrument.*;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

public class MonitorFacade {
    private final MeterRegistry registry;
    private final ConcurrentMap<MetricKey, Meter> cache = new ConcurrentHashMap<>();

    public MonitorFacade(MeterRegistry registry) {
        this.registry = registry;
    }

    // 通用获取或创建方法
    private <T extends Meter> T getOrCreate(
            String name,
            Function<MeterRegistry, T> meterSupplier,
            String... tags) {

        MetricKey key = new MetricKey(name, tags);
        return (T) cache.computeIfAbsent(key, k -> meterSupplier.apply(registry));
    }

    // 计数器快捷方法
    public Counter counter(String name, String... tags) {
        return getOrCreate(name,
                reg -> Counter.builder(name)
                        .tags(tags)
                        .register(reg),
                tags);
    }

    // 计时器快捷方法
    public Timer timer(String name, String... tags) {
        return getOrCreate(name,
                reg -> Timer.builder(name)
                        .tags(tags)
                        .publishPercentiles(0.5, 0.95)
                        .register(reg),
                tags);
    }

    // 计量器快捷方法
    public <T> Gauge gauge(String name, T obj, ToDoubleFunction<T> f, String... tags) {
        return getOrCreate(name,
                reg -> Gauge.builder(name, obj, f)
                        .tags(tags)
                        .register(reg),
                tags);
    }

    // 分布摘要快捷方法
    public DistributionSummary summary(String name, String... tags) {
        return getOrCreate(name,
                reg -> DistributionSummary.builder(name)
                        .tags(tags)
                        .register(reg),
                tags);
    }

    // 记录执行时间(返回结果)
    public <T> T record(String timerName, Supplier<T> supplier, String... tags) {
        return timer(timerName, tags).record(supplier);
    }

    // 记录执行时间(无返回值)
    public void record(String timerName, Runnable runnable, String... tags) {
        timer(timerName, tags).record(runnable);
    }

    // 指标键
    record MetricKey(String name, String[] tags) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MetricKey metricKey = (MetricKey) o;
            return name.equals(metricKey.name) && Arrays.equals(tags, metricKey.tags);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + Arrays.hashCode(tags);
            return result;
        }
    }
}