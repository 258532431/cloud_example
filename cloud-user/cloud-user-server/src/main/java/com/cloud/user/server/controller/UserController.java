package com.cloud.user.server.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.user.entity.User;
import com.cloud.user.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @program: cloud_example
 * @description: 用户rest
 * @author: yangchenglong
 * @create: 2019-05-31 16:08
 */
@RestController
@RequestMapping("/base")
@Api(tags = "用户管理", description = "用户管理rest接口")
public class UserController extends BaseController{

    @Resource
    private UserService userService;

    @RequestMapping(value = "/getSessionUser", method = RequestMethod.GET)
    @ApiOperation(value = "获取当前登录用户", notes = "")
    public void getSessionUser(){
        HttpSession session = request.getSession();
        System.out.println("UserController session : "+session.getAttribute("authCode")+", "+session.getAttribute("username"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "用户登录", notes = "登录密码通过RSA加密传输")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query", required = true)
    })
    public User login(@RequestParam String username, @RequestParam String password){
        User user = userService.login(username, password);
        if(user != null){
            HttpSession session = request.getSession();
            session.setAttribute("userInfo", user);
        }
        return user;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据ID获取用户", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键Id", dataType = "Long", paramType = "path", example = "0", required = true)
    })
    public User get(@PathVariable Long id){
        return userService.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增用户", notes = "密码通过RSA加密传输")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "String", paramType = "query")
    })
    public int insertSelective(@ApiIgnore User user){
        return userService.insertSelective(user);
    }

}
