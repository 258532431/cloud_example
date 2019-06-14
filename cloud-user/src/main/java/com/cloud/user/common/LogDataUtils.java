package com.cloud.user.common;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: server
 * @description: 业务日志常量数据转集合
 * @author: yangchenglong
 * @create: 2019-06-14 10:49
 */
@Slf4j
public class LogDataUtils {

    //获取功能模块数据列表
    public static List<LogData> getOperateTypes () {
        return getEnumDatas(LogBiz.OperatingModule.class);
    }

    //获取访问设备数据列表
    public static List<LogData> getVisitDeviceTypes () {
        return getEnumDatas(LogBiz.VisitDeviceType.class);
    }

    private static List<LogData> getEnumDatas (Class<?> clz){
        List<LogData> list = new ArrayList<>();
        try {
            Object[] objects = clz.getEnumConstants();
            Method getCode = clz.getMethod("getCode");
            Method getName = clz.getMethod("getName");
            for (Object obj : objects) {
                LogData data = new LogData();
                data.setCode((String) getCode.invoke(obj));
                data.setName((String) getName.invoke(obj));
                list.add(data);
            }
        } catch (Exception e) {
            log.error("getEnumDatas error : {}", e);
        }

        return list;
    }

}
