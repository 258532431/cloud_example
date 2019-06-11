package com.cloud.gate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @program: cloud_example
 * @description: spring分布式session，基于redis实现
 * @author: yangchenglong
 * @create: 2019-06-11 15:29
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60)  //启用redis session，设置session过期时间，默认1800秒，注意这个时间redis会缓存
public class RedisSessionConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisAuth;

    @Value("${spring.redis.database}")
    private int redisDb;

    @Bean
    public RedisConnectionFactory lettuceConnectionFactory() {
        //单点redis
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        //哨兵redis
//        RedisSentinelConfiguration redisConfig = new RedisSentinelConfiguration();
        //集群redis
//        RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
        redisConfig.setHostName(redisHost);
        redisConfig.setPort(redisPort);
        redisConfig.setDatabase(redisDb);
        redisConfig.setPassword(RedisPassword.of(redisAuth));
        return new LettuceConnectionFactory(redisConfig);
    }

}
