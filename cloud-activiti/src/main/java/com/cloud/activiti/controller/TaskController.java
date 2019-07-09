package com.cloud.activiti.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.config.GlobalException;
import com.cloud.common.entity.ResponseMessage;
import com.cloud.common.enums.ResponseCodeEnum;
import com.cloud.common.utils.StringUtils;
import com.cloud.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: cloud_example
 * @description: 任务管理
 * @author: yangchenglong
 * @create: 2019-07-09 11:10
 */
@RestController
@RequestMapping("/task")
@Api(tags = "工作流管理-任务", description = "工作流管理rest接口")
@Slf4j
public class TaskController extends BaseController{

    //获取当前登录用户信息
    public User getSessionUser(){
        User user = getUserSession();
        if(user == null) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100000);
        }
        return user;
    }

    @ApiOperation(value = "启动任务流程", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinitionKey", value = "流程图KEY(模型KEY)", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "businessKey", value = "业务表唯一标识", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "variablesJson", value = "业务属性值 - 由Map转Json", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/startTask", method = RequestMethod.POST)
    public ResponseMessage startTask(@RequestParam String processDefinitionKey, @RequestParam String businessKey, @RequestParam String variablesJson) {
        //设置认证信息
        String userCode = this.getSessionUser().getUserCode();//用户code唯一标识
        identityService.setAuthenticatedUserId(userCode);
        Map<String, Object> variables = new HashMap();
        log.info("variablesJson = {}", variablesJson);
        if(StringUtils.isNotBlank(variablesJson)){
            variables = JSONObject.parseObject(variablesJson);
        }
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        log.info("流程启动成功，流程id:{}", pi.getId());
        ExecutionEntity executionEntity = (ExecutionEntity) pi;
        String taskId = executionEntity.getTasks().get(0).getId();//任务ID
        // 根据实例把流程往下推
        taskService.complete(taskId, variables);

        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200);
    }

    @ApiOperation(value = "获取当前用户的待办任务列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinitionKey", value = "流程图KEY(模型KEY)", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", example = "0", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", example = "0", paramType = "query", required = true)
    })
    @RequestMapping(value = "/getTaskListByUser", method = RequestMethod.GET)
    public ResponseMessage<List<Task>> getTaskListByUser(@RequestParam String processDefinitionKey, @RequestParam int pageNum, @RequestParam int pageSize) {
        pageNum = pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize <= 1 ? 0xf423f : pageSize;
        int startIndex = (pageNum - 1) * pageSize;
        String userCode = this.getSessionUser().getUserCode();//用户code唯一标识
        long count = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).taskCandidateOrAssigned(userCode).count();
        List<Task> tasks = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).taskCandidateOrAssigned(userCode).orderByTaskCreateTime().desc().listPage(startIndex, pageSize);

        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200, tasks, count);
    }


    @ApiOperation(value = "获取当前用户的任务", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", example = "0", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", example = "0", paramType = "query", required = true)
    })
    @RequestMapping(value = "/getTaskList", method = RequestMethod.GET)
    public ResponseMessage<List<String>> getTaskList(@RequestParam int pageNum, @RequestParam int pageSize) {
        pageNum = pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize <= 1 ? 0xf423f : pageSize;
        int startIndex = (pageNum - 1) * pageSize;
        String userCode = this.getSessionUser().getUserCode();//用户code唯一标识
        long count = taskService.createTaskQuery().processVariableValueEquals("usercode", userCode).orderByTaskCreateTime().desc().count();
        List<Task> tasks = taskService.createTaskQuery().processVariableValueEquals("usercode", userCode).orderByTaskCreateTime().desc().listPage(startIndex, pageSize);
        List<String> result = new ArrayList<>();
        tasks.forEach(task -> {
            result.add(task.toString());
        });
        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200, result, count);
    }

    @ApiOperation(value = "获取待当前用户审批的任务", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", example = "0", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", example = "0", paramType = "query", required = true)
    })
    @RequestMapping(value = "/getAuditList", method = RequestMethod.GET)
    public ResponseMessage<List<Task>> getAuditList(@RequestParam int pageNum, @RequestParam int pageSize) {
        pageNum = pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize <= 1 ? 0xf423f : pageSize;
        int startIndex = (pageNum - 1) * pageSize;
        String auditorCode = this.getSessionUser().getUserCode();//用户code唯一标识
        // 获取个人已签收任务
        long count = taskService.createTaskQuery().taskAssignee(auditorCode).count();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(auditorCode).listPage(startIndex, pageSize);
        List<String> result = new ArrayList<>();
        tasks.forEach(task -> {
            result.add(task.toString());
        });
        // 获取个人待签收任务
        List<Task> untasks = taskService.createTaskQuery().taskCandidateUser(auditorCode).listPage(startIndex, pageSize);
        untasks.forEach(task -> {
            result.add(task.toString());
        });
        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200, result, count);
    }

}
