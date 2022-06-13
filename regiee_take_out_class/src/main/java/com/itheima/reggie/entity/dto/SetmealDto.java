package com.itheima.reggie.entity.dto;

import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author swk
 * @Date 2022/6/12 17:15
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetmealDto extends Setmeal {
    //分类名称
    private String categoryName;

    //套餐关联菜品集合
    private List<SetmealDish> setmealDishes;
}
