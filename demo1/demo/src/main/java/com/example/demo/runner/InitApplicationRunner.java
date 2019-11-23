package com.example.demo.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.weixin.AccessToken;
import com.example.demo.weixin.AccessTokenInfo;
import com.example.demo.weixin.NetWorkUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class InitApplicationRunner implements ApplicationRunner {
    /*
     * 实现ApplicationRunner的方法会优先于实现CommandLineRunner接口的方法运行。
     * */
    @Override
    public void run(ApplicationArguments args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        //获取accessToken
                        AccessTokenInfo.accessToken = getAccessToken("wx1a083f0163a2da0f", "e30c7ca6de6c75674d3749333b384c9a");
                        //获取成功
                        if (AccessTokenInfo.accessToken != null) {
                            //获取到access_token 休眠7000秒,大约2个小时左右
                            Thread.sleep(7000 * 1000);
                            System.out.println("weixin token:" + AccessTokenInfo.accessToken);
                        } else {
                            //获取失败
                            Thread.sleep(1000 * 3); //获取的access_token为空 休眠3秒
                        }
                    } catch (Exception e) {
                        System.out.println("发生异常：" + e.getMessage());
                        e.printStackTrace();
                        try {
                            Thread.sleep(1000 * 10); //发生异常休眠1秒
                        } catch (Exception e1) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }

    private AccessToken getAccessToken(String appId, String appSecret) {
        NetWorkUtil netHelper = new NetWorkUtil();
        /**
         * 接口地址为https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET，其中grant_type固定写为client_credential即可。
         */
        String Url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appId, appSecret);
        //此请求为https的get请求，返回的数据格式为{"access_token":"ACCESS_TOKEN","expires_in":7200}
        String result = netHelper.getHttpsResponse(Url, "");
        System.out.println("获取到的access_token=" + result);

        //使用FastJson将Json字符串解析成Json对象
        JSONObject json = JSON.parseObject(result);
        AccessToken token = new AccessToken();
        token.setTokenName(json.getString("access_token"));
        token.setExpireSecond(json.getInteger("expires_in"));
        return token;
    }

}
