package com.itheima.reggie.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.dto.SetmealDto;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author swk
 * @Date 2022/6/12 17:16
 * @Version 1.0
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    SetmealService setmealService;


    /*套餐管理分类查询，携带套餐分类名称*/
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(@RequestParam("page") Integer currentPage , Integer pageSize , String name){

        //参合理化设置
        if (currentPage==null) {
            currentPage=1;
        }
        if (pageSize==null) {
            pageSize=10;
        }
     Page<SetmealDto> page = setmealService.pageWithCategoryName(currentPage,pageSize,name);
        return R.success("查询成功",page);
    }
}
