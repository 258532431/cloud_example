package com.cloud.user.server.controller;

import com.cloud.common.entity.ResponseMessage;
import com.cloud.common.enums.ResponseCodeEnum;
import com.cloud.user.entity.Leave;
import com.cloud.user.server.service.LeaveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: cloud_example
 * @description: 请假rest
 * @author: yangchenglong
 * @create: 2019-05-31 16:08
 */
@RestController
@Transactional
@RequestMapping("/leave")
@Api(tags = "请假管理", description = "请假管理rest接口")
public class LeaveController extends BaseController {

    @Resource
    private LeaveService leaveService;

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

}
