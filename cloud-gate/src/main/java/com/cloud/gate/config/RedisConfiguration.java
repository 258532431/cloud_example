package com.cloud.gate.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Author: yangchenglong on 2019/6/27
 * @Description: redis配置类
 * update by: 
 * @Param: 
 * @return: 
 */
@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    @ConditionalOnMissingBean(name="redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        try {
            RedisTemplate<Object, Object> template = new RedisTemplate<Object,Object>();
            template.setConnectionFactory(redisConnectionFactory);
            return template;
        } catch (Exception e){
            log.error("========> RedisConfiguration error : ", e);
            return null;
        }
    }

    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        try {
            StringRedisTemplate template = new StringRedisTemplate();
            template.setConnectionFactory(redisConnectionFactory);
            return template;
        } catch (Exception e){
            log.error("========> RedisConfiguration error : ", e);
            return null;
        }
    }

}
