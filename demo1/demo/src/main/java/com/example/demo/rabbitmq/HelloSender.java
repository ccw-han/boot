package com.example.demo.rabbitmq;

import com.example.demo.entity.MyInfo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Component
public class HelloSender {
//    /*
//     * 生产者
//     * */
////    @Autowired
//    private AmqpTemplate rabbitTemplate;
//
//    public void send() {
//        String context = "hello " + new Date();
//        System.out.println("Sender : " + context);
//        this.rabbitTemplate.convertAndSend("hello", context);
//    }
//
//    public void send(MyInfo user) {
//        System.out.println("Sender object: " + user.toString());
//        this.rabbitTemplate.convertAndSend("object", user);
//    }
//
//    //topic 模式
//    public void send1() {
//        String context = "hi, i am message 1";
//        System.out.println("Sender : " + context);
//        this.rabbitTemplate.convertAndSend("exchange", "topic.message", context);
//    }
//
//    public void send2() {
//        String context = "hi, i am messages 2";
//        System.out.println("Sender : " + context);
//        this.rabbitTemplate.convertAndSend("exchange", "topic.messages", context);
//    }
//
//    //Fanout 模式
//    public void send4() {
//        String context = "hi, fanout msg ";
//        System.out.println("Sender : " + context);
//        this.rabbitTemplate.convertAndSend("fanoutExchange", "", context);
//    }

}
