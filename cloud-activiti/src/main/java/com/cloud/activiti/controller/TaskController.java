package com.cloud.activiti.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.config.GlobalException;
import com.cloud.common.entity.ActivitiTaskVo;
import com.cloud.common.entity.ResponseMessage;
import com.cloud.common.enums.ActivitiTaskStatusEnum;
import com.cloud.common.enums.ResponseCodeEnum;
import com.cloud.common.utils.StringUtils;
import com.cloud.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @ApiImplicitParam(name = "businessType", value = "业务类型 如：0-请假 1-报销", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "variablesJson", value = "业务属性值 - 由Map转Json", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/startTask", method = RequestMethod.POST)
    public ResponseMessage startTask(@RequestParam String processDefinitionKey, @RequestParam String businessKey, @RequestParam String businessType,
                                     @RequestParam String variablesJson) {
        //设置认证信息
        String userCode = this.getSessionUser().getUserCode();//用户code唯一标识
        identityService.setAuthenticatedUserId(userCode);
        Map<String, Object> variables = new HashMap();
        log.info("variablesJson = {}", variablesJson);
        if(StringUtils.isNotBlank(variablesJson)){
            variables = JSONObject.parseObject(variablesJson);
        }
        variables.put("businessType", businessType);
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        log.info("流程启动成功，流程id:{}", pi.getId());
        ExecutionEntity executionEntity = (ExecutionEntity) pi;
        String taskId = executionEntity.getTasks().get(0).getId();//任务ID
        // 根据实例把流程往下推
        taskService.complete(taskId, variables);

        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200);
    }

    @ApiOperation(value = "审批任务", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务表ID", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "variablesJson", value = "业务属性值 - 由Map转Json（如：{\"auditStatus\": 1,\"auditorCode\": \"下一个审批人\",\"businessKey\": \"业务表kEY\"}）", dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/auditTask", method = RequestMethod.POST)
    public ResponseMessage auditTask(@RequestParam String taskId, @RequestParam String variablesJson) {
        String userCode = this.getSessionUser().getUserCode();//用户code唯一标识
        Map<String, Object> variables = new HashMap();
        log.info("variablesJson = {}", variablesJson);
        if(StringUtils.isNotBlank(variablesJson)){
            variables = JSONObject.parseObject(variablesJson);
        }
        // 使用任务id, 获取任务对象，获取流程实例id
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //利用任务对象，获取流程实例id
        String processInstancesId = task.getProcessInstanceId();
        Authentication.setAuthenticatedUserId(userCode); // 添加批注时候的审核人，通常应该从session获取
        //在每次提交任务的时候需要描述一些批注信息，例如：请假流程提交的时候要描述信息为什么请假，如果领导驳回可以批注驳回原因等
        taskService.addComment(taskId, processInstancesId, variablesJson);
        // 根据实例把流程往下推
        taskService.complete(taskId, variables);
        //判断流程是否结束,查询正在执行的执行对象表
        ProcessInstance rpi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstancesId)//查询条件-流程的实例id(流程的实例id在流程启动后的整个流程中是不改变的)
                .singleResult(); //返回唯一结果集
        String mark = ActivitiTaskStatusEnum.PROCESSING.getCode();
        if(rpi == null) {
            mark = ActivitiTaskStatusEnum.FINISH.getCode();
            log.info("auditTask，任务执行完毕,taskId={}, variablesJson={}", taskId, variablesJson);
        }

        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200, mark);
    }

    @ApiOperation(value = "获取待当前用户审批的任务", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "businessType", value = "业务类型 如：0-请假 1-报销", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", example = "0", paramType = "query", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", example = "0", paramType = "query", required = true)
    })
    @RequestMapping(value = "/getAuditList", method = RequestMethod.GET)
    public ResponseMessage<List<ActivitiTaskVo>> getAuditList(@RequestParam String businessType, @RequestParam int pageNum, @RequestParam int pageSize) {
        pageNum = pageNum < 1 ? 1 : pageNum;
        pageSize = pageSize <= 1 ? 0xf423f : pageSize;
        int startIndex = (pageNum - 1) * pageSize;
        String auditorCode = this.getSessionUser().getUserCode();//用户code唯一标识
        long count = taskService.createTaskQuery().taskAssignee(auditorCode).processVariableValueEquals("businessType", businessType).count();
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(auditorCode).processVariableValueEquals("businessType", businessType).orderByTaskCreateTime().desc().listPage(startIndex, pageSize);
        List<ActivitiTaskVo> list = new ArrayList<>();
        tasks.forEach(task -> {
            ActivitiTaskVo vo = new ActivitiTaskVo();
            vo.setTaskId(task.getId());
            vo.setTaskName(task.getName());
            //获取流程实例对象
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            vo.setBusinessKey(processInstance.getBusinessKey());
            vo.setAssignee(task.getAssignee());
            vo.setBusinessType(businessType);

            list.add(vo);
        });
        return new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200, list, count);
    }

}
