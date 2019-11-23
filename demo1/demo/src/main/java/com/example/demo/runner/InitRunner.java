package com.example.demo.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class InitRunner implements CommandLineRunner {
    /*
    * CommandLineRunner 接口的 Component 会在所有 Spring Beans都初始化之后，
    * SpringApplication.run()之前执行，非常适合在应用程序启动之初进行一些数据初始化的工作。
    * */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("The Runner start to initialize ...");
    }
}
