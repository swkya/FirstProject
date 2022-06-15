package com.itheima.reggie.web.controller;

import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("加入购物车，信息{}",shoppingCart);

      ShoppingCart sc= shoppingCartService.add(shoppingCart);
        if (sc!=null) {
            return R.success("加入购物车成功",sc);
        }
        return R.fail("加入失败");

    }

    /*查看购物车*/
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车");
        //查看购物车
        List<ShoppingCart> shoppingCarts = shoppingCartService.listByUserId();
        if (shoppingCarts.size()==0) {
            return R.success("空空如也");
        }
        return R.success("查询购物车成功！",shoppingCarts);
    }
 
 }  