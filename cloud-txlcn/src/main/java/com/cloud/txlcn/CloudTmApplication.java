package com.cloud.txlcn;

import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableTransactionManagerServer  //LCN分布式事务服务端
@Slf4j
public class CloudTmApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudTmApplication.class, args);
        log.info("--------------CloudTmApplication 启动成功--------------------------");
    }

}
