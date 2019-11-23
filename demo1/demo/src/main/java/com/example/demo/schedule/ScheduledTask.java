package com.example.demo.schedule;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
//读取配置文件的一种方式，在系统spring配置文件，直接使用不用导入
@PropertySource(value = "classpath:config/config.properties")
public class ScheduledTask {

    @Value("${note.cnyBTCUrl}")
    private String cnyBTCUrl;

    @Value("${note.cnyUSDTUrl}")
    private String cnyUSDTUrl;

    @Value("${note.cnyETHUrl}")
    private String cnyETHUrl;


    /**
     * 查询均价，往数据库中插入数据给datav使用的，数据库为funcoin，表为big_ave_price
     *
     * @param
     * @param
     * @return result.getData()
     */
    //3.添加定时任务
    //或直接指定时间间隔，例如：5分钟 异步执行
//    @Scheduled(cron = "0 0/1 * * * ?")
    @Async
    public void getAvePrice() {
        System.out.println("hello");
    }

}
