package com.example.demo.controller;

import com.example.demo.entity.MyInfo;
import com.example.demo.entity.config.Config;
import com.example.demo.service.HelloService;
import com.example.demo.websocket.WebSocketServer;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
@Api
@Slf4j
public class HelloWorldController {
    @Autowired
    HelloService helloService;

    private static final Logger log = LoggerFactory.getLogger(HelloWorldController.class);


    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ApiImplicitParam(name = "itemName", value = "商品名称", required = false, dataType = "String")
    @ApiOperation(value = "HELLO", notes = "HELLO")
    //其中 value 的值就是缓存到 Redis 中的 key
//    @Cacheable(value="user-key")
    public Map loginController() {
        Map response = new HashMap();
        MyInfo myInfo = helloService.getMyInfo();
        log.info("我是ccw");
        log.warn("我是ccw");
        response.put("data", myInfo);
        return response;
    }

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            //重定向参数追加到重定向地址后的参数？
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {
//            // Get the file and save it somewhere
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
//            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    //websocket推送消息
    //页面请求
    @GetMapping("/socket/{cid}")
    public ModelAndView socket(@PathVariable String cid) {
        ModelAndView mav = new ModelAndView("/socket");
        mav.addObject("cid", cid);
        return mav;
    }
    /*
    *
    * <script>
        var socket;
        if(typeof(WebSocket) == "undefined") {
            console.log("您的浏览器不支持WebSocket");
        }else{
            console.log("您的浏览器支持WebSocket");
                //实现化WebSocket对象，指定要连接的服务器地址与端口  建立连接
                //等同于socket = new WebSocket("ws://localhost:8083/checkcentersys/websocket/20");
                socket = new WebSocket("${basePath}websocket/${cid}".replace("http","ws"));
                //打开事件
                socket.onopen = function() {
                    console.log("Socket 已打开");
                    //socket.send("这是来自客户端的消息" + location.href + new Date());
                };
                //获得消息事件
                socket.onmessage = function(msg) {
                    console.log(msg.data);
                    //发现消息进入    开始处理前端触发逻辑
                };
                //关闭事件
                socket.onclose = function() {
                    console.log("Socket已关闭");
                };
                //发生了错误事件
                socket.onerror = function() {
                    alert("Socket发生了错误");
                    //此时可以尝试刷新页面
                }
                //离开页面时，关闭socket
                //jquery1.8中已经被废弃，3.0中已经移除
                // $(window).unload(function(){
                //     socket.close();
                //});
        }
        </script>
    * */
    //推送数据接口
    //    @ResponseBody
    //    @RequestMapping("/socket/push/{cid}")
    //    public ApiReturnObject pushToWeb(@PathVariable String cid, String message) {
    //        try {
    //            WebSocketServer.sendInfo(message, cid);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //            return ApiReturnUtil.error(cid + "#" + e.getMessage());
    //        }
    //        return ApiReturnUtil.success(cid);
    //    }

    public static void main(String[] args) {
        // 获取系统运行时变量中的日志文件的输出路径
        // 此变量需要在启动命令中通过-D的方式设置
//        String baseDir = System.getProperty("base.dir");
//        if (baseDir == null) {
//            // 开发环境中使用
//            baseDir = System.getProperty("user.dir");
//            System.setProperty("base.dir", baseDir);
//        }
        log.info("==== 系统运行路径: " + System.getProperty("user.home") + " ====");
        // 其他处理逻辑......
    }

    /*
    * 过上面的示例可以发现，开发模式和之前 Spring Mvc 的模式差别不是很大，
    * 只是在方法的返回值上有所区别。
    just() 方法可以指定序列中包含的全部元素。
    响应式编程的返回值必须是 Flux 或者 Mono ，两者之间可以相互转换。
    * */
    @GetMapping("/flux")
    public Mono<String> hello() {
        return Mono.just("Welcome to reactive world ~");
    }

}
