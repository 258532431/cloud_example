package com.cloud.common.config;

import com.cloud.common.enums.ResponseCodeEnum;

/**
 * @Author: yangchenglong on 2019/6/14
 * @Description: 全局异常类
 * update by:
 */
public class GlobalException extends RuntimeException {

    /**
     * 错误码
     */
    private String errorCode;

    public GlobalException(ResponseCodeEnum responseCodeEnum){
        this(responseCodeEnum.getCode(), responseCodeEnum.getMsg());
    }

    public GlobalException(ResponseCodeEnum responseCodeEnum, String msg){
        this(responseCodeEnum.getCode(), msg);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode 错误编码
     * @param message 信息描述
     */
    public GlobalException(String errorCode, String message){
        super(message);
        this.setErrorCode(errorCode);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode 错误编码
     * @param message 信息描述
     * @param cause 异常
     */
    public GlobalException(String errorCode, String message, Throwable cause){
        super(message, cause);
        this.setErrorCode(errorCode);
    }
    
    public String getErrorCode(){
        return errorCode;
    }

    public void setErrorCode(String errorCode){
        this.errorCode = errorCode;
    }
    
}
