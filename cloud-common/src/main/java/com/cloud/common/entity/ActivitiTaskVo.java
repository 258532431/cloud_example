package com.cloud.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: cloud_example
 * @description: 工作流任务
 * @author: yangchenglong
 * @create: 2019-07-10 15:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivitiTaskVo implements Serializable {

    private static final long serialVersionUID = 56313146508656741L;

    private String taskId;//任务ID

    private String taskName;//任务名称

    private String businessKey;//业务表唯一标识

    private String businessType;//业务类型 如：0-请假 1-报销

    private String assignee;//任务办理人

}
