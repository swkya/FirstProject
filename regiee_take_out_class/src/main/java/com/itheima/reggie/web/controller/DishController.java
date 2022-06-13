package com.itheima.reggie.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.dto.DishDto;
import com.itheima.reggie.service.DishService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;

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

        log.info("保存菜品，信息如下：{}", dishDto.toString());

        boolean saveResult = dishService.saveWithFlavor(dishDto);

        if (saveResult) {
            return R.success("新增菜品成功");
        }
        return R.fail("新增菜品失败");

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

/*修改菜品及菜品口味信息*/
    @PutMapping
    public R update(@RequestBody DishDto dishDto){
        log.info("修改菜品，数据:{}",dishDto);
        //Id非空校验
        Long dishId = dishDto.getId();
        if (dishId!=null) {
          boolean updateResult = dishService.updateByIdWithFlavors(dishDto);
            if (updateResult) {
                return R.success("更新成功");
            }
            return R.fail("修改失败");
        }
        return R.fail("参数有误");

    }


    /*起售/禁售*/
    @PostMapping("/status/{status}")
    public R switchStatus(@PathVariable Integer status ,Long[] ids){
        log.info("批量修改菜品状态，目标状态{}，菜品id们{}",status==0?"禁售":"起售", Arrays.toString(ids));
        if (status!=null&&(status==0||status==1)) {
           boolean ssResult = dishService.switchStatus(status,ids);
           if (ssResult){
               return R.success("修改状态成功");
           }
            return R.fail("修改状态失败");
     }
        return R.fail("参数有误");
    }

    /*菜品批量删除 逻辑删除*/
@DeleteMapping
    public R deleteByIds( Long[] ids){
    log.info("本次删除的id为{}",ids);
    if (ids!=null){
        boolean updateResult =  dishService.updateByIds(ids);
        if (updateResult){
           return R.success("删除成功！");
        }
      return R.fail("删除失败！");

    }
   return R.fail("参数有误！");

}

    /*套餐添加，菜品的添加*/
    @GetMapping("/list")
    public R<List<Dish>> findDishWithCategoryId(Long categoryId){
        if (categoryId==null){
            return R.fail("查询失败!");
        }


        List<Dish> dish = dishService.findByCategoryId(categoryId);
        return R.success("菜品查询成功",dish);
    }


}
