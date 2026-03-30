package com.xu.timesheet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 工时记录系统启动类
 */
@SpringBootApplication(scanBasePackages = "com.xu")
@MapperScan("com.xu.timesheet.mapper")
public class TimesheetApplication {

    /**
     * 启动工时记录服务
     */
    public static void main(String[] args) {
        SpringApplication.run(TimesheetApplication.class, args);
    }
}
