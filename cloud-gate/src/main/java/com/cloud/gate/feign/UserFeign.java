package com.cloud.gate.feign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yangchenglong on 2019/6/14
 * @Description: 用户服务接口
 * update by:
 */
@FeignClient(value = "cloud-user", fallback = UserFeign.UserFeignCallBack.class)
public interface UserFeign {

    @RequestMapping("/base/login")
    Object login(@RequestParam("username") String username, @RequestParam("password") String password);

    @Component
    class UserFeignCallBack implements UserFeign{

        @Override
        public Object login(String username, String password) {
            return fallback();
        }

        public Object fallback() {
            Map<String, String> map = new HashMap<>();
            map.put("code", "100500");
            map.put("msg", "用户服务异常");
            return JSONObject.toJSONString(map);
        }
    }

}
