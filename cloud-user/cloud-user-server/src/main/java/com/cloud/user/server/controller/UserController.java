package com.cloud.user.server.controller;

import com.cloud.common.entity.ResponseMessage;
import com.cloud.common.enums.ResponseCodeEnum;
import com.cloud.user.entity.User;
import com.cloud.user.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * @program: cloud_example
 * @description: 用户rest
 * @author: yangchenglong
 * @create: 2019-05-31 16:08
 */
@RestController
@Transactional
@RequestMapping("/base")
@Api(tags = "用户管理", description = "用户管理rest接口")
public class UserController extends BaseController{

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "用户登录", notes = "登录密码通过RSA加密传输")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query", required = true)
    })
    public ResponseMessage<User> login(@RequestParam String username, @RequestParam String password){
        User user = userService.login(username, password);
        if(user != null){
            setUserSession(user);
        }

        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200, user);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ApiOperation(value = "退出登录", notes = "请求需要在header中传入token")
    public ResponseMessage logout(){
        removeUserSession();
        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200);
    }

    @RequestMapping(value = "/getSessionUser", method = RequestMethod.GET)
    @ApiOperation(value = "获取当前登录用户", notes = "请求需要在header中传入token")
    public ResponseMessage<User> getSessionUser(){
        User user = getUserSession();
        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200, user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据ID获取用户", notes = "请求需要在header中传入token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键Id", dataType = "Long", paramType = "path", example = "0", required = true)
    })
    public ResponseMessage<User> get(@PathVariable Long id){
        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200, userService.selectByPrimaryKey(id));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增用户", notes = "密码通过RSA加密传输；请求需要在header中传入token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "String", paramType = "query")
    })
    public ResponseMessage insertSelective(@ApiIgnore User user){
        User entity = userService.insertSelectiveGet(user);
        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200, entity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "根据ID删除用户", notes = "请求需要在header中传入token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键Id", dataType = "Long", paramType = "path", example = "0", required = true)
    })
    public ResponseMessage<User> delete(@PathVariable Long id){
        userService.deleteByPrimaryKey(id);
        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200);
    }

}
