package com.cloud.center;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableEurekaServer
@SpringBootApplication
@Slf4j
public class CloudCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudCenterApplication.class, args);
        log.info("--------------CloudCenterApplication 启动成功--------------------------");
    }

    @EnableWebSecurity
    static class WebSecurityConfigure extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // 在/eureka/**端点忽略csrf验证, 否则其他服务无法注册到注册中心
            http.csrf().ignoringAntMatchers("/eureka/**");
            // 配置使请求需要通过httpBasic或form验证
            http.authorizeRequests().anyRequest()
                    .authenticated()
                    .and()
                    .formLogin()
                    .and()
                    .httpBasic();
            super.configure(http);
        }
    }

}
