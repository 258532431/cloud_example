package com.cloud.user.server;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan({"com.cloud.user.server.mapper"})
@Slf4j
public class CloudUserServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudUserServerApplication.class, args);
        log.info("-------------------CloudUserServerApplication 启动成功------------------------");
    }

}
