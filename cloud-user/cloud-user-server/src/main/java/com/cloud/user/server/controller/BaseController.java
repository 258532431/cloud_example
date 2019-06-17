package com.cloud.user.server.controller;

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

}
