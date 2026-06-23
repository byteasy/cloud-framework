package com.byteasy.monitor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Monitored {

    String value();

    String[] tags() default {};
}
