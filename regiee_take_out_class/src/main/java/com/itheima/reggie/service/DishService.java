package com.itheima.reggie.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.dto.DishDto;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author swk
 * @Date 2022/6/11 11:31
 * @Version 1.0
 */

public interface DishService extends IService<Dish> {
    /*保存包含口味的菜品*/
    boolean saveWithFlavors(DishDto dishDto) throws SQLIntegrityConstraintViolationException;


    Page<DishDto> pageWithPageName(Integer currentPage,Integer pageSize,String name);

}
