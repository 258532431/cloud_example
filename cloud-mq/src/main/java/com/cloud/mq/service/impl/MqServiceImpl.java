package com.cloud.mq.service.impl;

import com.cloud.common.mq.MqConstants;
import com.cloud.mq.service.MqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @program: cloud_example
 * @description: 发送消息队列实现
 * @author: yangchenglong
 * @create: 2019-07-17 16:16
 */
@Slf4j
@Service
public class MqServiceImpl implements MqService, ApplicationRunner, RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback{

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RabbitAdmin rabbitAdmin;

    //发送消息, 有确认
    @Override
    public void convertAndSend(String exchangeName, String queueName, Object object){
        rabbitTemplate.setConfirmCallback(this);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        log.info("发送消息id={}", correlationData.getId());
        rabbitTemplate.convertAndSend(exchangeName, queueName, object, correlationData);
    }

    //声明交换机和队列，并发送消息, 有确认
    @Override
    public void declareOfConvertAndSend(String exchangeName, String queueName, Object object) {
        //声明交换机
        DirectExchange exchange = this.getDirectExchange(exchangeName);
        this.declareExchange(exchange);
        //声明队列
        Queue queue = this.getQueue(queueName);
        this.declareQueue(queue);
        //使用BindingBuilder绑定交换机和队列
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(queueName));

        rabbitTemplate.setConfirmCallback(this);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        log.info("发送消息id={}", correlationData.getId());
        rabbitTemplate.convertAndSend(exchangeName, queueName, object, correlationData);
    }

    //申明交换机
    private void declareExchange(AbstractExchange exchange){
        rabbitAdmin.declareExchange(exchange);
    }

    //删除交换机
    private void deleteExchange(String exchangeName){
        rabbitAdmin.deleteExchange(exchangeName);
    }

    //申明队列
    private void declareQueue(Queue queue){
        rabbitAdmin.declareQueue(queue);
    }

    //抛弃掉队列中的所有消息
    private void purgeQueue(String queueName){
        rabbitAdmin.purgeQueue(queueName, false);
    }

    //创建一个Direct交换机：完全根据key投递，比如设置了Routing key等于abc，那么客户端必须发送key=abc才会被投递到队列
    private DirectExchange getDirectExchange(String exchangeName) {
        return new DirectExchange(exchangeName, true, false);
    }

    //创建一个topic交换机：根据key进行模糊匹配后投递，可以使用符合#匹配一个或者多个词，*只能匹配一个次
    private TopicExchange getTopicExchange(String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }

    //创建一个Fanout交换机：无需配置关键字Key，它采用广播室，一个消息进来，投递给所有与该Exchange绑定的消息队列
    private FanoutExchange getFanoutExchange(String exchangeName) {
        return new FanoutExchange(exchangeName, true, false);
    }

    //创建一个队列
    private Queue getQueue(String queueName){
        return new Queue(queueName);
    }

    @Override
    public void run(ApplicationArguments args){
        log.info("---------- 正在初始化MQ交换机和队列 ---------");
        DirectExchange upExchange = this.getDirectExchange(MqConstants.EXCHANGE_UP);
        DirectExchange downExchange = this.getDirectExchange(MqConstants.EXCHANGE_DOWN);
        this.declareExchange(upExchange);
        this.declareExchange(downExchange);
        Queue queue1 = this.getQueue(MqConstants.QUEUE_USER);
        Queue queue2 = this.getQueue(MqConstants.QUEUE_USER);
        this.declareQueue(queue1);
        this.declareQueue(queue2);
        log.info("---------- 初始化MQ交换机和队列完成 ---------");
    }

    //如果消息没有到exchange,则confirm回调,ack=false，如果消息到达exchange,则confirm回调,ack=true
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("消息发送到exchange确认成功, 消息id={}", correlationData.getId());
        } else {
            log.info("消息发送exchange确认失败, 消息id={},原因={}", correlationData.getId(), cause);
        }
    }

    //exchange到queue成功,则不回调return，exchange到queue失败,则回调return(需设置mandatory=true,否则不回回调,消息就丢了)
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String queue) {
        String content = new String(message.getBody());
        log.info("消息发送到queue失败，content={},replyCode={}, replyText={}, exchange={},queue={}", content, replyCode, replyText,exchange,queue);
    }
}