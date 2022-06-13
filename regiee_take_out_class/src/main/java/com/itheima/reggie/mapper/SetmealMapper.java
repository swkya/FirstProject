package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author swk
 * @Date 2022/6/11 11:32
 * @Version 1.0
 */
@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
    /*起售停售*/
    boolean updateStatus(@Param("status") Integer status,@Param("ids") Long[] ids);

    /*逻辑删除字段*/
    boolean updateWithDeletedId(Long[] ids);

}
