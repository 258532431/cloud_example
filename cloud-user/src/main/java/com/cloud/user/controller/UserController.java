package com.cloud.user.controller;

import com.cloud.user.entity.User;
import com.cloud.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/base")
@Api(tags = "用户管理", description = "用户管理rest接口")
public class UserController extends BaseController{

    @Resource
    private UserService userService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据ID获取用户", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键Id", dataType = "Long", paramType = "path", example = "0", required = true)
    })
    public User get(@PathVariable Long id) {
        return this.userService.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增用户", notes = "密码通过RSA加密传输")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "String", paramType = "query")
    })
    public int insertSelective(@ApiIgnore User user){
        return this.userService.insertSelective(user);
    }

}
