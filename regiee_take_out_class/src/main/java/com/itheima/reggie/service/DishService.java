package com.itheima.reggie.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.dto.DishDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * @Author swk
 * @Date 2022/6/11 11:31
 * @Version 1.0
 */

public interface DishService extends IService<Dish> {
    /*保存包含口味的菜品*/
    boolean saveWithFlavor(DishDto dishDto);


    Page<DishDto> pageWithPageName(Integer currentPage,Integer pageSize,String name);

    /*根据id查询菜品包含口味信息
     * id查询条件*/
    DishDto getByWithFlavors(Long id);

    /*修改菜品及菜品口味信息*/
    boolean updateByIdWithFlavors(DishDto dishDto);
    /*起售/禁售*/
    boolean switchStatus(Integer status, Long[] ids);

    /*删除菜品逻辑删除，更改删除字段*/
    boolean updateByIds(Long[] ids);




    List<Dish> findByCondtion(Long categoryId, String name);
}
