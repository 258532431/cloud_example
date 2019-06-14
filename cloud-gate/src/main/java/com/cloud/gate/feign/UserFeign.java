package com.cloud.gate.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: yangchenglong on 2019/6/14
 * @Description: 用户服务接口
 * update by:
 */
@FeignClient(value = "cloud-user", fallback = UserFeign.UserFeignCallBack.class)
public interface UserFeign {

    @RequestMapping("/base/login")
    public Object loginForPwd(@RequestParam("username") String username, @RequestParam("password") String password);

    class UserFeignCallBack {

        public Object fallback() {
//            return JSONObject.toJSONString(new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100500.getCode(), "用户服务异常"));
            return null;
        }

    }

}
