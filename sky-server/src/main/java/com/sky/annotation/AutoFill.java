package com.sky.annotation;

import com.sky.enumeration.OperationType;
import jdk.dynalink.Operation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于辅助AOP进行全局公共字段填充
 */
@Target(ElementType.METHOD)//使用注解定义该注释适用于方法上的
@Retention(RetentionPolicy.RUNTIME)//定义注解是运行时启用的
public @interface AutoFill {
    //定义注解中的参数，是数据库的操作类型，为INSERT和UPDATE
    OperationType value();
}
