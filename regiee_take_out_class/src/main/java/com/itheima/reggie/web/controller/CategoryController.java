package com.itheima.reggie.web.controller;

import com.alibaba.druid.sql.visitor.functions.If;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author swk
 * @Date 2022/6/11 9:39
 * @Version 1.0
 * @Description: 分类控制器，包含菜品分类和套餐分类
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    /*分类管理，新增菜品套餐分类*/
  /*  @PostMapping
    public R add(@RequestBody Category category){
        log.info("新增分类，信息：{}",category);
        boolean saveResult = categoryService.save(category);
       if (saveResult){
           return R.success("添加成功");
       }
       return R.fail("新增失败");6
    }*/

    @PostMapping
    public R add(@RequestBody Category category) {
        log.info("新增分类，信息：{}", category);
        boolean saveResult = categoryService.saveWithCheck(category);
        if (saveResult) {
            return R.success("添加成功");
        }
        return R.fail("新增失败");
    }

    /*分页查询
     * currentPage:当前页
     * pageSize 分页大小
     * 排序条件 主要条件sort次要条件updateTime*/
    @GetMapping("/page")
    public R<Page<Category>> page(@RequestParam("page") Integer currentPage, Integer pageSize) {
        log.info("所有分类分页查询，第{}页，每页{}条", currentPage, pageSize);

        //TODO 下面的逻辑代码应该写在service层

        //请求数据合理化设置
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        //创建分页查询的page对象
        Page<Category> page = new Page<>(currentPage, pageSize);
        //创建条件对象，并设置隐藏条件
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        //查询数据自动封装到page对象

        categoryService.page(page, qw);
        return R.success("查询条件", page);
    }


    //    按照id删除
    @DeleteMapping
    public R deleteById(Long id) {
        log.info("按照id删除 id为{}", id);
        //删除
        boolean deleteResult = categoryService.removeByIdCheckUse(id);
        if (deleteResult) {
            return R.success("删除成功");
        }
        return R.fail("删除失败");
    }

    //修改必须要携带id
    @PutMapping
    public R update(@RequestBody Category category) {
        log.info("编辑分类，新的信息数据如下{}", category);
        if (category.getId() != null) {
            boolean updateResult = categoryService.updateById(category);
            if (updateResult) {
                return R.success("修改成功");
            }
            return R.fail("修改失败");

        }
        return R.fail("参数异常");

    }

    /*查询分类*/
    @GetMapping("/list")
    public R<List<Category>> listByType(Long type) {
        if (type != null) {
            LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Category::getType, type);
            lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
            List<Category> categories = categoryService.list(lqw);
            if (categories != null && categories.size() > 0) {
                return R.success("查询分类成功", categories);
            }
            return R.fail("没有这种分类");

        }

        return R.fail("参数异常");
    }


}
