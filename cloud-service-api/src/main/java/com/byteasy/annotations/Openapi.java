package com.byteasy.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Openapi {

    String title() default "";

    @AliasFor("title")
    String summary() default "";
}
