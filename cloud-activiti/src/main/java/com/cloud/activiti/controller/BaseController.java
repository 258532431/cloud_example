package com.cloud.activiti.controller;

import com.cloud.activiti.utils.RedisUtils;
import com.cloud.common.utils.StringUtils;
import com.cloud.user.constant.UserConstants;
import com.cloud.user.entity.User;
import org.activiti.engine.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @Resource
    protected RepositoryService repositoryService;//	管理流程定义和流程部署的API

    @Resource
    protected RuntimeService runtimeService;//流程运行时对流程实例进行管理和控制

    @Resource
    protected IdentityService identityService;//流程用户角色管理

    @Resource
    protected TaskService taskService;//管理流程任务(签收,提交)

    @Resource
    protected FormService formService;//动态表单

    @Resource
    protected HistoryService historyService;//管理流程历史数据

    @Resource
    protected ManagementService managementService;//管理维护流程引擎

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
