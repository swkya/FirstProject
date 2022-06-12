package com.itheima.reggie.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author swk
 * @Date 2022/6/8 12:18
 * @Version 1.0
 * 登录dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    private String username;
    private String password;
}
