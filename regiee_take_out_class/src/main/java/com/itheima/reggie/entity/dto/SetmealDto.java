package com.itheima.reggie.entity.dto;

import com.itheima.reggie.entity.Setmeal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author swk
 * @Date 2022/6/12 17:15
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetmealDto extends Setmeal {
    private String categoryName;
}
