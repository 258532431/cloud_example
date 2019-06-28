package com.cloud.user.constant;

/**
 * @program: cloud_example
 * @description: 用户相关常量
 * @author: yangchenglong
 * @create: 2019-06-27 14:41
 */
public class UserConstants {

    //redis PC端缓存目录
    public static final String REDIS_PC_USER_TOKEN = "PC:USER:TOKEN";

    //redis 移动端缓存目录
    public static final String REDIS_MOBILE_USER_TOKEN = "MOBILE:USER:TOKEN";

    //PC用户登录失效时间（秒）
    public static final Long PC_SESSION_EXPIRETIME_SECONDS = 60 * 60L;

    //PC端用户TOKEN令牌KEY
    public static final String PC_ACCESS_TOKEN = "PCTOKENCEF305134A3D4E9082D5F9DE3AB19033";

    //移动端用户登录失效时间（秒）
    public static final Long MOBILE_SESSION_EXPIRETIME_SECONDS = 15 * 24 * 60 * 60L;

    //移动端用户TOKEN令牌KEY
    public static final String MOBILE_ACCESS_TOKEN = "MOBILETOKENB9A89EC6DC204A9384989C8DE22A73E4";

}
