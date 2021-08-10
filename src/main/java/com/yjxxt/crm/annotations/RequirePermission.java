package com.yjxxt.crm.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 权限控制注解
 */
public @interface RequirePermission {
    String code() default "";
}