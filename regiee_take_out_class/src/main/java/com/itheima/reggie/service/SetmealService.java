package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.dto.SetmealDto;

/**
 * @Author swk
 * @Date 2022/6/11 11:31
 * @Version 1.0
 */

public interface SetmealService extends IService<Setmeal> {

    /*套餐管理分类查询,携带套餐分类名称*/
    Page<SetmealDto> pageWithCategoryName(Integer currentPage, Integer pageSize, String name);
}
