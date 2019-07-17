package com.cloud.common.mq;

/**
 * @Author: yangchenglong on 2019/7/17
 * @Description: 处理类型
 * update by:
 */
public enum HandleTypeEum {

    INSERT("insert", "新增"),
    UPDATE("update", "修改"),
    DELETE("delete", "删除"),
    ORTHER("orther", "其他");

    private final String code;
    private final String name;

    private HandleTypeEum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
