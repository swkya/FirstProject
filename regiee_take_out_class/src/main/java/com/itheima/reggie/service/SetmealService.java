package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.dto.DishDto;
import com.itheima.reggie.entity.dto.SetmealDto;

import java.util.List;

/**
 * @Author swk
 * @Date 2022/6/11 11:31
 * @Version 1.0
 */

public interface SetmealService extends IService<Setmeal> {

    /*套餐管理分类查询,携带套餐分类名称*/
    Page<SetmealDto> pageWithCategoryName(Integer currentPage, Integer pageSize, String name);

    /*新增套餐*/
    void saveWithDish(SetmealDto setmealDto);

    /*套餐修改回显*/
    SetmealDto getByWithDish(Long id);

    /*套餐更新*/
   public void updateWithDishs(SetmealDto setmealDto);

   /*停售 起售*/
    boolean switchStatus(Integer status, Long[] ids);

    /*删除 逻辑删除 更改字段*/
    boolean updateByIds(Long[] ids);
/*按照条件查询套餐*/
    List<Setmeal> listByCondtions(Setmeal setmeal);
}
