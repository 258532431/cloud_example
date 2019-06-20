package com.cloud.common.enums;

/**
 * @Author: yangchenglong on 2019/6/14
 * @Description: 请求返回码
 * update by:
 */
public enum ResponseCodeEnum {

    //通用返回码
    RETURN_CODE_100200("100200", "请求成功"),
    RETURN_CODE_100500("100500", "系统异常"),
    RETURN_CODE_100600("100600", "请求超时"),

    RETURN_CODE_100000("100000", "未登录"),
    RETURN_CODE_100001("100001", "权限不足"),
    RETURN_CODE_100002("100002", "参数不允许为空"),
    RETURN_CODE_100003("100003", "入参格式或类型错误"),

    RETURN_CODE_101000("101000", "服务异常"),

    //用户相关
    RETURN_CODE_101001("101001", "用户名或密码错误"),
    RETURN_CODE_101002("101002", "短信验证码错误"),
    RETURN_CODE_101003("101003", "图形验证码错误"),
    RETURN_CODE_101004("101004", "该用户已禁用"),
    RETURN_CODE_101005("101005", "该用户不存在");

    private final String code;
    private final String msg;

    private ResponseCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
