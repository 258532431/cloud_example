package com.cloud.user.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data   //get、set方法等
@NoArgsConstructor //无参构造方法
@AllArgsConstructor //有参构造方法
@ApiModel(value = "请假")
public class Leave implements Serializable {

    private static final long serialVersionUID = -7351383732369014635L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "请假唯一编码")
    private String leaveCode;

    @ApiModelProperty(value = "用户唯一编码")
    private String userCode;

    @ApiModelProperty(value = "请假类型 0-事假 1-调休")
    private Byte type;

    @ApiModelProperty(value = "请假说明")
    private String content;

    @ApiModelProperty(value = "审核状态 0-待审 1-通过 2-被拒绝")
    private Boolean auditStatus;
}