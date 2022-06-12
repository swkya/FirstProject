package com.itheima.reggie.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.dto.DishDto;
import com.itheima.reggie.service.DishService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author swk
 * @Date 2022/6/12 9:15
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    DishService dishService;


    /*保存包含口味的菜品*/
    @PostMapping
    public R saveWithFlavors(@RequestBody DishDto dishDto) throws SQLIntegrityConstraintViolationException {

        log.info("保存菜品内容{}", dishDto);
        boolean saveResult = dishService.saveWithFlavors(dishDto);
        if (saveResult) {
            return R.success("保存成功");
        }

        return R.fail("保存失败");

    }


    /*菜品分页查询*/

    @GetMapping("/page")
    public R<Page<DishDto>> page(@RequestParam("page") Integer currentPage, Integer pageSize, String name) {

        Page<DishDto> dishDtoPage = dishService.pageWithPageName(currentPage, pageSize, name);
        //组织数据并相应
        return R.success("查询成功", dishDtoPage);
    }

   /* @GetMapping("/{id}")
    public R<Dish> getById(@PathVariable Long id) {
        log.info("根据菜品id查询菜品信息，id为{}", id);
        //id非空判断
        if (id != null) {
            Dish dish = dishService.getById(id);
            if (dish != null) {
                return R.success("查询成功", dish);
            }
            return R.fail("查询失败");
        }
        return R.fail("参数有误");
    }*/


    /*根据id查询菜品包含口味信息
    * id查询条件*/
    @GetMapping("/{id}")
    public R<DishDto> getByWithFlavors(@PathVariable Long id) {
        log.info("根据菜品id查询菜品信息，id为{}", id);
        //id非空判断
        if (id != null) {
            DishDto dishDto = dishService.getByWithFlavors(id);
            if (dishDto != null) {
                return R.success("查询成功", dishDto);
            }
            return R.fail("查询失败");
        }
        return R.fail("参数有误");
    }


}
