package com.zhy.servicebase.exceptionhandler;


import com.zhy.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //指定什么异常执行该方法
    // 全局
    @ExceptionHandler(Exception.class)
    @ResponseBody //为了返回数据到客户端页面，之前是RestController = Controller + Responsebody
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("执行全局异常处理。。");
    }

    //特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody //为了返回数据到客户端页面，之前是RestController = Controller + Responsebody
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("执行ArithmeticException异常处理。。");
    }


    //自定义异常处理
    @ExceptionHandler(GuliException.class)
    @ResponseBody //为了返回数据到客户端页面，之前是RestController = Controller + Responsebody
    public R error(GuliException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().message(e.getMsg()).code(e.getCode());
    }


}
