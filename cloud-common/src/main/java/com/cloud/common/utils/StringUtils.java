package com.cloud.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * @program: cloud_example
 * @description: 字符串工具类
 * @author: yangchenglong
 * @create: 2019-05-24 16:03
 */
@Slf4j
public class StringUtils {

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 字符串是否为空
     * update by:
     * @Param:
     * @return:
     */
    public static boolean isBlank(String str){
        return org.apache.commons.lang3.StringUtils.isBlank(str);
    }

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 字符串不为空
     * update by: 
     * @Param: 
     * @return: 
     */
    public static boolean isNotBlank(String str){
        return org.apache.commons.lang3.StringUtils.isNotBlank(str);
    }

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 输出对象到response
     * update by:
     * @Param:
     * @return:
     */
    public static void print(Object object) throws IOException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        response.addHeader("Cache-Control", "no-cache");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(object);
        out.flush();
        out.close();
    }


    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 获取IP地址
     * update by: 
     * @Param: 
     * @return: 
     */
    public static String getIP(HttpServletRequest request) {
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
        if(!org.apache.commons.lang3.StringUtils.isBlank(ip)){
            ip = ip.split(",")[0];
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 判断是PC还是移动端访问
     * update by: 
     * @Param: 
     * @return: 
     */
    public static boolean isMobileDevice(HttpServletRequest request){
        //android : 所有android设备, mac os : iphone ipad , windows phone: windows系统的手机
        String[] deviceArray = new String[]{"android","mac os","windows phone"};
        String requestHeader = request.getHeader("user-agent");
        if(org.apache.commons.lang3.StringUtils.isBlank(requestHeader)){
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

    /**
     * 判断用户是否移动端访问
     * android : 所有android设备
     * mac os : iphone ipad
     * windows phone:Nokia等windows系统的手机
     */
    public static boolean  isMobileDevice() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String requestHeader = request.getHeader("article-agent");
        String[] deviceArray = {"android", "mac os", "windows phone"};
        if (requestHeader == null)
            return false;
        requestHeader = requestHeader.toLowerCase();
        for (String mobile : deviceArray) {
            if (requestHeader.indexOf(mobile) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 随机流水号UUID
     * update by: 
     * @Param: 
     * @return: 
     */
    public static String getSerialNumber() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {
        System.out.println(getSerialNumber());
    }

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: MD5加密
     * update by: 
     * @Param: 
     * @return: 
     */
    public static String md5(String password) {
        try {
            //得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes("UTF-8"));
            StringBuffer buffer = new StringBuffer();
            // 把没一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (Exception e) {
            log.error("md5Password error:{}", e);
            return "";
        }
    }

}
