package com.cloud.mq.controller;

import com.cloud.common.utils.DateUtils;
import com.cloud.common.utils.StringUtils;
import com.cloud.mq.utils.RedisUtils;
import com.cloud.user.constant.UserConstants;
import com.cloud.user.entity.User;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @program: cloud_example
 * @description: 基础rest
 * @author: yangchenglong
 * @create: 2019-05-31 16:10
 */
public class BaseController {

    @Resource
    public HttpServletRequest request;

    @Resource
    public HttpServletResponse response;

    @Resource
    public RedisUtils redisUtils;

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 缓存会话数据
     * update by: 
     * @Param:
     * @return:  缓存key值
     */
    public String setUserSession(User user) {
        String tokenPrefix = UserConstants.REDIS_PC_USER_TOKEN;
        String token = UserConstants.REDIS_PC_USER_TOKEN + ":" +request.getHeader(UserConstants.PC_ACCESS_TOKEN);
        Long expireTime = UserConstants.PC_SESSION_EXPIRETIME_SECONDS;
        if (StringUtils.isMobileDevice(request)) {//移动端
            tokenPrefix = UserConstants.REDIS_MOBILE_USER_TOKEN;
            token = UserConstants.REDIS_MOBILE_USER_TOKEN + ":" +request.getHeader(UserConstants.MOBILE_ACCESS_TOKEN);
            expireTime = UserConstants.MOBILE_SESSION_EXPIRETIME_SECONDS;
        }
        if (StringUtils.isNotBlank(token) && redisUtils.exists(token)) {//token存在刷新
            redisUtils.refreshSessionCache(token, expireTime);
        } else {//不存在增加
            token = StringUtils.getSerialNumber() + StringUtils.md5(DateUtils.DateToString(new Date(), DateUtils.formatToNo));
            user.setToken(token);
            redisUtils.setSessionCache(tokenPrefix + ":" +token, user, expireTime);
        }

        return token;
    }

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 取出会话数据
     * update by:
     * @Param:
     * @return:  User
     */
    public User getUserSession() {
        String token = UserConstants.REDIS_PC_USER_TOKEN + ":" +request.getHeader(UserConstants.PC_ACCESS_TOKEN);
        if (StringUtils.isMobileDevice(request)) {//移动端
            token = UserConstants.REDIS_MOBILE_USER_TOKEN + ":" +request.getHeader(UserConstants.MOBILE_ACCESS_TOKEN);
        }
        if (StringUtils.isNotBlank(token) && redisUtils.exists(token)) {
            return (User) redisUtils.getObject(token);
        }

        return null;
    }

}
