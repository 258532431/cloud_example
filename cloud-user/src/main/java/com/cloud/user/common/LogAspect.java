package com.cloud.user.common;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;
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
        log.info("------- 在目标调用前处理 start ---------");
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
        log.info("------- 在目标调用后处理 start ---------");
        try {
            String targetClassName = joinPoint.getTarget().getClass().getName();//目标类名
            Signature signature = joinPoint.getSignature();//获取封装了署名信息的对象,在该对象中可以获取到目标方法名,所属类的Class等信息
            String targetMethodName = signature.getName();//目标方法名
            MethodSignature methodSignature = (MethodSignature) signature;
            String[] parameterNames = methodSignature.getParameterNames();//目标参数名列表
            Object[] arguments = joinPoint.getArgs();//目标参数值列表
            StringBuilder params = new StringBuilder();
            for(int i=0; i<parameterNames.length; i++){
                //排除入参类型为HttpServletResponse、HttpServletRequest时报错
                if(!(arguments[i] instanceof HttpServletResponseWrapper) && !(arguments[i] instanceof HttpServletRequestWrapper)){
                    params.append(parameterNames[i]+"="+ JSON.toJSONString(arguments[i]));
                    params.append("&");
                }
            }
            if(params.length()>0){
                params.replace(params.lastIndexOf("&"), params.length(), "");
            }

            Class<?> targetClass = Class.forName(targetClassName);//创建目标类对象
            Method[] methods = targetClass.getMethods();//目标类方法集合
            String operator = "";//操作人
            LogBiz.OperatingModule operatingModule = null;//操作模块
            String description = "";//操作描述
            LogBiz.LogType logType = null;//日志级别
            for(Method method : methods){
                if(method.getName().equals(targetMethodName)){
                    if(method.isAnnotationPresent(LogBiz.class)){
                        LogBiz logBiz = method.getAnnotation(LogBiz.class);
                        operator = logBiz.operator();// 操作人
                        operatingModule = logBiz.operatingModule();// 操作模块
                        description = logBiz.description();// 操作描述
                        logType = logBiz.level();// 日志级别
                    }
                }
            }

            String ipAddr = "";//ip地址
            String url = "";//请求地址
            String visitDevice = "";
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if(requestAttributes != null){
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                ipAddr = this.getRemoteHost(request);//IP
                url = request.getRequestURI();//请求地址
                if(this.isMobileDevice(request)){
                    visitDevice = LogBiz.VisitDeviceType.MOBILE.getCode();
                }else {
                    visitDevice = LogBiz.VisitDeviceType.PC.getCode();
                }
            }

            log.info("执行 目标类名："+targetClassName+", 目标方法名："+targetMethodName+", 目标参数值："+params.toString());
            log.info("业务 操作人："+operator+", 操作模块："+operatingModule.getName()+", 操作描述："+description+", 日志级别："+logType.name());
        } catch (Exception e) {
            log.error("在目标调用后处理异常：", e);
        }
    }

    //从HttpServletRequest获取传入参数
    /*private String preHandle(MethodSignature methodSignature, HttpServletRequest request) {
        String reqParam = "";
        Method targetMethod = methodSignature.getMethod();
        Annotation[] annotations = targetMethod.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(EnableBizLog.class)) {
                reqParam = JSON.toJSONString(request.getParameterMap());
                break;
            }
        }
        return reqParam;
    }*/

    //获取IP地址
    private String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    //判断是PC还是移动端访问
    private boolean isMobileDevice(HttpServletRequest request){
        //android : 所有android设备, mac os : iphone ipad , windows phone: windows系统的手机
        String[] deviceArray = new String[]{"android","mac os","windows phone"};
        String requestHeader = request.getHeader("user-agent");
        if(StringUtils.isBlank(requestHeader)){
            return false;
        }
        requestHeader = requestHeader.toLowerCase();
        for(int i=0; i<deviceArray.length; i++){
            if(requestHeader.indexOf(deviceArray[i]) > 0){
                return true;
            }
        }
        return false;
    }

}
