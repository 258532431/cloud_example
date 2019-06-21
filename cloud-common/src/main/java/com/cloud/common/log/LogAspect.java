package com.cloud.common.log;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.*;
import java.lang.reflect.Method;
import java.util.Date;

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
    @Pointcut("@annotation(com.cloud.common.log.EnableBizLog)")
    public void entryPoint(){
    }

    /**
     * @Author: yangchenglong on 2019/6/10
     * @Description: 在切入点调用前处理
     * update by: 
     * @Param: 
     * @return: 
     */
    @Before("entryPoint()")
    public void doBefore(JoinPoint joinPoint){
    }

    /**
     * @Author: yangchenglong on 2019/6/10
     * @Description: 在切入点调用后处理
     * update by: 
     * @Param: 
     * @return: 
     */
    @After("entryPoint()")
    public void doAfter(JoinPoint joinPoint){
        try {
            String targetClassName = joinPoint.getTarget().getClass().getName();//目标类名
            Signature signature = joinPoint.getSignature();//获取封装了署名信息的对象,在该对象中可以获取到目标方法名,所属类的Class等信息
            String targetMethodName = signature.getName();//目标方法名
            MethodSignature methodSignature = (MethodSignature) signature;
            String[] parameterNames = methodSignature.getParameterNames();//目标参数名列表
            Object[] arguments = joinPoint.getArgs();//目标参数值列表
            StringBuilder params = new StringBuilder();
            for(int i=0; i<parameterNames.length; i++){
                //排除response, request类型参数转json报错
                if(!(arguments[i] instanceof HttpServletResponseWrapper) && !(arguments[i] instanceof HttpServletRequestWrapper) &&
                        !(arguments[i] instanceof HttpServletResponse) && !(arguments[i] instanceof HttpServletRequest)){
                    params.append(parameterNames[i]+"="+ JSON.toJSONString(arguments[i]));
                    params.append("&");
                }
            }
            if(params.length()>0){
                params.replace(params.lastIndexOf("&"), params.length(), "");
            }
            String operateParams = params.toString();//操作内容(入参)

            Class<?> targetClass = Class.forName(targetClassName);//创建目标类对象
            Method[] methods = targetClass.getMethods();//目标类方法集合
            EnableBizLog.OperateModule operateModule = null;//操作模块
            EnableBizLog.OperateType operateType = null;//操作类型
            EnableBizLog.LogLevel logLevel = null;//日志级别
            for(Method method : methods){
                if(method.getName().equals(targetMethodName)){
                    if(method.isAnnotationPresent(EnableBizLog.class)){
                        EnableBizLog bizlog = method.getAnnotation(EnableBizLog.class);
                        if(bizlog != null){
                            operateModule = bizlog.operateModule();// 操作模块
                            operateType = bizlog.operateType();// 操作类型
                            logLevel = bizlog.logLevel();// 日志级别
                        }
                    }
                }
            }

            String ipAddr = "";//ip地址
            String url = "";//请求地址
            String visitDeviceType = EnableBizLog.VisitDeviceType.DEFAULT.getCode();
            String operator = "";//操作人
            String operatorName = "";//操作人姓名
//            String bizId = "";//业务ID，根据实际情况使用
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if(requestAttributes != null){
                HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                ipAddr = LogUtils.getRemoteHost(request);//IP
                url = request.getRequestURI();//请求地址
                if(LogUtils.isMobileDevice(request)){
                    visitDeviceType = EnableBizLog.VisitDeviceType.MOBILE.getCode();
                }else {
                    visitDeviceType = EnableBizLog.VisitDeviceType.PC.getCode();
                }

                HttpSession session = request.getSession();
                operator = (String) session.getAttribute("username");
                operatorName = (String) session.getAttribute("realname");
            }

            //导入操作，操作内容简化记录
            if(EnableBizLog.OperateType.IMPORT.getCode().equals(operateType.getCode())){
                operateParams = "excel-file/image";
            }

            BizLog bizLog = new BizLog();
            bizLog.setOperateClass(targetClassName);
            bizLog.setOperateMethod(targetMethodName);
            bizLog.setOperateModule(operateModule.getCode());
            bizLog.setIp(ipAddr);
            bizLog.setOperateParams(operateParams);
            bizLog.setOperateTime(new Date());
            bizLog.setOperateType(operateType.getCode());
            bizLog.setOperator(operator);
            bizLog.setOperatorName(operatorName);
//            bizLog.setBizId(bizId);
            bizLog.setUrl(url);
            bizLog.setVisitDeviceType(visitDeviceType);

            log.info("业务Log数据发送到mq , bizLog = {}", bizLog);
        } catch (Exception e) {
            log.error("在目标调用后处理异常：{}", e);
        }
    }

    /**
     * @Author: yangchenglong on 2019/6/17
     * @Description: 在切入点调用时拦截
     * update by: 
     * @Param: 
     * @return: 
     */
    @Around("entryPoint()")
    public void around() {}

}
