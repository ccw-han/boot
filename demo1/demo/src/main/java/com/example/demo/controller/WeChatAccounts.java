package com.example.demo.controller;

import com.example.demo.weixin.MessageUtil;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

@Controller
public class WeChatAccounts {

    private final String TOKEN = "ccw";

    //公众号第一次接入
    @RequestMapping(value = "/weChatServlet", method = RequestMethod.GET)
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("-----开始校验签名-----");

        /**
         * 接收微信服务器发送请求时传递过来的参数
         * 1微信官方注册验证
         * 2后天连接微信服务器，到此，我们的公众号应用已经能够和微信服务器正常通信了，也就是说我们的公众号已经接入到微信公众平台了。
         * 3通过https请求方式: GET
         * https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
         * 获取access_token去调用微信接口
         * 总结：
         * 每次访问地址，先经过微信和url的验证，然后再去转发到我们的地址，然后地址需要调其他功能接口，需要先获取access——token,
         * 然后接口会去给地址加上这个token，然后返回一个id，我们使用返回的信息直接返回或者返回自己的页面也可以
         */
        String signature = req.getParameter("signature");
        String timestamp = req.getParameter("timestamp");
        String nonce = req.getParameter("nonce"); //随机数
        String echostr = req.getParameter("echostr");//随机字符串

        /**
         * 将token、timestamp、nonce三个参数进行字典序排序
         * 并拼接为一个字符串
         */
        String sortStr = sort(TOKEN, timestamp, nonce);
        /**
         * 字符串进行shal加密
         */
        String mySignature = shal(sortStr);
        /**
         * 校验微信服务器传递过来的签名 和  加密后的字符串是否一致, 若一致则签名通过
         */
        if (!"".equals(signature) && !"".equals(mySignature) && signature.equals(mySignature)) {
            System.out.println("-----签名校验通过-----");
            resp.getWriter().write(echostr);
        } else {
            System.out.println("-----校验签名失败-----");
        }
    }

    @RequestMapping(value = "/weChatServlet", method = RequestMethod.POST)
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO 接收、处理、响应由微信服务器转发的用户发送给公众帐号的消息
        /*
         * 从微信公众平台接口消息指南中可以了解到，当用户向公众帐号发消息时，微信服务器会将消息通过POST方式提交给我们在接口配置信息中填写的URL，
         * 而我们就需要在URL所指向的请求处理类WxServlet的doPost方法中接收消息、处理消息和响应消息。
         * */
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        System.out.println("请求进入");
        String result = "";
        try {
            Map<String, String> map = MessageUtil.parseXml(req);

            System.out.println("开始构造消息");
            result = MessageUtil.buildXml(map);
            System.out.println(result);

            if (result.equals("")) {
                result = "未正确响应";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发生异常：" + e.getMessage());
        }
        resp.getWriter().write(result);
        resp.getWriter().println(result);
    }

    /**
     * 参数排序
     *
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    public String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 字符串进行shal加密
     *
     * @param str
     * @return
     */
    public String shal(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
