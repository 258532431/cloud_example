package com.cloud.mq.service.impl;

import com.cloud.common.mq.MqConstants;
import com.cloud.common.utils.StringUtils;
import com.cloud.mq.service.MqService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @program: cloud_example
 * @description: 发送消息队列实现
 * @author: yangchenglong
 * @create: 2019-07-17 16:16
 */
@Service
public class MqServiceImpl implements MqService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RabbitAdmin rabbitAdmin;

    //发送消息
    @Override
    public void convertAndSend(String exchangeName, String queueName, Object object){
        rabbitTemplate.convertAndSend(exchangeName, queueName, object);
    }

    //声明交换机和队列，并发送消息
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
        rabbitTemplate.convertAndSend(exchangeName, queueName, object);
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

    /**
     * @Author: yangchenglong on 2019/7/17
     * @Description: 获取上行交换机名称
     * update by:
     * @Param: exchangePrefix：交换机名称前缀
     * @return:
     */
    @Override
    public String getUpExchangeName(String exchangePrefix){
        if(StringUtils.isBlank(exchangePrefix)){
            return MqConstants.EXCHANGE_UP_SUFFIX;
        }
        return exchangePrefix + "." + MqConstants.EXCHANGE_UP_SUFFIX;
    }

    /**
     * @Author: yangchenglong on 2019/7/17
     * @Description: 获取下行交换机名称
     * update by:
     * @Param: exchangePrefix：交换机名称前缀
     * @return:
     */
    @Override
    public String getDownExchangeName(String exchangePrefix){
        if(StringUtils.isBlank(exchangePrefix)){
            return MqConstants.EXCHANGE_DOWN_SUFFIX;
        }
        return exchangePrefix + "." + MqConstants.EXCHANGE_DOWN_SUFFIX;
    }

    /**
     * @Author: yangchenglong on 2019/7/17
     * @Description:
     * update by:
     * @Param: queuePrefix：队列名前缀，queueSufix：队列名后缀
     * @return:
     */
    @Override
    public String getQueueName(String queuePrefix, String queueSufix){
        return queuePrefix + "." + queueSufix;
    }

}
