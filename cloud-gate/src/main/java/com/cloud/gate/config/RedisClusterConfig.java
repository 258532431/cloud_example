package com.cloud.gate.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yangchenglong on 2019/6/27
 * @Description: redis配置类-集群
 * update by: 
 * @Param: 
 * @return: 
 */
@Configuration
@Slf4j
public class RedisClusterConfig {

    @Value("${spring.redis.cluster.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.cluster.max-wait}")
    private long maxWaitMillis;
    @Value("${spring.redis.cluster.test-on-borrow}")
    private boolean testOnBorrow;
    @Value("${spring.redis.cluster.max-redirects}")
    private int maxRedirects;
    @Value("${spring.redis.cluster.nodes}")
    private String nodes;

    /**
     * 配置 Redis 连接池信息
     */
    @Bean
    public JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig =new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);

        return jedisPoolConfig;
    }

    /**
     * 配置 Redis Cluster 信息
     */
    @Bean
    public RedisClusterConfiguration getJedisCluster() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        redisClusterConfiguration.setMaxRedirects(maxRedirects);
        List<RedisNode> nodeList = new ArrayList<>();
        String[] cNodes = nodes.split(",");
        //分割出集群节点
        for(String node : cNodes) {
            String[] hp = node.split(":");
            nodeList.add(new RedisNode(hp[0], Integer.parseInt(hp[1])));
        }
        redisClusterConfiguration.setClusterNodes(nodeList);

        return redisClusterConfiguration;
    }

    /**
     * 配置 Redis 连接工厂
     */
    @Bean
    public JedisConnectionFactory getJedisConnectionFactory(RedisClusterConfiguration redisClusterConfiguration, JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration, jedisPoolConfig);
        return jedisConnectionFactory;
    }

    @Bean
    @ConditionalOnMissingBean(name="redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        try {
            RedisTemplate<Object, Object> template = new RedisTemplate<Object,Object>();
            template.setConnectionFactory(redisConnectionFactory);

            //默认使用的JdkSerializationRedisSerializer 这样的会导致我们通过redis desktop manager显示的我们key跟value的时候显示不是正常字符
            //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
            Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            serializer.setObjectMapper(mapper);

            template.setValueSerializer(serializer);
            //使用StringRedisSerializer来序列化和反序列化redis的key值
            template.setKeySerializer(new StringRedisSerializer());
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setHashValueSerializer(serializer);
            template.afterPropertiesSet();
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
