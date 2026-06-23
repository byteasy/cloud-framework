package com.byteasy.annotations;

import java.lang.annotation.*;

/**
 * 添加本注解后，方法需要登录后才能访问，如果没加注解，则方法不需要登录就能访问
 * 如果authenticated返回false，则登录就能访问，否则还要有授权后才能访问某项资源
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authentication {

    String name() default "";

    String group() default "";

    boolean authenticated() default false;
}
