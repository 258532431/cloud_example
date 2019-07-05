package com.cloud.activiti;

import com.cloud.activiti.modeler.JsonpCallbackFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@Slf4j
@ComponentScan({"org.activiti.rest.diagram", "com.westcatr.rd"})
@EnableAsync
public class CloudActivitiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudActivitiApplication.class, args);
    }

    @Bean
    public JsonpCallbackFilter filter(){
        return new JsonpCallbackFilter();
    }
}
