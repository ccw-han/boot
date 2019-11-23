package com.example.demo.exception;


import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackages = "com.example.demo.controller")
public class ControllerAdviceException {
    /*
        1全局异常处理，配置一个视图一个页面，不能对异常做复杂处理异常
        2自定义异常，实现  handlerexceptionresolver，可以随意catch然后put 到页面和异常信息，需要注册bean
        3注解@exceptionhandler({runtimeexception.class}) 局部处理，同一个controller下使用
        4容器最终异常处理
    *
    *
    * */
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView runtimeException(RuntimeException e) {
        e.printStackTrace();
        return new ModelAndView("error");
    }

    @InitBinder
    //// 这里value参数用于指定需要绑定的参数名称，如果不指定，则会对所有的参数进行适配，
    //    // 只有是其指定的类型的参数才会被转换
    // 这里@InitBinder标注的方法注册的Formatter在每次request请求进行参数转换时都会调用，用于判断指定的参数是否为其可以转换的参数。
    // 如下是我们声明的包含Date类型参数的接口： https://www.jb51.net/article/136447.htm详细使用
    public void globalInitBinder(WebDataBinder binder) {
        binder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }

    /*
    * @ModelAttribute
      关于@ModelAttribute的用法，
    * 处理用于接口参数可以用于转换对象类型的属性之外，其还可以用来进行方法的声明。
    * 如果声明在方法上，并且结合@ControllerAdvice，
    * 该方法将会在@ControllerAdvice所指定的范围内的所有接口方法执行之前执行，
    * 并且@ModelAttribute标注的方法的返回值还可以供给后续会调用的接口方法使用。
    * 如下是@ModelAttribute注解的声明：

    * */
    @ModelAttribute(value = "message")
    public String globalModelAttribute() {
        System.out.println("global model attribute.");
        return "this is from model attribute";
    }


}
