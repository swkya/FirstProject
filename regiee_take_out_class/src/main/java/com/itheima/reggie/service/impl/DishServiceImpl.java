package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.entity.dto.DishDto;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author swk
 * @Date 2022/6/11 11:33
 * @Version 1.0
 */

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {

    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    CategoryService categoryService;
    /*保存包含口味的菜品*/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveWithFlavors(DishDto dishDto) throws SQLIntegrityConstraintViolationException {
        //1.保存菜品基本信息
        this.save(dishDto);
        //2.获取菜品id
        Long dishId = dishDto.getId();
        //3.给菜品口味设置菜品id
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((flavor)->{
            flavor.setDishId(dishId);
            return flavor;
        }).collect(Collectors.toList());


        if (true) {
            throw new SQLIntegrityConstraintViolationException();
        }
        //4.调用dishFlavorService保存口味
        dishFlavorService.saveBatch(flavors);

        //组织结果并返回
        return true;
    }

    @Override
    public Page<DishDto> pageWithPageName(Integer currentPage,Integer pageSize, String name) {
        //检查并设置分页参数的合理性
        if (currentPage==null) {
            currentPage=1;
        }
        if (pageSize==null) {
            pageSize=10;
        }
        //条件查询
        Page<Dish> page = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),Dish::getName,name);
       //查询出来的page的records里面的每个dish只包含分类id，无分类名称
        this.page(page,queryWrapper);

        //创建新的page对象，内含dishDto
        Page<DishDto> dishDtoPage = new Page<>();
        //复制基本信息
        BeanUtils.copyProperties(page,dishDtoPage,"records");
        //查询所有(菜品)分类
        List<Category> categories = categoryService.list();
        //处理records中的dishDto对象分类名称
    List<DishDto> dishDtos = page.getRecords().stream()
                //获取每一个dish对象
                .map((dish)->{
                    //准备一个可以保存分类名称的dish的子类的dishDto对象
            DishDto dishDto = new DishDto();

            //复制dish的基本数据
                    BeanUtils.copyProperties(dish,dishDto);
            //根据dish中的分类id
            Long categoryId = dish.getCategoryId();
            //遍历所有分类集合，根据id查找名字
            for (Category category : categories) {
                if (categoryId.equals(category.getId()) ) {
                    //找到对应的
                    dishDto.setCategoryName(category.getName());
                }
            }
            return dishDto;
        }).collect(Collectors.toList());

    dishDtoPage.setRecords(dishDtos);

        return dishDtoPage;
    }
}
