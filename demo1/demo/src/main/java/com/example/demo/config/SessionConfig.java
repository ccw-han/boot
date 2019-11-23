package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
//maxInactiveIntervalInSeconds: 设置 Session 失效时间，使用 Redis Session 之后，原 Spring Boot 的 server.session.timeout 属性不再生效。
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400 * 30)
//以后httpsession就可以直接用了，集群使用
//如何在两台或者多台中共享 Session
//其实就是按照上面的步骤在另一个项目中再次配置一次，启动后自动就进行了 Session 共享。
public class SessionConfig {
}
