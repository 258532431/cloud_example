package com.cloud.gate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableZuulProxy
@EnableFeignClients
public class CloudGateApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudGateApplication.class, args);
    }

}
