package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Employee;

public interface EmployeeService extends IService<Employee> {


    //保存用户，并检查是否存在
    boolean saveWithCheckUserName(Employee employee);
}