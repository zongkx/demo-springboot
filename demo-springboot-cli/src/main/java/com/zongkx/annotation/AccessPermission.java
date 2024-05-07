package com.zongkx.annotation;

import com.fasterxml.jackson.databind.JsonNode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zongkxc
 * @Description
 * @create 2021/5/31  15:01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface AccessPermission {
    String value() default "";
    String type();
    String path();
    Class<?> voClass() default JsonNode.class;
}
