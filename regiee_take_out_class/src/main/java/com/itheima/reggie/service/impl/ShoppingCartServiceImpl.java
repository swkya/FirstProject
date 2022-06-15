package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.mapper.ShoppingCartMapper;
import com.itheima.reggie.service.ShoppingCartService;
import com.itheima.reggie.utils.BaseContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    /*加入购物车*/
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {

        //获取用户id，并设置进购物车项
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        //查询购物车项是否存在
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        if (dishId!=null) {
            qw.eq(ShoppingCart::getDishId, dishId);
        }
        else {
            qw.eq(ShoppingCart::getSetmealId,setmealId);
        }
        ShoppingCart scInData = this.getOne(qw);

        //存在，数量+1（忽略口味）
        if (scInData!=null) {
            scInData.setNumber(scInData.getNumber()+1);
        }
        else {
            //不存在，维护基础数据
            scInData = shoppingCart;
            scInData.setNumber(1);
        }

        //统一保存
        boolean result = saveOrUpdate(scInData);
        if (result) {
            return scInData;
        }
        else {
            return null;
        }

    }

    /*根据用户id查询购物车*/
    @Override
    public List<ShoppingCart> listByUserId() {
        //用户id
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> qw = new LambdaQueryWrapper<>();
        qw.eq(ShoppingCart::getUserId,userId)
                .orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCarts = this.list(qw);


        return shoppingCarts;
    }
}