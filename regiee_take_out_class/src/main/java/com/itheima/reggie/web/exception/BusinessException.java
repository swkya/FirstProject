package com.itheima.reggie.web.exception;

/**
 * @Author swk
 * @Date 2022/6/9 9:39
 * @Version 1.0
 */
public class BusinessException extends RuntimeException{
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
