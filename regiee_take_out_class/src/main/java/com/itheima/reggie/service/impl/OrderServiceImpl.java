package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.mapper.OrderMapper;
import com.itheima.reggie.service.*;
import com.itheima.reggie.utils.BaseContext;
import com.itheima.reggie.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
  @Autowired
  ShoppingCartService shoppingCartService;
  @Autowired
  AddressBookService addressBookService;
  @Autowired
  UserService userService;

  @Autowired
  OrderDetailService orderDetailService;

    /*提交订单，订单详细数据需要通过数据库查询得到
    * 包含了备注，支付方式，地址id*/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean submit(Orders order) {

        //获取用户id，需要用户名，查询数据库
        Long userId = BaseContext.getCurrentId();
        User user = userService.getById(userId);
        //获取购物车内容
        List<ShoppingCart> shoppingCarts = shoppingCartService.listByUserId();
        if (shoppingCarts==null || shoppingCarts.size()<=0) {
           // return false;
            throw new BusinessException("购物车为空，下单失败");
        }
        //获取地址信息
        AddressBook addressBook = addressBookService.getById(order.getAddressBookId());
        //组织订单订单基本信息
        order.setNumber(UUID.randomUUID().toString());
        order.setStatus(2);
        order.setUserId(userId);
        //order.setAddressBookId(); 无需设置
        order.setOrderTime(LocalDateTime.now());
        order.setCheckoutTime(LocalDateTime.now());
        //order.setRemark(); 无需设置
        order.setUserName(user.getName());
        order.setPhone(addressBook.getPhone());
        order.setAddress(addressBook.getDetail());
        order.setConsignee(addressBook.getConsignee());

        //计算总金额
        int orderSum = 0;
        for (ShoppingCart sc : shoppingCarts) {
            Integer amount = sc.getAmount();
            Integer number = sc.getNumber();
            orderSum+=amount*number;
        }
        order.setAmount(orderSum);
        //保证订单信息
        boolean result = this.save(order);
        if (result) {
            return false;
        }
        ArrayList<OrderDetail> ods = new ArrayList<>();
        //组织订单详情信息
        for (ShoppingCart sc : shoppingCarts) {
            OrderDetail od = new OrderDetail();
            od.setName(sc.getName());
            od.setOrderId(order.getId());
            od.setDishId(sc.getDishId());
            od.setSetmealId(sc.getSetmealId());
            od.setDishFlavor(sc.getDishFlavor());
            od.setNumber(sc.getNumber());
            od.setAmount(sc.getAmount());
            od.setImage(sc.getImage());
            ods.add(od);

        }

        //保存订单详情
        boolean odsResult = orderDetailService.saveBatch(ods);
        if (odsResult) {
            LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
            qw.eq(ShoppingCart::getUserId,userId);
            shoppingCarts.remove(qw);
        }
        return odsResult;


    }
}