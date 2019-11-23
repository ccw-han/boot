package com.example.demo;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableAsync
@EnableTransactionManagement //开启事务
//@ServletComponentScan //开启自动注解
//开启缓存
@EnableCaching
@SpringBootApplication
@MapperScan(basePackages = "com.example.demo.mapper")
public class DemoApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    /**
     * 向Spring容器中定义RestTemplate对象
     *cloud 使用的远程调用
     * @return
     */
//    @Bean
////    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
//    }

    /**
     * 配置FastJson方式二
     *
     * @return HttpMessageConverters
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        // 1.定义一个converters转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        // 2.添加fastjson的配置信息，比如: 是否需要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        // 3.在converter中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        // 4.将converter赋值给HttpMessageConverter
        HttpMessageConverter<?> converter = fastConverter;
        // 5.返回HttpMessageConverters对象
        return new HttpMessageConverters(converter);
    }

    @Override
    //跨域配置
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("*");
    }

    //可以直接使用封装好的模板调用okhttp请求
    /*泛型的声明，必须在方法的修饰符（public,static,final,abstract等）之后，返回值声明之前。
    public static <T> T request2Bean(HttpServletRequest request,Class<T> clazz){}
    其中第一个<T>是与传入的参数Class<T>相对应的，相当于返回值的一个泛型，后面的T是返回值类型，代表方法必须返回T类型的（由传入的Class<T>决定）
    getForObject("http://fantj.top/notice/list/"
                , Notice.class,map);
    https://www.cnblogs.com/javazhiyin/p/9851775.html 具体详细解释如何调用
    */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }

    //1jar包直接运行
    /*## 排除测试代码后进行打包
        mvn clean package  -Dmaven.test.skip=true
        启动 jar 包命令
        java -jar  target/spring-boot-scheduler-1.0.0.jar
        这种方式，只要控制台关闭，服务就不能访问了。下面我们使用在后台运行的方式来启动:

        nohup java -jar target/spring-boot-scheduler-1.0.0.jar &
        也可以在启动的时候选择读取不同的配置文件

        java -jar app.jar --spring.profiles.active=dev
        也可以在启动的时候设置 jvm 参数

        java -Xms10m -Xmx80m -jar app.jar &
    *
    * */
    // war包放入tomcat下
    /*
    * 1<packaging>war</packaging>
    * 2<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
            <scope>provided</scope> 重点
        </dependency>
     * 3、注册启动类
        创建 ServletInitializer.java，继承 SpringBootServletInitializer
        * 覆盖 configure()，把启动类 Application 注册进去。
        * 外部 Web 应用服务器构建 Web Application Context 的时候，会把启动类添加进去。
    *   4mvn clean package  -Dmaven.test.skip=true
    * */
    //重启
    /*
    * 启动方式：
        1、 可以直接./yourapp.jar 来启动
        2、注册为服务
        也可以做一个软链接指向你的jar包并加入到init.d中，然后用命令来启动。
        init.d 例子:
        ln -s /var/yourapp/yourapp.jar /etc/init.d/yourapp
        chmod +x /etc/init.d/yourapp
        这样就可以使用stop或者是restart命令去管理你的应用。
        /etc/init.d/yourapp start|stop|restart
        或者
        service yourapp start|stop|restart
    *
    * */
    //tomcatEmbedded 这段代码是为了解决，上传文件大于10M出现连接重置的问题。此异常内容 GlobalException 也捕获不到
    //上传下载
    @Bean
    public TomcatServletWebServerFactory tomcatEmbedded() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
                //-1 means unlimited
                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
            }
        });
        return tomcat;
    }
}
