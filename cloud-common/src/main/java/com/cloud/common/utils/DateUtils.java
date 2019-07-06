package com.cloud.common.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @program: cloud_example
 * @description: 日期工具类
 * @author: yangchenglong
 * @create: 2019-06-27 14:54
 */
public class DateUtils {

    /**
     * @Description: yyyy-MM-dd HH:mm:ss
     */
    public static final String formatToStr = "yyyy-MM-dd HH:mm:ss";
    /**
     * @Description: yyyyMMddHHmmss
     */
    public static final String formatToNo = "yyyyMMddHHmmss";
    /**
     * @Description: yyyyMMddHHmmssSSS
     */
    public static final String formatToSSSNo = "yyyyMMddHHmmssSSS";

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

    /**
     * @Author: yangchenglong on 2019/7/6
     * @Description: 计算两个日期相差多少天
     * update by:
     * @Param:
     * @return:
     */
    public static long daysBetween(Date smallDate, Date bigDate) {
        LocalDate $smallDate = getLocalDate(smallDate);
        LocalDate $bigDate = getLocalDate(bigDate);
        return ChronoUnit.DAYS.between($smallDate, $bigDate);
    }

    public static LocalDateTime getLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate getLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
