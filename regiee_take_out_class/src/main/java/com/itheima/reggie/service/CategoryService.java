package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.mapper.CategoryMapper;

/**
 * @Author swk
 * @Date 2022/6/11 9:39
 * @Version 1.0
 */
public interface CategoryService extends IService<Category> {
    boolean saveWithCheck(Category category);

    boolean removeByIdCheckUse(Long id);
}
