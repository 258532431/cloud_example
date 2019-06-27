package com.cloud.gate.controller;

import com.cloud.common.entity.ResponseMessage;
import com.cloud.common.enums.ResponseCodeEnum;
import com.cloud.gate.feign.UserFeign;
import com.cloud.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
public class GateController extends BaseController{

    @Resource
    private UserFeign userFeign;

    @RequestMapping(value = "/testSessionUser", method = RequestMethod.GET)
    @ApiOperation(value = "session测试", notes = "")
    public void testSessionUser() {
        User sessionUser = getUserSession();
        System.out.println("gate session User: "+sessionUser);
    }

    @RequestMapping(value = "/debugLogin", method = RequestMethod.POST)
    @ApiOperation(value = "调试用登录", notes = "密码通过RSA加密传输")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authCode", value = "授权码", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query", required = true)
    })
    @ResponseBody
    public ResponseMessage debugLogin(@RequestParam String authCode, @RequestParam String username, @RequestParam String password) {
        User user = userFeign.login(username, password);
        System.out.println("user : "+user.toString());

        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200, user);
    }

}
