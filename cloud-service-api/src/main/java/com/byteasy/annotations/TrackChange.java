package com.byteasy.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TrackChange {
    /**
     * 业务模块名称或自定义标识
     */
    String value() default "";
}
