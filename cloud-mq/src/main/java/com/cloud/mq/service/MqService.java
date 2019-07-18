package com.cloud.mq.service;

/**
 * @program: cloud_example
 * @description: 发送消息队列
 * @author: yangchenglong
 * @create: 2019-07-17 16:16
 */
public interface MqService {

    void convertAndSend(String exchangeName, String queueName, Object object);

    void declareOfConvertAndSend(String exchangeName, String queueName, Object object);

}
