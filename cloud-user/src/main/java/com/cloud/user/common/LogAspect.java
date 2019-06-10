package com.cloud.user.common;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @program: cloud_example
 * @description: 日志切面类
 * @author: yangchenglong
 * @create: 2019-06-10 16:43
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    /**
     * @Author: yangchenglong on 2019/6/10
     * @Description: 定义切入点
     * update by:
     * @Param:
     * @return:
     */
    @Pointcut("@annotation(com.cloud.user.common.LogBiz)")
    public void entryPoint(){
        //无需内容
    }

    /**
     * @Author: yangchenglong on 2019/6/10
     * @Description: 在目标调用前处理
     * update by: 
     * @Param: 
     * @return: 
     */
    @Before("entryPoint()")
    public void doBefore(JoinPoint joinPoint){
        log.info("------- 在目标调用前处理 ---------");
        try {

        } catch (Exception e) {
            log.error("在目标调用前处理异常：", e);
        }
    }

    /**
     * @Author: yangchenglong on 2019/6/10
     * @Description: 在目标调用后处理
     * update by: 
     * @Param: 
     * @return: 
     */
    @After("entryPoint()")
    public void doAfter(JoinPoint joinPoint){
        log.info("------- 在目标调用后处理 ---------");
        try {
            String targetClassName = joinPoint.getTarget().getClass().getName();//目标类名
            String targetMethodName = joinPoint.getSignature().getName();//目标方法名

            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            //2.最关键的一步:通过这获取到方法的所有参数名称的字符串数组
            String[] parameterNames = methodSignature.getParameterNames();//目标参数
            Object[] arguments = joinPoint.getArgs();//目标参数值
            StringBuilder params = new StringBuilder();
            for(int i=0; i<parameterNames.length; i++){
                params.append(parameterNames[i]+"="+arguments[i]);
                params.append("&");
            }
            if(params.length()>0){
                params.replace(params.lastIndexOf("&"), params.length(), "");
            }

            Class<?> targetClass = Class.forName(targetClassName);//目标类
            Method[] methods = targetClass.getMethods();//目标类方法
            String operator = "";//操作人
            LogBiz.OperatingModule operatingModule = null;//操作模块
            LogBiz.LogType logType = null;//日志级别
            for(Method method : methods){
                if(method.getName().equals(targetMethodName)){
                    Class<?>[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        operator = method.getAnnotation(LogBiz.class).operator();// 操作人
                        operatingModule = method.getAnnotation(LogBiz.class).operatingModule();// 操作模块
                        logType = method.getAnnotation(LogBiz.class).level();// 日志级别
                    }
                }
            }

            log.info("执行 目标类名："+targetClassName+", 目标方法名："+targetMethodName+", 目标参数值："+params.toString());
            log.info("业务 操作人："+operator+", 操作模块："+operatingModule.getName()+", 日志级别："+logType.name());
        } catch (Exception e) {
            log.error("在目标调用后处理异常：", e);
        }
    }

}
