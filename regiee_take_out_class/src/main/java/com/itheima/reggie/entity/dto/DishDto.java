package com.itheima.reggie.entity.dto;

import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author swk
 * @Date 2022/6/12 9:47
 * @Version 1.0
 */
/*包含口味的菜品*/
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
public class DishDto extends Dish {
    //菜品口味集合
    private List<DishFlavor> flavors;

    //菜品分类名称
    private String categoryName;
}
