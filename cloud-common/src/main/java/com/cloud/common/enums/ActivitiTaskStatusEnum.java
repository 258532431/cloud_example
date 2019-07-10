package com.cloud.common.enums;

/**
 * @Author: yangchenglong on 2019/6/14
 * @Description: 工作流任务执行状态
 * update by:
 */
public enum ActivitiTaskStatusEnum {

    FINISH("finish", "执行完毕"),
    PROCESSING("processing", "执行中");

    private final String code;
    private final String msg;

    private ActivitiTaskStatusEnum(String code, String msg) {
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
