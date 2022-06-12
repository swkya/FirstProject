package com.itheima.reggie.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    // 该成员变量序列化时，会序列化为String类型
   //  @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String username;
    private String name;
    private String password;
    private String phone;
    private String sex;
    private String idNumber; //驼峰命名法 ---> 映射的字段名为 id_number
    private Integer status; //0 禁用 1 正常

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //创建用户id
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    //更新用户id
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}