package com.cloud.mq.controller;

import com.cloud.common.entity.ResponseMessage;
import com.cloud.common.enums.ResponseCodeEnum;
import com.cloud.common.mq.MqConstants;
import com.cloud.mq.service.MqService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: cloud_example
 * @description: 消息中间件
 * @author: yangchenglong
 * @create: 2019-07-17 16:12
 */
@RestController
@RequestMapping("/base")
@Api(tags = "消息队列管理", description = "消息队列管理rest接口")
public class RabbitMqController extends BaseController{

    @Resource
    private MqService mqSendService;

    @RequestMapping(value = "/sendUser", method = RequestMethod.POST)
    @ApiOperation(value = "发送用户数据", notes = "")
    @ApiImplicitParam(name = "content", value = "消息体（json格式）", dataType = "String", paramType = "query", required = true)
    public ResponseMessage sendUser(@RequestParam String content){
        String exchangeName = mqSendService.getUpExchangeName(null);
        String queueName = MqConstants.QUEUE_USER_SUFFIX;
        mqSendService.declareOfConvertAndSend(exchangeName, queueName, content);
        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200);
    }

}
