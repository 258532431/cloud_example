package com.cloud.common.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BizLog implements Serializable {

    private static final long serialVersionUID = -984722441039400460L;

    private Long id;

    private String bizId;//业务ID

    private String operateModule;//操作模块

    private String operateType;//操作类型

    private String ip;//ip地址

    private String operateParams;//参数

    private String url;//请求url

    private String operator;//操作人

    private String operatorName;//操作人姓名

    private Date operateTime;//操作时间

    private String visitDeviceType;//访问设备类型

    private String operateClass;//操作的类

    private String operateMethod;//操作的方法
}