package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.mapper.EmployeeMapper;
import com.itheima.reggie.service.EmployeeService;
import com.itheima.reggie.utils.BaseContext;
import com.itheima.reggie.web.exception.BusinessException;
import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;




/*员工业务层实现类*/
/*自定义实现类
* 1.实现业务层接口 EmployeeService
* 2.继承自mybatis提供的通用实现类Service
*  该类需要指定两个泛型，分别是对应的Mapper和mode
*
* 注意要先继承，后实现*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService{

    //TODO 临时放 会优化
/*@Autowired
    HttpServletRequest request;*/

    //保存用户，并检查是否存在
    @Override
    public boolean saveWithCheckUserName(Employee employee) {
        //判断用户是否存在
        //1.根据用户名查询
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        String username = employee.getUsername();
        queryWrapper.eq(StringUtils.isNotBlank(username),Employee::getUsername, username);
        Employee emp = this.getOne(queryWrapper);
        if (emp !=null){
            throw new BusinessException("用户名已存在"+ username +"已存在");
            //这里不在需要return结束，因为我们没有try catch异常之后的代码不会继续执行
        }
        // 1. 对密码进行加密
        String pwd = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(pwd);

        // 2. 补全数据（推荐写在service层）


        employee.setStatus(1);
        //自动填充
       /*employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());*/

        // 2.1 获取当前登录用户的id
       /* Long employeeId = BaseContext.getCurrentId();
        employee.setCreateUser(employeeId);
        employee.setUpdateUser(employeeId);*/

        //保存用户
        this.save(employee);



        return true;
    }
}