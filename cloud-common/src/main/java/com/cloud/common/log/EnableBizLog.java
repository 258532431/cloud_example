package com.cloud.common.log;

import java.lang.annotation.*;

/**
 * @Author: yangchenglong on 2019/6/10
 * @Description: 业务处理日志
 * update by:
 */
@Target({ElementType.METHOD}) //注解用于描述方法
@Retention(RetentionPolicy.RUNTIME) //始终不会丢弃，运行期也保留该注解，因此可以使用反射机制读取该注解的信息
@Documented //注解是否将包含在JavaDoc中
public @interface EnableBizLog {

    /**
     * @Author: yangchenglong on 2019/6/10
     * @Description: 操作模块
     */
    OperateModule operateModule();

    /**
     * @Author: yangchenglong on 2019/6/10
     * @Description: 操作类型
     */
    OperateType operateType();

    /**
     * @Author: yangchenglong on 2019/6/10
     * @Description: 日志级别
     */
    LogLevel logLevel() default LogLevel.INFO;

    enum LogLevel {
        INFO, WARN, ERROR
    }

    enum OperateType {
        INSERT("insert", "新增"),
        UPDATE("update", "修改"),
        DELETE("delete", "删除"),
        SELECT("select", "查询"),
        IMPORT("import", "导入"),
        EXPORT("export", "导出"),
        AUTHORIZE("authorize", "授权"),
        AUDIT("audit", "审批");

        private final String code;
        private final String name;

        OperateType(String code, String name) {
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

    enum OperateModule{
        USER("user", "用户"), GATE("gate", "路由");

        private final String code;
        private final String name;

        OperateModule(String code, String name) {
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

    enum VisitDeviceType {
        DEFAULT("unkown", "未知"),
        PC("pc", "PC"),
        MOBILE("mobile", "移动端");

        private final String code;
        private final String name;

        VisitDeviceType(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public static String getNameByCode(String code) {
            for (VisitDeviceType e : values()) {
                if(e.getCode().equals(code)) return e.getName();
            }
            return null;
        }
    }

}


