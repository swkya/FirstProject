package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.entity.dto.DishDto;
import com.itheima.reggie.entity.dto.SetmealDto;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import com.itheima.reggie.web.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author swk
 * @Date 2022/6/11 11:36
 * @Version 1.0
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    CategoryService categoryService;
    @Autowired
    SetmealDishService setmealDishService;
    @Autowired
    SetmealMapper setmealMapper;
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

    /*新增套餐*/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWithDish(SetmealDto setmealDto) {
//        1. 如果套餐名称已存在，提示用户重名
        String setmealName = setmealDto.getName();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getName,setmealName);
        Setmeal setmeal = this.getOne(queryWrapper);
        if (setmeal!=null){
            throw new BusinessException(setmeal+"套餐已存在");
        }
//        2. 新增套餐基本信息，返回的套餐id会自动设置进entity
        this.save(setmealDto);
//        3. 保存套餐菜品明细
//        3.1 获取新增套餐菜品明细
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
//        3.2 遍历获取每个套餐菜品明细，添加套餐id
        setmealDishes.stream().map((setmealDish)->{
            //3.3 添加套餐id
            setmealDish.setSetmealId(setmealDto.getId());
            return setmealDish;
        }).collect(Collectors.toList());


//        3.4 保存套餐菜品明细
        setmealDishService.saveBatch(setmealDishes);
    }

    /*套餐修改回显*/
    @Override
    public SetmealDto getByWithDish(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);

//查询当前套餐对应的菜品信息，从setmeal_dish表查询
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(setmealDishes);
        return setmealDto;

    }

    /*套餐更新*/
    @Override
    public void updateWithDishs(SetmealDto setmealDto) {
        //更新setmeal表基本信息
        this.updateById(setmealDto);

        //清理当前套餐对应菜品数据---setmeal_dish表的delete操作
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        //添加当前提交过来的菜品数据---setmeal_dish表的insert操作
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes = setmealDishes.stream().map((dish) -> {
            dish.setSetmealId(setmealDto.getId());
            return dish;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);



    }


    /*菜品停售起售*/
    @Override
    public boolean switchStatus(Integer status, Long[] ids) {
     boolean result = setmealMapper.updateStatus(status,ids);
     return result;

    }

    /*逻辑删除套餐*/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateByIds(Long[] ids) {
        //如果套餐是起售状态，删除失败
        //尽量使用少的查询次数，查询出更多内容，这样mysql服务器压力会小一些
        //查询指定的套餐的启用的个数，如果个数大于0，就禁止删除
        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        //拼接 id in (xx,yy,zz)
        qw.in(Setmeal::getId,ids)
                //拼接 status=1
                .eq(Setmeal::getStatus,1);
        int onCount = this.count(qw);
        //如果启用中的套餐个数大一0
        if (onCount>0) {
            throw new BusinessException("禁止删除启用中的套餐");
        }
        //禁售状态，删除（setmeal表）
        boolean result = this.removeByIds(Arrays.asList(ids));
        if (!result){
            return false;
        }
        //3.删除套餐详情（setmeal_dish)表
        //根据套餐id删除改套餐id下包含的多个套餐详情（套餐对应的菜品）
        LambdaQueryWrapper<SetmealDish> rqw = new LambdaQueryWrapper<>();
        rqw.in(SetmealDish::getSetmealId,ids);
        //组织数据返回
       return setmealDishService.remove(rqw);



    }

    /*按照条件查询套餐*/
    @Override
    public List<Setmeal> listByCondtions(Setmeal setmeal) {
       //设置各种条件
        LambdaQueryWrapper<Setmeal> qw = new LambdaQueryWrapper<>();
        Long categoryId = setmeal.getCategoryId();
        String desc = setmeal.getDescription();
        String code = setmeal.getCode();
        String name = setmeal.getName();
        Integer status = setmeal.getStatus();
        /*设置各种条件*/
        qw.eq(categoryId !=null,Setmeal::getCategoryId, categoryId)
                .like(StringUtils.isNotBlank(code),Setmeal::getCode,code)
                .like(StringUtils.isNotBlank(desc),Setmeal::getDescription,desc)
                .like(StringUtils.isNotBlank(name),Setmeal::getName,name)
                .eq(status!=null,Setmeal::getStatus,status);

        List<Setmeal> setmeals = this.list(qw);


        return setmeals;
    }


}
