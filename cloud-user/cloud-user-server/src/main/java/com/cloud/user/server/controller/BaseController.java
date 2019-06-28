package com.cloud.user.server.controller;

import com.cloud.common.utils.DateUtils;
import com.cloud.common.utils.StringUtils;
import com.cloud.user.constant.UserConstants;
import com.cloud.user.entity.User;
import com.cloud.user.server.utils.RedisUtils;

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
        String token = UserConstants.REDIS_PC_USER_TOKEN + ":" +request.getHeader(UserConstants.PC_ACCESS_TOKEN);
        if (StringUtils.isMobileDevice(request)) {//移动端
            token = UserConstants.REDIS_MOBILE_USER_TOKEN + ":" +request.getHeader(UserConstants.MOBILE_ACCESS_TOKEN);
        }
        if (StringUtils.isNotBlank(token) && redisUtils.exists(token)) {//token存在刷新
            redisUtils.refreshSessionCache(token);
        } else {//不存在增加
            token = StringUtils.getSerialNumber() + StringUtils.md5(DateUtils.DateToString(new Date(), DateUtils.formatToNo));
            user.setToken(token);
            redisUtils.setSessionCache(token, user);
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

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 清空会话数据
     * update by:
     * @Param:
     * @return:
     */
    public void removeUserSession() {
        String token = UserConstants.REDIS_PC_USER_TOKEN + ":" +request.getHeader(UserConstants.PC_ACCESS_TOKEN);
        if (StringUtils.isMobileDevice(request)) {//移动端
            token = UserConstants.REDIS_MOBILE_USER_TOKEN + ":" +request.getHeader(UserConstants.MOBILE_ACCESS_TOKEN);
        }
        if (StringUtils.isNotBlank(token) && redisUtils.exists(token)) {
            redisUtils.remove(token);
        }
    }

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 设置缓存
     * update by:
     * @Param:
     * @return:
     */
    /*public void setSessionCache(String key, Object value) {
        if (StringUtils.isMobileDevice()) {//移动端
            redisUtils.setExpireObject(UserConstants.REDIS_MOBILE_USER_TOKEN + ":" +key, value, UserConstants.MOBILE_SESSION_EXPIRETIME_SECONDS);
        } else {//PC端
            redisUtils.setExpireObject(UserConstants.REDIS_PC_USER_TOKEN + ":" +key, value, UserConstants.PC_SESSION_EXPIRETIME_SECONDS);
        }
    }*/

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 刷新缓存
     * update by:
     * @Param:
     * @return:
     */
    /*public void refreshSessionCache(String key) {
        if (StringUtils.isMobileDevice()) {
            redisUtils.expire(UserConstants.REDIS_MOBILE_USER_TOKEN + ":" +key, UserConstants.MOBILE_SESSION_EXPIRETIME_SECONDS);
        } else {
            redisUtils.expire(UserConstants.REDIS_PC_USER_TOKEN + ":" +key, UserConstants.PC_SESSION_EXPIRETIME_SECONDS);
        }
    }*/

}
