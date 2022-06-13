package com.wqw;

/**
 * @Author swk
 * @Date 2022/6/13 10:25
 * @Blog http://43.138.50.101:8081/
 * @Description: 首先创建一个车票的享元接口，定义一个查询车票信息方法和一个设置座位的方法：
 * @Version 1.0
 */

/*
 * 抽象享元角色
 */
public interface IShareTicket {
    //定义查询车票信息的方法info
    void info();
    //定义设置座位的方法setSeat
    //设置参数（形参）seatType（座位类型）
    void setSeat(String seatType);
}
