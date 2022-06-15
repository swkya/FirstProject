package com.itheima.reggie.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.entity.dto.DishDto;
import com.itheima.reggie.entity.dto.SetmealDto;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;


    /*套餐管理分类查询，携带套餐分类名称*/
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(@RequestParam("page") Integer currentPage, Integer pageSize, String name) {

        //参合理化设置
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        Page<SetmealDto> page = setmealService.pageWithCategoryName(currentPage, pageSize, name);
        return R.success("查询成功", page);
    }

/*套餐新增*/
    @PostMapping
    public R save(@RequestBody SetmealDto setmealDto) {
        log.info("新增套餐，套餐信息：{}", setmealDto.toString());

        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /*套餐修改 数据回显*/
    @GetMapping("/{id}")
    public R<SetmealDto> findWithId(@PathVariable Long id) {
        log.info("根据套餐id查询套餐信息，id为{}", id);
        //id非空判断
        if (id != null) {
            SetmealDto setmealDto = setmealService.getByWithDish(id);
            if (setmealDto != null) {
                return R.success("查询成功", setmealDto);
            }
            return R.fail("查询失败");
        }
        return R.fail("参数有误");

    }

    /*修改套餐*/
    @PutMapping
    public R update(@RequestBody SetmealDto setmealDto) {
        log.info("修改的套餐信息为{}", setmealDto);
        setmealService.updateWithDishs(setmealDto);

        return R.success("菜品修改成功");
    }


    /*起售/禁售*/
    @PostMapping("/status/{status}")
    public R switchStatus(@PathVariable Integer status ,Long[] ids){
        log.info("批量修改套餐状态，目标状态{}，套餐id们{}",status==0?"禁售":"起售", Arrays.toString(ids));
        if (status!=null&&(status==0||status==1)) {
            boolean ssResult = setmealService.switchStatus(status,ids);
            if (ssResult){
                return R.success("修改状态成功");
            }
            return R.fail("修改状态失败");
        }
        return R.fail("参数有误");
    }

    /*删除 逻辑删除 修改删除字段*/
    @DeleteMapping
    public R deleteByIds( Long[] ids){
        log.info("本次删除的套餐id为{}",ids);
        if (ids.length>0){
            boolean updateResult =  setmealService.updateByIds(ids);
            if (updateResult){
                return R.success("删除成功！");
            }
            return R.fail("删除失败！");

        }
        return R.fail("参数有误！");

    }


    /*前台套餐展示*/
    @GetMapping("/list")
     public R<List<Setmeal>> list(Setmeal setmeal){
        log.info("条件查新套餐,条件为{}",setmeal);
        //调用service条件查询
     List<Setmeal> setmeals = setmealService.listByCondtions(setmeal);
        if (setmeals!=null) {
           return R.success("查询成功",setmeals);
        }
       return R.fail("查询失败");
     }

}