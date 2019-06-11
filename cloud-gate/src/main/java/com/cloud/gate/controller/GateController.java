package com.cloud.gate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @program: cloud_example
 * @description: 网关
 * @author: yangchenglong
 * @create: 2019-05-31 17:52
 */
@RestController
@RequestMapping("/base")
@Api(tags = "网关", description = "网关授权rest接口")
public class GateController {

    @RequestMapping(value = "/acess", method = RequestMethod.POST)
    @ApiOperation(value = "授权API调试", notes = "密码通过RSA加密传输")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authCode", value = "授权码", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query", required = true)
    })
    public void acess(HttpServletRequest request, String authCode, String username, String password) {
        System.out.println("acess : "+authCode+", "+username+", "+password);

        HttpSession session = request.getSession();
        session.setAttribute("authCode", authCode);
        session.setAttribute("username", username);

        System.out.println("session : "+session.getAttribute("authCode")+", "+session.getAttribute("username"));
    }

}
