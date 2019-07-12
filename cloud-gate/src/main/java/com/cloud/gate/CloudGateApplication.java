package com.cloud.gate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@RefreshScope //这个注解是当application.yml配置文件发生变化的时候，不需要手动的进行重启, 正在访问的客户端还是使用旧的配置文件，后来的会使用新的配置文件
@EnableDiscoveryClient
@SpringBootApplication
@EnableZuulProxy
@EnableFeignClients
@EnableCircuitBreaker //开启熔断
@Slf4j
public class CloudGateApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudGateApplication.class, args);
        log.info("--------------CloudGateApplication 启动成功--------------------------");
    }

}
