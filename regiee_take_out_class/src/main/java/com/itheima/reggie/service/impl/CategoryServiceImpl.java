package com.itheima.reggie.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import com.itheima.reggie.web.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author swk
 * @Date 2022/6/11 9:39
 * @Version 1.0
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;


    //检查添加分类重名问题
    @Override
    public boolean saveWithCheck(Category category) {
        //构造查询条件
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        //设置查询条件
        queryWrapper.eq(Category::getName, category.getName());
        //查询，得到条件
        Category ca = this.getOne(queryWrapper);
//判断对象是否为null，如果存在则抛出业务异常
        if (ca != null) {
            throw new BusinessException("分类" + ca.getName() + "已经存在");
        }

        //保存
        this.save(category);
        return true;
    }


    /*删除分类，当该分类未被使用时*/
    @Override
    public boolean removeByIdCheckUse(Long id) {
        //TODO 注意：这里不区分分类类型，性能更好
        //因为这样做，最少查一次数据库，如果区分分类类型，需要两次查询数据库

        //检查该分类是否在被菜品使用
        LambdaQueryWrapper<Dish> qw = new LambdaQueryWrapper<>();
        qw.eq(id != null, Dish::getCategoryId, id);
        List<Dish> dishes = dishService.list(qw);
        //如果菜品分类在使用，就抛业务异常
        if (dishes != null && dishes.size() > 0) {
            throw new BusinessException("菜品分类在使用，禁止删除");
        }
        //检查该分类是否被套餐使用
        LambdaQueryWrapper<Setmeal> qw1 = new LambdaQueryWrapper<>();
        qw1.eq(id!=null,Setmeal::getCategoryId,id);

        List<Setmeal> setmeales = setmealService.list(qw1);
        if (setmeales != null && setmeales.size() > 0) {
            throw new BusinessException("套餐分类在使用，禁止删除");
        }
        //都没有才删除
        this.removeById(id);
        return true;
    }



}
