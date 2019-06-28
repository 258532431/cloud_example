package com.cloud.gate.controller;

import com.cloud.common.config.GlobalException;
import com.cloud.common.entity.ResponseMessage;
import com.cloud.common.enums.ResponseCodeEnum;
import com.cloud.gate.feign.UserFeign;
import com.cloud.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @Value("${debug-api-password}")
    private String debugApiPassword;//接口调试密码

    @Resource
    private UserFeign userFeign;

    @RequestMapping(value = "/authorize", method = RequestMethod.POST)
    @ApiOperation(value = "调试用登录", notes = "密码通过RSA加密传输")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "apiPassword", value = "接口调试密码", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query", required = true)
    })
    public ResponseMessage<User> authorize(@RequestParam String apiPassword, @RequestParam String username, @RequestParam String password) {
        if(debugApiPassword.equals(apiPassword)){
            removeUserSession();
            ResponseMessage<User> user = userFeign.login(username, password);
            if(user.getDatas() == null) {
                throw  new GlobalException(user.getErrorCode(), user.getMsg());
            }
            //调试方便，将token保存在session中
            HttpSession session = request.getSession();
            session.setAttribute("token", user.getDatas().getToken());
            return user;
        }

        return new ResponseMessage<>(ResponseCodeEnum.RETURN_CODE_102001);
    }

}
