package com.wqw;

import java.math.BigDecimal;

/**
 * @Author swk
 * @Date 2022/6/13 10:33
 * @Blog http://43.138.50.101:8081/
 * @Description: 定义一个实现类来实现IShareTicket接口
 * @Version 1.0
 */

/*
 * 具体享元角色(细粒度)
 */
public class TrainShareTicket implements IShareTicket {
    private String from;//内部状态
    private String to;//内部状态
    private String seatType = "站票";//外部状态

    public TrainShareTicket(String from, String to) {
        this.from = from;
        this.to = to;
    }

    /*重写设置座位的方法setSeat*/
    @Override
    public void setSeat(String seatType) {
        this.seatType = seatType;
    }

    /*重写查询车票信息的方法info*/
    @Override
    public void info() {
        System.out.println(from + "->" + to + ":" +
                seatType + this.getPrice(seatType));
    }

    private BigDecimal getPrice(String seatType) {
        BigDecimal value = null;
        switch (seatType) {
            case "硬座":
                value = new BigDecimal("100");
                break;
            case "硬卧":
                value = new BigDecimal("200");
                break;
            default:
                value = new BigDecimal("50");
        }
        return value;
    }
}
