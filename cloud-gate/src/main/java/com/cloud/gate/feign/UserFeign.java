package com.cloud.gate.feign;

import com.cloud.common.config.GlobalException;
import com.cloud.common.entity.ResponseMessage;
import com.cloud.common.enums.ResponseCodeEnum;
import com.cloud.user.entity.User;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: yangchenglong on 2019/6/14
 * @Description: 用户服务接口
 * update by:
 */
@FeignClient(value = "cloud-user", fallbackFactory = UserFeign.FeignFallbackConfig.class)
public interface UserFeign {

    @RequestMapping(value = "/base/login", method = RequestMethod.POST)
    ResponseMessage<User> login(@RequestParam("username") String username, @RequestParam("password") String password);

    /**
     * @Author: yangchenglong on 2019/6/20
     * @Description: feigin断路回调处理
     * update by:
     */
    @Component
    @Slf4j
    class FeignFallbackConfig implements FallbackFactory {

        @Override
        public GlobalException create(Throwable throwable) {
            return new GlobalException(ResponseCodeEnum.RETURN_CODE_101000);
        }

    }

}
