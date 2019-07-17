package com.cloud.user.server.mq;

import com.cloud.common.mq.MqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @program: cloud_example
 * @description: 用户-消息队列消费者
 * @author: yangchenglong
 * @create: 2019-07-17 17:45
 */
@Component
@Slf4j
public class UserConsumer {

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = MqConstants.QUEUE_USER_SUFFIX, durable = "true"),
            exchange = @Exchange(value = MqConstants.EXCHANGE_UP_SUFFIX, durable = "true", type = ExchangeTypes.DIRECT),
            key = MqConstants.QUEUE_USER_SUFFIX))
    public void handleMessage(String json){
        log.info("UserConsumer.handleMessage={}", json);
    }

}
