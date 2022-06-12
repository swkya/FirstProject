package com.itheima.reggie.common;
import com.itheima.reggie.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author swk
 * @Date 2022/6/9 9:52
 * @Version 1.0
 */
@Slf4j
//@Component //装配进spring容器
//只处理标有下述注解集合中任意一个的类抛出的一行
//某个类如果标有@RestController注解或者@Controller注解，抛出了异常，我才会处理，否则不处理
//@RestControllerAdvice=@ControllerAdvice+@ResponseBody
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {
    //定义方法，处理具体一场
    /*
    * 处理BusinessException异常
    * 参数：对应异常的参数，异常发生时，会将异常对象赋值给该参数*/
    @ExceptionHandler(BusinessException.class)//该方法处理哪些异常，通过该注解指定该方法处理哪些异常
    public R handleBusinessException(BusinessException e){
        //记录日志
        log.warn(e.getMessage());
        //提示用户,错误信息
       return R.fail(e.getMessage());


    }

}
