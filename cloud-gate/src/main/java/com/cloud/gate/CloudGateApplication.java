package com.cloud.gate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableDiscoveryClient
@SpringBootApplication
@EnableZuulProxy
@EnableFeignClients
@EnableRedisHttpSession    //增加redissession缓存支持
@Slf4j
public class CloudGateApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudGateApplication.class, args);
        log.info("--------------CloudGateApplication 启动成功--------------------------");
    }

}
