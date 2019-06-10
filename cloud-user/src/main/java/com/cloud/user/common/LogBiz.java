package com.cloud.user.common;

import java.lang.annotation.*;

/**
 * @Author: yangchenglong on 2019/6/10
 * @Description: 业务处理日志
 * update by:
 */
@Target({ElementType.METHOD}) //注解用于描述方法
@Retention(RetentionPolicy.RUNTIME) //始终不会丢弃，运行期也保留该注解，因此可以使用反射机制读取该注解的信息
@Documented //注解是否将包含在JavaDoc中
public @interface LogBiz {

    /**
     * @Author: yangchenglong on 2019/6/10
     * @Description: 操作人
     */
    public String operator();

    /**
     * @Author: yangchenglong on 2019/6/10
     * @Description: 操作模块
     */
    public OperatingModule operatingModule();

    /**
     * @Author: yangchenglong on 2019/6/10
     * @Description: 日志级别
     */
    public LogType level() default LogType.INFO;

    public enum LogType {
        INFO, WARN, ERROR
    }

    public enum OperatingModule{
        USER("user", "用户");

        private final String code;
        private final String name;

        OperatingModule(String code, String name) {
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

}


