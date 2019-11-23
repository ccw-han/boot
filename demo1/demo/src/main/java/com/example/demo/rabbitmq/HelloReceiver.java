package com.example.demo.rabbitmq;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


//@Component
//@RabbitListener(queues = "hello")
public class HelloReceiver {
//    private Logger log = LoggerFactory.getLogger(HelloReceiver.class);
//
//    /*
//     * 消费者
//     * 注意，发送者和接收者的 queue name 必须一致，不然不能接收
//     * */
////    @RabbitHandler
//    public void process(Message message, Channel channel) throws IOException {
//        // 采用手动应答模式, 手动确认应答更为安全稳定
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
//        log.info("receive: " + new String(message.getBody()));
//    }

}
