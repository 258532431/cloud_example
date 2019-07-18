package com.cloud.user.server.mq;

import com.cloud.common.mq.MqConstants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @program: cloud_example
 * @description: 用户-消息队列消费者
 * @author: yangchenglong
 * @create: 2019-07-17 17:45
 */
@Component
@Slf4j
public class UserConsumer {

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = MqConstants.QUEUE_USER, durable = "true"),
            exchange = @Exchange(value = MqConstants.EXCHANGE_UP, durable = "true", type = ExchangeTypes.DIRECT),
            key = MqConstants.QUEUE_USER))
    public void handleUserMessage(Message message, Channel channel){
        try {
            String json = new String(message.getBody());
            log.info("处理消息UserConsumer.handleUserMessage={}", json);

            if("00".equals(json)){//模拟失败
                int i = 1/0;
            }
            // 采用手动应答模式, 手动确认应答更为安全稳定
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (Exception e) {
            log.error("UserConsumer.handleUserMessage.error={}", e);
            //异常，将消息确认后抛弃，或者是放入失败队列，或者记录到数据库后续处理
           this.basicNack(message, channel, false);//拒绝后抛弃
        }
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = MqConstants.QUEUE_TEST, durable = "true"),
            exchange = @Exchange(value = MqConstants.EXCHANGE_UP, durable = "true", type = ExchangeTypes.DIRECT),
            key = MqConstants.QUEUE_TEST))
    public void handleTestMessage(Message message, Channel channel){
        try {
            String json = new String(message.getBody());
            log.info("处理消息UserConsumer.handleTestMessage={}", json);
            // 采用手动应答模式, 手动确认应答更为安全稳定
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (Exception e) {
            log.error("UserConsumer.handleTestMessage.error={}", e);
            //异常，将消息确认后抛弃，或者是放入失败队列，或者记录到数据库后续处理
            this.basicNack(message, channel, false);//拒绝后抛弃
        }
    }

    //拒绝一条或多条消息，并设置是否将消息重发到队列头部
    public void basicNack(Message message, Channel channel, boolean requeue){
        try {
            //最后一个参数 requeue 设置为true 会把消费失败的消息从新添加到队列的尾端，设置为false不会重新回到队列
            boolean multiple = false;//multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), multiple, requeue);
        } catch (IOException e) {
            log.error("basicNack.error={}", e);
        }
    }

    //拒绝一条消息，并设置是否将消息重发到队列头部
    public void basicReject(Message message, Channel channel, boolean requeue){
        try {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), requeue);
        } catch (IOException e) {
            log.error("basicReject.error={}", e);
        }
    }

    //将消息重新放入mq的队尾中
    public void basicPublish(Message message, Channel channel){
        //重新发送消息到队尾
        try {
            channel.basicPublish(message.getMessageProperties().getReceivedExchange(), message.getMessageProperties().getReceivedRoutingKey(),
                    MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBody());
        } catch (Exception e) {
            log.error("basicPublish.error={}", e);
        }
    }

}
