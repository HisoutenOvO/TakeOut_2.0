package com.sky.aspect;


import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect//定义这是一个切面类
@Component//定义这是一个组件类,由spring容器管理
@Slf4j
/**
 * 基于反射的原理逻辑扩展切面类
 */
public class AutoFIllAspect {
    /**
     * 用注解的方式添加切入点
     */
    //定义切面为：com.sky.mapper包下所有的方法，且该方法拥有自定义注解@AutoFill
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * 对扩展方法添加切面，并对被切面的方法进行逻辑扩展
     * @param joinPoint
     */
    //通过@Before注解指定在目标方法执行前执行
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){//可通过joinPoint.getSignature获取方法签对象（可进一步获取其上的注解），也可通过joinPoint.getArgs获取方法参数数组
        log.info("开始进行数据填充");
        //1.获取到当前被拦截到的方法的数据库操作类型
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();//获取方法签对象
        AutoFill annotation = methodSignature.getMethod().getAnnotation(AutoFill.class);//获取方法签对象上的注解对象，此时得到的是注解对象
        OperationType operationType = annotation.value();//获取注解对象中的属性值value，即其中存放着数据库操作类型
        //2.获取到当前被拦截到的方法参数--实体对象Object
        Object[] args =joinPoint.getArgs();
        //防止参数不存在的情况，直接返回
        if(args == null || args.length == 0){
            return;
        }
        Object entity = args[0];//获取实体对象,即args的第一个参数，我们规定这种mapper方法的第一个参数要是com.sky.entity类对象
        //3.准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();//通过线程的存储空间ThreadLocal获取当前登录用户的id
        //4.根据当前不同类型的操作，为对应的属性赋值--通过反射的方式
        if(operationType == OperationType.INSERT){
            //如果是插入语句，则需要更新四个字段，分别是创建人和日期，修改人和日期
            try{
                //基于反射获取时间和ID的get和set方法
                Method setCreateTime = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setCreateUser = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
                //为对应的属性赋值
                setCreateTime.invoke(entity,now);//意为调用entity的setCreateTime方法，并传入now作为参数，而该entity就是
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else if (operationType == OperationType.UPDATE) {
            //如果是更新语句，则需要更新两个字段，分别是修改人和日期
            try{
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
                setUpdateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        log.info("数据填充完毕www");
    }
}
