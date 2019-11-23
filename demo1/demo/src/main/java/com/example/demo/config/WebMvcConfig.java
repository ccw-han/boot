package com.example.demo.config;

import com.example.demo.inteceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * 拦截器*/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /*方便跳转，类似于省去写controller的过程
    * 多个拦截器执行顺序

    如有两个拦截器

    (1)方法执行前返回值都为true,则顺序如下

    方法执行前1

    方法执行前2

    方法执行后2

    方法执行后1

    页面渲染后2

    页面渲染后1
    * */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/index.html").setViewName("login");

    }

    /*在Spring Boot1.0中，我们自定义的拦截器并不会对静态资源做出拦截，但是在Spring Boot2.0中，我们自定义的拦截器对静态资源同样做出了拦截。*/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/", "user/login", "/index.html")
                .excludePathPatterns("/public/**", "/resources/**");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //第一个方法设置访问路径前缀，第二个方法设置资源路径
        registry.addResourceHandler("/resources/**", "/public/**")
                .addResourceLocations("classpath:/resources/", "classpath:/public/");
    }

}
