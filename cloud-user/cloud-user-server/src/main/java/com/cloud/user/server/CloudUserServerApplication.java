package com.cloud.user.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan({"com.cloud.user.mapper"})
public class CloudUserServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudUserServerApplication.class, args);
    }

}
