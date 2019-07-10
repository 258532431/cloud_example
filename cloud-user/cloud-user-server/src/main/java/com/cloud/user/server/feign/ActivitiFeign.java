package com.cloud.user.server.feign;

import com.cloud.common.config.GlobalException;
import com.cloud.common.entity.ResponseMessage;
import com.cloud.common.enums.ResponseCodeEnum;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Author: yangchenglong on 2019/7/9
 * @Description: 工作流服务接口
 * update by:
 */
@FeignClient(value = "cloud-activiti", fallbackFactory = ActivitiFeign.FeignFallbackConfig.class)
public interface ActivitiFeign {

    @RequestMapping(value = "/task/startTask", method = RequestMethod.POST)
    ResponseMessage startTask(@RequestParam("processDefinitionKey") String processDefinitionKey, @RequestParam("businessKey") String businessKey,
                              @RequestParam("businessType") String businessType, @RequestParam("variablesJson") String variablesJson);

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
            return new GlobalException(ResponseCodeEnum.RETURN_CODE_103000);
        }

    }

}
