package com.cloud.user.controller;

import com.cloud.user.entity.User;
import com.cloud.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: cloud_example
 * @description: 用户rest
 * @author: yangchenglong
 * @create: 2019-05-31 16:08
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理", description = "用户管理rest接口")
public class UserController extends BaseController{

    @Resource
    private UserService userService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据ID获取用户", notes = "测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "主键Id", value = "Id", dataType = "String", paramType = "query", required = true)
    })
    public User get(String id) {
        return this.userService.selectByPrimaryKey(Long.valueOf(id));
    }

}
