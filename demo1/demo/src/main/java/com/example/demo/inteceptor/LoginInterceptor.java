package com.example.demo.inteceptor;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginInterceptor implements HandlerInterceptor {
    /*
     * 第一拦截器，这个拦截器要实现HandlerInterceptor接口，这个接口里有四大方法，
     * preHandle是在请求controllor前调用，
     * postHandler在调用Controller方法之后、视图渲染之前调用，
     * afterCompletion是在渲染视图完成之后使用，
     * afterConcurrentHandlingStarted方法用来处理异步请求。*/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    // 返回失败消息
    public void render(HttpServletResponse response, String msg) throws IOException {
//        ResultObject resultObject = new ResultObject(-1, msg, null);
//
//        JSONObject object = new JSONObject(resultObject);
//
//        response.setContentType("application/json;charset=UTF-8");
//        OutputStream out = response.getOutputStream();
//        out.write(object.toString().getBytes("UTF-8"));
//        out.flush();
//        out.close();
    }
}
