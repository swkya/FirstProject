package com.itheima.reggie.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.service.AddressBookService;
import com.itheima.reggie.utils.BaseContext;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Queue;

/**
 * @Author swk
 * @Date 2022/6/14 19:30
 * @Blog http://43.138.50.101:8081/
 * @Description:
 * @Version 1.0
 */
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    AddressBookService addressBookService;

    /*地址列表查询*/
    @GetMapping("/list")
    public R<List<AddressBook>> findAll( AddressBook addressBook){
        /*条件构造器*/
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        /*首先获取记录在拦截器线程的当前登录的id*/
        Long currentId = BaseContext.getCurrentId();
        System.out.println("线程id"+currentId);
        /*设置当前登录id*/
        addressBook.setUserId(currentId);
        /*获取登录的id*/
        Long userId = addressBook.getUserId();
        System.out.println("当前id"+userId);
        if (userId!=null) {
        /*通过id查询*/
        queryWrapper.eq(AddressBook::getUserId, userId);
        /*按更新时间排序*/
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> addressBooks = addressBookService.list(queryWrapper);
        if (addressBooks!=null) {
            return R.success("查询成功！",addressBooks);
        }
        return R.fail("查询失败");
        }
        return R.fail("参数出错！");
    }

    /*新增地址*/
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook){
        /*先获取当前登录id设置进id*/
        addressBook.setUserId(BaseContext.getCurrentId());
        Long userId = addressBook.getUserId();
        if (userId!=null) {
            boolean result = addressBookService.save(addressBook);
            if (result) {
                return R.success("新增成功！", addressBook);
            }
            return R.fail("新增失败！");
        }
        return R.fail("参数错误，id未知");

    }

    /*设置默认地址*/
    @PutMapping("/default")
    public R updateDefault(@RequestBody AddressBook addressBook){
        /*获取登录id信息*/
        addressBook.setUserId(BaseContext.getCurrentId());
        Long userId = addressBook.getUserId();
        if (userId!=null) {
            //条件构造器
            LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
                //条件查询id查找这条数据
                updateWrapper.eq(AddressBook::getUserId, userId);
                /*设置默认地址的值*/
                updateWrapper.set(AddressBook::getIsDefault, 0);
                addressBookService.update(updateWrapper);

            addressBook.setIsDefault(1);
            addressBookService.updateById(addressBook);
            return R.success("修改默认值成功",addressBook);

        }
        return R.fail("id参数错误");

    }

    /*编辑地址数据回显*/
    @GetMapping("/{id}")
    public R<AddressBook> selectById(@PathVariable Long id){
        if (id!=null){
            LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AddressBook::getId,id);
            AddressBook addressBook = addressBookService.getOne(queryWrapper);
            return R.success("回显数据成功",addressBook);
        }
        return R.fail("参数有误");
    }

    /*编辑地址*/
    @PutMapping
    public R updateById(@RequestBody AddressBook addressBook){
        boolean result = addressBookService.updateById(addressBook);
        if (result) {
            return R.success("修改成功");
        }
        return R.fail("修改失败");
    }

    /*逻辑删除地址*/
    @DeleteMapping
    public R deleteById(long ids){

         boolean result = addressBookService.UpdateByIds(ids);
         if (!result){
             return R.fail("删除失败");
         }

         return R.success("删除成功");


    }
}
