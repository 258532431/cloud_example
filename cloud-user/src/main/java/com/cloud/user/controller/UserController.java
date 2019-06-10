package com.cloud.user.controller;

import com.cloud.user.common.LogBiz;
import com.cloud.user.common.LogUtils;
import com.cloud.user.entity.User;
import com.cloud.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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

    @LogBiz(operator = "操作人", operatingModule = LogBiz.OperatingModule.USER, description = "操作描述")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据ID获取用户", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键Id", dataType = "Long", paramType = "path", example = "0", required = true)
    })
    public User get(@PathVariable Long id){
        //记录业务日志
        Map fieldValues = new HashMap();
        fieldValues.put("operator", "wilson");
        fieldValues.put("description", "根据ID获取用户");
        LogUtils.setAnnotationValue(UserController.class, "get", fieldValues, Long.class);

        return this.userService.selectByPrimaryKey(id);
    }

    @LogBiz(operator = "操作人", operatingModule = LogBiz.OperatingModule.USER, description = "操作描述")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增用户", notes = "密码通过RSA加密传输")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "String", paramType = "query")
    })
    public int insertSelective(@ApiIgnore User user){
        //记录业务日志
        Map fieldValues = new HashMap();
        fieldValues.put("operator", "风叔叔");
        fieldValues.put("description", "新增用户");
        LogUtils.setAnnotationValue(UserController.class, "insertSelective", fieldValues, User.class);

        return this.userService.insertSelective(user);
    }

}
