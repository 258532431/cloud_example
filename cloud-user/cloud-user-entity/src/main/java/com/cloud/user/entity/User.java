package com.cloud.user.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data   //get、set方法等
@NoArgsConstructor //无参构造方法
@AllArgsConstructor //有参构造方法
@ApiModel(value = "用户")
public class User implements Serializable {

    private static final long serialVersionUID = -8193496733708663442L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "用户唯一编码")
    private String userCode;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "状态 0-正常 1-冻结")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;
}