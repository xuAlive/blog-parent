package com.xu.calendar;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 日历系统启动类
 */
@SpringBootApplication(scanBasePackages = "com.xu")
@MapperScan("com.xu.calendar.mapper")
public class CalendarApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalendarApplication.class, args);
    }
}
