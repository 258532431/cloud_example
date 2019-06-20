package com.cloud.common.entity;

import com.cloud.common.enums.ResponseCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: yangchenglong on 2019/6/14
 * @Description: 返回消息实体类
 * update by:
 */
@Data
public class ResponseMessage<T> implements Serializable{

	private boolean success;//是否成功
	private String errorCode;//错误代码
	private String msg;//返回信息
	private T data;//封装返回数据

	public ResponseMessage(ResponseCodeEnum responseCodeEnum, T data){
		this.errorCode = responseCodeEnum.getCode();
		this.data = data;
		this.msg = responseCodeEnum.getMsg();
		if(this.errorCode.equals(ResponseCodeEnum.RETURN_CODE_100200.getCode())){
			this.success = true;
		} else {
			this.success = false;
		}
	}

	public ResponseMessage(ResponseCodeEnum responseCodeEnum){
		this.errorCode = responseCodeEnum.getCode();
		this.msg = responseCodeEnum.getMsg();
		if(this.errorCode.equals(ResponseCodeEnum.RETURN_CODE_100200.getCode())){
			this.success = true;
		} else {
			this.success = false;
		}
	}

	public ResponseMessage(String errorCode, String msg){
		this.errorCode = errorCode;
		this.msg = msg;
		if(this.errorCode.equals(ResponseCodeEnum.RETURN_CODE_100200.getCode())){
			this.success = true;
		} else {
			this.success = false;
		}
	}

}