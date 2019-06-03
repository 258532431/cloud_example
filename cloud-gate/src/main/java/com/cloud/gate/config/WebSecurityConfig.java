package com.cloud.gate.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @program: cloud_example
 * @description:
 * @author: yangchenglong
 * @create: 2019-05-31 21:26
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${permit-path}")
    private String permitPath;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .formLogin();
    }

    //不需要校验的路径处理
    private void permitAll(HttpSecurity http) throws Exception {
        if(!StringUtils.isBlank(permitPath)){
            for(String url : permitPath.split(",")){
                http.authorizeRequests().antMatchers(url).permitAll();
            }
            http.authorizeRequests()
                .anyRequest().authenticated()
                .and();
        }
    }

}
