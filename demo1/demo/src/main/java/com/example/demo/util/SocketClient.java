package com.example.demo.util;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

public  class SocketClient {

    /**
         * 构造函数
         * @param host 要连接的服务端IP地址
         * @param port 要连接的服务端对应的监听端口
         * @throws Exception
         */
        public  SocketClient(String host, int port) throws Exception {
            // 与服务端建立连接
            this.client = new Socket(host, port);
            System.out.println("Cliect[port:" + client.getLocalPort() + "] 与服务端建立连接...");
        }

        private Socket client;

        private Writer writer;

        /**
         * 发送消息
         * @param msg
         * @throws Exception
         */
        public void send(String msg) throws Exception {
            // 建立连接后就可以往服务端写数据了
            if(writer == null) {
                writer = new OutputStreamWriter(client.getOutputStream(), "UTF-8");
            }
            writer.write(msg);
            writer.write("eof\n");
            writer.flush();// 写完后要记得flush
            System.out.println("Cliect[port:" + client.getLocalPort() + "] 消息发送成功");
        }

        /**
         * 接收消息
         * @throws Exception
         */
        public void receive() throws Exception {
            // 写完以后进行读操作
            Reader reader = new InputStreamReader(client.getInputStream(), "UTF-8");
            // 设置接收数据超时间为10秒
            client.setSoTimeout(10*1000);
            char[] chars = new char[64];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = reader.read(chars)) != -1) {
                sb.append(new String(chars, 0, len));
            }
            System.out.println("Cliect[port:" + client.getLocalPort() + "] 消息收到了，内容:" + sb.toString());
            reader.close();

            // 关闭连接
            writer.close();
            client.close();
        }

    }

