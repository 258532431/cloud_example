package com.cloud.gate.config;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.config.GlobalException;
import com.cloud.common.entity.ResponseMessage;
import com.cloud.common.enums.ResponseCodeEnum;
import com.cloud.common.utils.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: yangchenglong on 2019/6/14
 * @Description: 全局异常捕获
 * update by:
 */
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public void exceptionHandle(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        try {
            String json = null;
            if (exception instanceof GlobalException) {
                json = JSONObject.toJSONString(new ResponseMessage(((GlobalException) exception).getErrorCode(), exception.getMessage()));
            } else {
                json = JSONObject.toJSONString(new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100500.getCode(), exception.getMessage()));
            }
            StringUtils.print(json);
        } catch (Exception e) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100500, e.getMessage());
        }
    }

}
