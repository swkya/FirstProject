package com.itheima.reggie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author swk
 * @Date 2022/6/8 11:07
 * @Version 1.0
 */
/*打印启动日志*/
@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class ReggieApplicationClass {
    public static void main(String[] args) {
       SpringApplication.run(ReggieApplicationClass.class,args);
       log.info("项目启动成功。。。");

    }
}
