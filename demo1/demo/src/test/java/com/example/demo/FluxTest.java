package com.example.demo;

import com.example.demo.controller.HelloWorldController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = HelloWorldController.class)
public class FluxTest {
    @Autowired
    WebTestClient client;

    /*
     * 测试没成功，感觉和mvc冲突，项目得单独部署webflux
     * */
    @Test
    public void getHello() {
        client.get().uri("/flux").exchange().expectStatus().isOk();
    }
}
