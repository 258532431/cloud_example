package com.cloud.user.config;

import com.alibaba.fastjson.JSONObject;
import com.cloud.user.entity.ResponseMessage;
import com.cloud.user.enums.ResponseCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: yangchenglong on 2019/6/14
 * @Description: response全局响应代理
 * update by:
 */
@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    public HttpServletRequest request;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.hasMethodAnnotation(ResponseBody.class);
    }

    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest req, ServerHttpResponse res) {
        try {
            String json = JSONObject.toJSONString(new ResponseMessage(ResponseCodeEnum.RETURN_CODE_100200, obj));
            this.print(json);
        } catch (IOException e) {
            throw new GlobalException(ResponseCodeEnum.RETURN_CODE_100500, e.getMessage());
        }
        return null;
    }

    public static void print(Object object) throws IOException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        response.addHeader("Cache-Control", "no-cache");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(object);
        out.flush();
        out.close();
    }

}
