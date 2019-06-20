package com.cloud.gate.config;

import com.cloud.common.enums.ResponseCodeEnum;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: yangchenglong on 2019/6/20
 * @Description: feigin断路回调处理
 * update by:
 */
@Component
public class FeignFallbackConfig implements FallbackFactory{

	@Override
	public Object create(Throwable throwable) {
		return new GlobalException(ResponseCodeEnum.RETURN_CODE_101000);
	}

}