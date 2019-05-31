package com.cloud.gate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: cloud_example
 * @description: 网关
 * @author: yangchenglong
 * @create: 2019-05-31 17:52
 */
@RestController
@RequestMapping("/gate")
@Api(tags = "网关", description = "网关授权API调用")
public class GateController {

    @RequestMapping(value = "/acessApi", method = RequestMethod.POST)
    @ApiOperation(value = "授权API调试", notes = "密码通过RSA加密传输")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "授权码", value = "authCode", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "用户名", value = "username", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "密码", value = "password", dataType = "String", paramType = "query", required = true)
    })
    public void acessApi(String authCode, String username, String password) {
        System.out.println("acessApi : "+authCode+", "+username+", "+password);
    }

}
