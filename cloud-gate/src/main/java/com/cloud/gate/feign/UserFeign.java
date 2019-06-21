package com.cloud.gate.feign;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.entity.ResponseMessage;
import com.cloud.common.enums.ResponseCodeEnum;
import com.cloud.gate.config.FeignFallbackConfig;
import com.cloud.user.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yangchenglong on 2019/6/14
 * @Description: 用户服务接口
 * update by:
 */
@FeignClient(value = "cloud-user", fallbackFactory = FeignFallbackConfig.class)
public interface UserFeign {

    @RequestMapping(value = "/base/login", method = RequestMethod.POST)
    User login(@RequestParam("username") String username, @RequestParam("password") String password);

}
