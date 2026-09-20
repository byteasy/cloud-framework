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

    /**
     * 接口所在的领域对象
     *
     * @return
     */
    Class<?> entity() default Void.class;

    /**
     * 接口所在的领域对象
     *
     * @return
     */
    String entityName() default "";

    /**
     * 接口路径定义，适用于通用接口，拥有最高优先级
     *
     * @return
     */
    String uri() default "";
}
