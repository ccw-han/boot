package com.example.demo;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/*
 * 打成war包放入tomcat小，需要排除内置tomcat，然后这边是让外部tomcat识别
 *
 * */
public class SpringBootStartApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意的Application是启动类，就是main方法所属的类
        return builder.sources(DemoApplication.class);
    }

}
