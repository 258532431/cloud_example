package com.cloud.activiti;

import com.alibaba.fastjson.JSONObject;
import com.cloud.activiti.modeler.JsonpCallbackFilter;
import com.cloud.activiti.utils.RedisUtils;
import com.cloud.common.entity.ResponseMessage;
import com.cloud.common.enums.ResponseCodeEnum;
import com.cloud.common.utils.StringUtils;
import com.cloud.user.constant.UserConstants;
import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@EnableAsync  // 启用异步任务
@EnableDiscoveryClient
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class, org.activiti.spring.boot.SecurityAutoConfiguration.class})  //排除这个否则导致启动失败
@EnableFeignClients
@EnableCircuitBreaker //开启断路器功能
@EnableTransactionManagement   //启用事务管理
@EnableDistributedTransaction  //LCN分布式事务参与方
@Slf4j
@ComponentScan({"org.activiti.rest.diagram", "com.cloud.activiti"})
public class CloudActivitiApplication implements WebMvcConfigurer {

    @Value("${filter-path}")
    private String filterPath;//未登录需拦截路径

    @Value("${excludes-filter-path}")
    private String excludesFilterPath;//未登录不需要拦截的路径

    public static void main(String[] args) {
        SpringApplication.run(CloudActivitiApplication.class, args);
        log.info("-------------------CloudActivitiApplication 启动成功------------------------");
    }

    @Bean
    public JsonpCallbackFilter filter(){
        return new JsonpCallbackFilter();
    }

    @Bean
    public HandlerInterceptor getLoginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        String[] filterPaths = this.filterPath.split(",");
        String[] excludesFilterPaths = this.excludesFilterPath.split(",");
        //配置拦截路径
        registry.addInterceptor(getLoginInterceptor())
                .addPathPatterns(filterPaths)
                .excludePathPatterns(excludesFilterPaths);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/rest/api/doc/**").addResourceLocations("classpath:/swagger/dist/");
    }

    //登录拦截器
    @Component
    class LoginInterceptor implements HandlerInterceptor {

        @Resource
        private RedisUtils redisUtils;

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            String token = UserConstants.REDIS_PC_USER_TOKEN + ":" +request.getHeader(UserConstants.PC_ACCESS_TOKEN);
            Long expireTime = UserConstants.PC_SESSION_EXPIRETIME_SECONDS;
            if (StringUtils.isMobileDevice(request)) {//移动端
                token = UserConstants.REDIS_MOBILE_USER_TOKEN + ":" +request.getHeader(UserConstants.MOBILE_ACCESS_TOKEN);
                expireTime = UserConstants.MOBILE_SESSION_EXPIRETIME_SECONDS;
            }
            if (StringUtils.isNotBlank(token) && redisUtils.exists(token)) {
                redisUtils.refreshSessionCache(token, expireTime);
                return true;
            } else {
                String json = JSONObject.toJSONString(new ResponseMessage<>(ResponseCodeEnum.RETURN_CODE_100000));
                StringUtils.print(json);
                return false;
            }
        }

        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        }

    }
}
