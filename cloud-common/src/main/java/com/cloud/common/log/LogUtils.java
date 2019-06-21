package com.cloud.common.log;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @program: cloud_example
 * @description: 日志工具类
 * @author: yangchenglong
 * @create: 2019-06-10 18:55
 */
public class LogUtils {

    /**
     * @Author: yangchenglong on 2019/6/10
     * @Description: 设置注解中的字段值
     * update by:
     * @Param: clz 业务类、methodName 业务类方法名、fieldValues  Map<注解字段名, 注解的值>、paramTypes 业务方法的参数类型
     * @return:
     */
    public static void setAnnotationValue(Class<?> clz, String methodName, Map<String, Object> fieldValues, Class<?> paramTypes){
        try {
            Method method = clz.getMethod(methodName, paramTypes);////获取业务方法
            EnableBizLog logBiz = method.getAnnotation(EnableBizLog.class);////获取此业务方法上的注解实例
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(logBiz);//获取这个代理实例所持有的 InvocationHandler
            Field declaredField = invocationHandler.getClass().getDeclaredField("memberValues");
            declaredField.setAccessible(true);//如果是private final 修饰，要打开权限
            Map memberValues = (Map) declaredField.get(invocationHandler);
            for(String fieldName : fieldValues.keySet()){
                Object fieldValue = fieldValues.get(fieldName);
                memberValues.put(fieldName, fieldValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取IP地址
    public static String getRemoteHost(HttpServletRequest request) {
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
        if(!StringUtils.isBlank(ip)){
            ip = ip.split(",")[0];
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    //判断是PC还是移动端访问
    public static boolean isMobileDevice(HttpServletRequest request){
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
