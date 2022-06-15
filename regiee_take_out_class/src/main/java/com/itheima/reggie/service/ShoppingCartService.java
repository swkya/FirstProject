package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    /*加入购物车*/
    ShoppingCart add(ShoppingCart shoppingCart);


    /*根据用户id查询购物车*/
    List<ShoppingCart> listByUserId();

}