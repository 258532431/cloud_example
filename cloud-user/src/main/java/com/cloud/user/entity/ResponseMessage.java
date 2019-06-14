package com.cloud.user.entity;

import com.cloud.user.enums.ResponseCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: yangchenglong on 2019/6/14
 * @Description: 返回消息实体类
 * update by:
 */
@Data
public class ResponseMessage implements Serializable{

	private boolean success;//是否成功
	private String errorCode;//错误代码
	private String msg;//返回信息
	private Object data;//封装返回数据

	public ResponseMessage(ResponseCodeEnum responseCodeEnum, Object object){
		this.errorCode = responseCodeEnum.getCode();
		this.data = object;
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