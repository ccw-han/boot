package com.example.demo.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class OnlineListener implements HttpSessionListener {
    // 记录当前登录用户数
    private int onlineNumber = 0;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // 监听到创建了新的session，在线用户累加1
        onlineNumber++;
        // 设置session属性
        se.getSession().getServletContext().setAttribute("onlineNumber", onlineNumber);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // 监听到销毁了旧的session，在线用户减少1
        onlineNumber--;
        // 设置session属性
        se.getSession().getServletContext().setAttribute("onlineNumber", onlineNumber);
    }

}
