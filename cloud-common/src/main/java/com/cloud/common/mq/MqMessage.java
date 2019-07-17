package com.cloud.common.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;

/**
 * @program: cloud_example
 * @description: mq消息
 * @author: yangchenglong
 * @create: 2019-07-17 16:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MqMessage<T> implements Serializable {

    private static final long serialVersionUID = 1064508078206420834L;

    private String msgId;//消息唯一标识

    private String bizId;//业务ID

    private HandleTypeEum handleType;//处理类型

    private T content;//消息体内容

}
