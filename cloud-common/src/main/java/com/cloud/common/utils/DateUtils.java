package com.cloud.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: cloud_example
 * @description: 日期工具类
 * @author: yangchenglong
 * @create: 2019-06-27 14:54
 */
public class DateUtils {

    public static final String formatToStr = "yyyy-MM-dd HH:mm:ss";

    public static final String formatToNo = "yyyyMMddHHmmss";

    /**
     * @Author: yangchenglong on 2019/6/27
     * @Description: 日期转字符串
     * update by: 
     * @Param: 
     * @return: 
     */
    public static String DateToString(Date date, String format){
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String str = formatter.format(date);

        return str;
    }

}
