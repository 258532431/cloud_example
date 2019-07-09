package com.cloud.user.server.controller;

import com.cloud.common.config.GlobalException;
import com.cloud.common.entity.ResponseMessage;
import com.cloud.common.enums.ResponseCodeEnum;
import com.cloud.user.entity.Leave;
import com.cloud.user.entity.User;
import com.cloud.user.server.service.LeaveService;
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

/**
 * @program: cloud_example
 * @description: 请假rest
 * @author: yangchenglong
 * @create: 2019-05-31 16:08
 */
@RestController
@RequestMapping("/leave")
@Api(tags = "请假管理", description = "请假管理rest接口")
public class LeaveController extends BaseController {

    @Resource
    private LeaveService leaveService;

    //获取当前登录用户信息
    public User getSessionUser(){
        User user = getUserSession();
        if(user == null) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100000);
        }
        return user;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据ID获取对象", notes = "请求需要在header中传入token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键Id", dataType = "Long", paramType = "path", example = "0", required = true)
    })
    public ResponseMessage<Leave> get(@PathVariable Long id){
        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200, leaveService.selectByPrimaryKey(id));
    }

    @RequestMapping(value = "/getByCode/{leaveCode}", method = RequestMethod.GET)
    @ApiOperation(value = "根据code获取对象", notes = "请求需要在header中传入token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "leaveCode", value = "请假编码", dataType = "String", paramType = "path", example = "", required = true)
    })
    public ResponseMessage<Leave> getByCode(@PathVariable String leaveCode){
        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200, leaveService.selectByLeaveCode(leaveCode));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增对象", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "请假类型 0-事假 1-调休", dataType = "int", example = "0", paramType = "query", required = true),
            @ApiImplicitParam(name = "content", value = "请假说明", dataType = "String", paramType = "query", required = true)
    })
    public ResponseMessage<Leave> insertSelective(@ApiIgnore Leave leave){
        leave.setUserCode(this.getSessionUser().getUserCode());
        leaveService.insertSelective(leave);
        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200, leave);
    }

}
