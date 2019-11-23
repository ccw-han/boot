package com.example.demo.config;

import com.example.demo.listener.OnlineListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListenConfig {
    @Bean
    public OnlineListener init() {
        return new OnlineListener();
    }
}
