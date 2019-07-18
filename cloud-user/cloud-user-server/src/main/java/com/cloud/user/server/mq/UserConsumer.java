package com.cloud.user.server.mq;

import com.cloud.common.mq.MqConstants;
import com.rabbitmq.client.Channel;
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
            log.info("UserConsumer.handleUserMessage={}", json);
            if("55".equals(json)){
                // 采用手动应答模式, 手动确认应答更为安全稳定
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            }else{//模拟失败
                this.basicNack(message, channel, true);
            }
        } catch (IOException e) {
            log.error("UserConsumer.handleUserMessage.error={}", e);
            //将消息重新放入 mq中
           this.basicNack(message, channel, true);
        }
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = MqConstants.QUEUE_TEST, durable = "true"),
            exchange = @Exchange(value = MqConstants.EXCHANGE_UP, durable = "true", type = ExchangeTypes.DIRECT),
            key = MqConstants.QUEUE_TEST))
    public void handleTestMessage(Message message, Channel channel){
        try {
            String json = new String(message.getBody());
            log.info("UserConsumer.handleTestMessage={}", json);
            // 采用手动应答模式, 手动确认应答更为安全稳定
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (IOException e) {
            log.error("UserConsumer.handleTestMessage.error={}", e);
            //将消息重新放入 mq中
            this.basicNack(message, channel, false);
        }
    }

    //将消息重新放入 mq中
    public void basicNack(Message message, Channel channel, boolean requeue){
        try {
            //最后一个参数 requeue 设置为true 会把消费失败的消息从新添加到队列的尾端，设置为false不会重新回到队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,requeue);
        } catch (IOException e) {
            log.error("basicNack.error={}", e);
        }
    }

}
