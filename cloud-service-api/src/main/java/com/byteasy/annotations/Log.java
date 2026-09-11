package com.byteasy.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface Log {

    /**
     * 是否保存请求的参数
     */
    boolean request() default true;

    /**
     * 是否保存响应的参数，响应参数数据大默认为false
     */
    boolean response() default false;

    /**
     * 排除指定的请求参数
     */
    String[] excludeParamNames() default {};

}
