package com.byteasy.monitor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class MonitoredAspect {

    private final MonitorFacade facade;

    public MonitoredAspect(MonitorFacade facade) {
        this.facade = facade;
    }

    @Around("@annotation(monitored)")
    public Object monitor(ProceedingJoinPoint pjp, Monitored monitored) throws Throwable {
        String metricName = monitored.value();
        String[] tags = monitored.tags();

        try {
            return facade.timer(metricName, tags).record(() -> {
                try {
                    return pjp.proceed();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            facade.counter(metricName + ".errors", tags).increment();
            throw e;
        }
    }
}
