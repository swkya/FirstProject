package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {

    /*提交订单，订单详细数据需要通过数据库查询得到*/
    boolean submit(Orders order);
}