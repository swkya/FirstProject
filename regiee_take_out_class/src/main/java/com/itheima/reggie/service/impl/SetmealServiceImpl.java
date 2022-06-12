package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.dto.SetmealDto;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author swk
 * @Date 2022/6/11 11:36
 * @Version 1.0
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    CategoryService categoryService;
    /*套餐管理分类查询,携带套餐分类名称*/
    @Override
    public Page<SetmealDto> pageWithCategoryName(Integer currentPage, Integer pageSize, String name) {
        //封装分页对象
        Page<Setmeal> page = new Page<>(currentPage, pageSize);


        //封装查询条件对象
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //查
        this.page(page,queryWrapper);
        Page<SetmealDto> dtoPage = new Page<>();

        //复制基本数据
        BeanUtils.copyProperties(page,dtoPage,"records");
        //查询所有分类信息
        List<Category> categories = categoryService.list();

        //准备新的分页对象
        List<Setmeal> setmeals = page.getRecords();
        ArrayList<SetmealDto> dtos = new ArrayList<>();
        for (Setmeal setmeal : setmeals) {
            //获取从套餐中获取分类id
            Long cId = setmeal.getCategoryId();
            /*获取每个分类对象，通过分类id获取分类名称*/
            for (Category category : categories) {
                //如果找到了对应了对应id分类
                if (cId.equals(category.getId())) {
                    SetmealDto dto = new SetmealDto();

                    BeanUtils.copyProperties(setmeal,dto);
                    dto.setCategoryName(category.getName());
                    dtos.add(dto);

                    //跳出本次循环
                    break;
                }
            }

        }
            dtoPage.setRecords(dtos);
        //遍历原来分页对象中的records；
        return dtoPage;
    }
}
