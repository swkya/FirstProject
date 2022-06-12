package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author swk
 * @Date 2022/6/11 11:31
 * @Version 1.0
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {


    boolean updateStatusByIds(@Param("status") Integer status,@Param("ids") Long[] ids);

}
