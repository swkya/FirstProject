package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author swk
 * @Date 2022/6/11 11:31
 * @Version 1.0
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

/*更改起售停售状态*/
    boolean updateStatusByIds(@Param("status") Integer status,@Param("ids") Long[] ids);

    /*逻辑删除，修改删除字段参数*/
    boolean updateByIds(@Param("ids") Long[] ids);


}
