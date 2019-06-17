package com.cloud.common.log;

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

    //获取操作模块数据列表
    public static List<LogData> getOperateModules () {
        return getEnumDatas(EnableBizLog.OperateModule.class);
    }

    //获取操作类型数据列表
    public static List<LogData> getLogLevels () {
        return getEnumDatas(EnableBizLog.LogLevel.class);
    }

    //获取操作类型数据列表
    public static List<LogData> getOperateTypes () {
        return getEnumDatas(EnableBizLog.OperateType.class);
    }

    //获取访问设备数据列表
    public static List<LogData> getVisitDeviceTypes () {
        return getEnumDatas(EnableBizLog.VisitDeviceType.class);
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
