package com.cloud.activiti;

import com.cloud.activiti.modeler.JsonpCallbackFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication(exclude = {org.activiti.spring.boot.SecurityAutoConfiguration.class})  //排除这个否则导致启动失败
@EnableFeignClients
@Slf4j
@ComponentScan({"org.activiti.rest.diagram", "com.westcatr.rd"})
//@EnableAsync  // 启用异步任务
public class CloudActivitiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudActivitiApplication.class, args);
        log.info("-------------------CloudActivitiApplication 启动成功------------------------");
    }

    @Bean
    public JsonpCallbackFilter filter(){
        return new JsonpCallbackFilter();
    }
}
